package com.stf.bj.app.game.bj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.stf.bj.app.game.AppSettings;
import com.stf.bj.app.game.players.BasicBot;
import com.stf.bj.app.game.players.BasicCountingBot;
import com.stf.bj.app.game.players.BasicIndexCountingBot;
import com.stf.bj.app.game.players.Human;
import com.stf.bj.app.game.players.Play;
import com.stf.bj.app.game.players.Player;
import com.stf.bj.app.game.players.PlayerType;
import com.stf.bj.app.game.players.RealisticBot;
import com.stf.bj.app.game.server.Card;
import com.stf.bj.app.game.server.Event;
import com.stf.bj.app.game.server.EventType;
import com.stf.bj.app.game.server.Ranks;
import com.stf.bj.app.game.server.Suits;
import com.stf.bj.app.game.server.Server;
import com.stf.bj.app.game.server.TableRules.PayAndCleanPlayerBlackjack;
import com.stf.bj.app.game.sprites.AnimationManager;
import com.stf.bj.app.game.sprites.Timer;

public class BjManager {
	Server table;
	int playerSpot = 0;
	private final List<Spot> spots;
	private final AppSettings settings;
	private final Timer timer;
	int cardsDealt;

	private enum GamePhase {
		WAGER, INSURANCE, PLAY, OTHER
	}

	private GamePhase gamePhase = GamePhase.OTHER;
	private int round = 0;

	public BjManager(AppSettings settings) {
		this.settings = settings;
		table = new Server(settings.getTableRules());
		spots = new ArrayList<Spot>();
		for (int spotIndex = 0; spotIndex < settings.getTableRules().getSpots(); spotIndex++) {
			spots.add(new Spot(spotIndex, settings));
		}
		timer = new Timer(settings.getTimingSettings());
	}

	public void shadyShit(List<Ranks> ranks) {
		ArrayList<Card> cards = new ArrayList<Card>();
		for (int i = 0; i < 52; i++) {
			for (Ranks r : ranks) {
				cards.add(new Card(r, Suits.CLUBS));
			}
		}
		table.RigCardsUnitTestMethod(cards);
	}

	public void openTable() {
		table.openTable();
	}

	public void tick(AnimationManager animationManager) {

		if (timer.hasDelay()) {
			timer.tickDelay();
			return;
		}

		processEvents(animationManager);

		if (timer.hasDelay()) {
			return;
		}

		switch (gamePhase) {
		case INSURANCE:
			if (!table.inInsurance()) {
				throw new IllegalStateException();
			}
			if (updateInsurances(animationManager)) {
				timer.insuranceTimerWageChanged();
			}
			if (timer.insuranceTimer()) {
				table.closeInsurance();
				animationManager.setDisplayString("");
			}
			break;
		case OTHER:
			break;
		case PLAY:
			if (!table.inPlay()) {
				throw new IllegalStateException();
			}
			Play move = spots.get(table.getCurrentSpot()).getPlayer().getMove(table.getCurrentHand(), table.canDouble(),
					table.canSplit(), table.canSurrender());
			if (move == null) {
				return;
			} else {
				switch (move) {
				case DOUBLEDOWN:
					table.doubleDown();
					break;
				case HIT:
					table.hit();
					break;
				case SPLIT:
					table.split();
					break;
				case STAND:
					table.stand();
					break;
				case SURRENDER:
					table.surrender();
					break;
				default:
					throw new IllegalArgumentException("Unsupported move");
				}
			}
			break;
		case WAGER:
			if (!table.acceptingWagers()) {
				throw new IllegalStateException();
			}
			if (updateWagers(table)) {
				timer.dealTimerWageChanged();
				if (allSpotsTheSame()) {
					timer.dealTimerSpeedUp();
				}
			}
			boolean timerDone = timer.dealTimer();
			if (table.canStartDeal() && timerDone && betsClean()) {
				table.startDeal();
			}
			break;
		default:
			throw new IllegalStateException();

		}
	}

	private boolean allSpotsTheSame() {
		for (Spot s : spots) {
			if (s.getPlayedLastRound() != s.singleBetOut()) {
				return false;
			}
		}
		return true;
	}

	private void updateSpotsIn() {
		for (Spot s : spots) {
			s.setPlayedLastRound();
		}
	}

	public void processEvents(AnimationManager animationManager) {
		while (table.hasNewEvent() && !timer.hasDelay()) {
			Event e = table.grabLastEvent();
			timer.setDelayForEventType(e.getType());

			for (Spot s : spots) {
				Player p = s.getPlayer();
				if (p == null)
					continue;
				p.sendEvent(e);
			}

			if (e.hasSpot()) {
				processSpotEvent(animationManager, e, spots.get(e.getSpotIndex()));
			} else {
				processTableEvent(animationManager, e);
			}
		}
	}

	private void processTableEvent(AnimationManager animationManager, Event e) {
		switch (e.getType()) {
		case DEALER_GAINED_CARD:
			animationManager.addDealerCard(e.getCard());
			trackCardDealt(animationManager);
			break;
		case DEALER_GAINED_FACE_DOWN_CARD:
			animationManager.addDealerCard(null);
			break;
		case DEAL_STARTED:
			gamePhase = GamePhase.OTHER;
			animationManager.setDisplayString("");
			animationManager.newDeal();
			round++;
			animationManager.debugText = settings.getTableRules().getDetails() + " - " + round;
			break;
		case DEALER_ENDED_TURN:
			break;
		case TABLE_OPENED:
			gamePhase = GamePhase.WAGER;
			timer.resetWagerTimer();
			animationManager.setDisplayString("Place your bets!");
			for (Spot s : spots) {
				s.clearCards();
			}
			animationManager.discardAllSprites(0);
			break;
		case INSURANCE_OFFERED:
			gamePhase = GamePhase.INSURANCE;
			timer.resetInsuranceTimer();
			animationManager.setDisplayString("Insurance / even money?");
			break;
		case INSURANCE_PAID:
			gamePhase = GamePhase.OTHER;
			payoutInsurance();
			break;
		case INSURANCE_COLLECTED:
			gamePhase = GamePhase.OTHER;
			collectInsurance();
			break;
		case PLAY_STARTED:
			gamePhase = GamePhase.PLAY;
			updateSpotsIn();
			break;
		case PLAY_FINISHED:
			gamePhase = GamePhase.OTHER;
			break;
		case DECK_SHUFFLED:
			animationManager.setDisplayString("Shuffling");
			resetCardDealt(animationManager);
			break;
		default:
			break;
		}
	}

	private void processSpotEvent(AnimationManager animationManager, Event e, Spot spot) {
		switch (e.getType()) {
		case PLAYER_BUSTED:
			spot.addBust(e.getHandIndex());
			animationManager.discardHand(spot.getHand(e.getHandIndex()).getSprite(), settings.getTimingSettings().getBustDelay());
			animationManager.updateHandPlacements(spot.getSprite(), settings.getTimingSettings().getBustDelay());
			break;
		case SPOT_GAINED_CARD:
			spot.addCard(e.getCard(), e.getHandIndex());
			trackCardDealt(animationManager);
			break;
		case SPOT_SPLIT:
			spot.addSplit(e.getHandIndex());
			animationManager.updateHandPlacements(spot.getSprite(), settings.getTimingSettings().getPlayDelay());
			break;
		case SPOT_STARTED_PLAY:
			spot.setInPlay(true);
			break;
		case SPOT_FINISHED_PLAY:
			spot.setInPlay(false);
			break;
		case SPOT_ONTO_NEXT_HAND:
			spot.ontoNextHand();
			break;
		case WIN_BLACKJACK:
			if (!spot.tookEvenMoney()) {
				spot.addChips(e.getHandIndex(), EventType.WIN_BLACKJACK.getPayout(), false);
			}
			if (settings.getTableRules().getPayAndCleanPlayerBlackjack() == PayAndCleanPlayerBlackjack.PLAY_START) {
				spot.clearCards();
				animationManager.discardSpot(spot.getSprite(), settings.getTimingSettings().getPayOutDelay());
			}
			break;
		case SPOT_DOUBLE:
			spot.setDoubled(e.getHandIndex());
			break;
		case SPOT_SURRENDER:
			animationManager.discardSpot(spot.getSprite(), settings.getTimingSettings().getPlayDelay());
			break;
		default:
			if (e.getType().isPayout() && !spot.tookEvenMoney()) {
				spot.addChips(e.getHandIndex(), e.getType().getPayout(), false);
			}
			break;
		}
	}

	private void resetCardDealt(AnimationManager animationManager) {
		cardsDealt = 0;
		animationManager.setPenetrationString(settings.getTableRules().getDecks() + " decks remaining");
	}
	
	private void trackCardDealt(AnimationManager animationManager) {
		cardsDealt++;
		if (cardsDealt % 26 == 0) {
			double totalCards = settings.getTableRules().getDecks() * 52.0;
			double decksLeft = (totalCards - cardsDealt) / 52.0;
			String penetration = decksLeft + " decks remaining";
			animationManager.setPenetrationString(penetration);
		}
	}

	public void input(int keyPressed) {
		for (Spot s : spots) {
			if (!s.isHuman()) {
				continue;
			}
			s.getHuman().sendInput(keyPressed);
		}
	}

	public List<Spot> getSpots() {
		return spots;
	}

	public void addPlayer(int spotIndex, PlayerType pt, Random r) {
		Player p = null;
		Spot spot = spots.get(spotIndex);
		switch (pt) {
		case BASIC_BOT:
			p = new BasicBot(settings, r, spot);
			break;
		case BASIC_COUNTING_BOT:
			p = new BasicCountingBot(settings, r, spot);
			break;
		case BASIC_INDEX_COUNTING_BOT:
			p = new BasicIndexCountingBot(settings, r, spot);
			break;
		case REALISTIC_BOT:
			p = new RealisticBot(settings, r, spot);
			break;
		case HUMAN:
			p = new Human(spot, settings);
			break;
		default:
			throw new IllegalArgumentException("Player Type not yet supported");

		}
		spot.addPlayer(p);
		p.setSpot(spot.getIndex()); // TODO is this really needed?

	}

	private boolean updateWagers(Server table) {
		boolean updated = false;
		for (Spot s : spots) {
			if (s.getPlayer() == null)
				continue;
			if (updateWager(s, table)) {
				updated = true;
			}
		}
		return updated;
	}

	private boolean updateWager(Spot s, Server table) {
		double newWager = s.getPlayer().getWager();
		double oldWager = s.getWager();
		if (newWager < 0) {// No update from player
			if (table.canActivateSpot(s.getIndex()) && s.singleBetOut()) {
				table.activateSpot(s.getIndex());
				return true;
			}
			return false;
		} else if (newWager == 0 && oldWager == 0) { // Player said no bet and they already weren't betting
			return false;
		} else if (newWager == oldWager && !table.canActivateSpot(s.getIndex())) { // Player said same bet, and is
																					// already in
			return false;
		}

		// We actually have to update chips here
		s.removeAllChips();
		s.setWager(newWager);
		if (newWager == 0) {
			if (table.canDeactivateSpot(s.getIndex())) {
				table.deactivateSpot(s.getIndex());
			}
			return true;
		}

		s.addChips(0, 1, true);
		if (table.canActivateSpot(s.getIndex()))
			table.activateSpot(s.getIndex());
		return true;
	}

	public String getDisplayString(int spot) {
		return spots.get(spot).getChipDisplay();
	}

	public String getDisplayString(int spot, int hand) {
		return spots.get(spot).getHand(hand).getChipDisplay();
	}

	private boolean betsClean() {
		for (Spot s : spots) {
			if (!s.isReady()) {
				return false;
			}
		}
		return true;
	}

	private boolean updateInsurances(AnimationManager sm) {
		boolean updated = false;
		for (Spot s : spots) {
			if (s.getPlayer() == null)
				continue;
			if (!s.singleBetOut())
				continue;
			if (updateInsurance(sm, s)) {
				updated = true;
			}
		}
		return updated;
	}

	private boolean updateInsurance(AnimationManager sm, Spot s) {
		boolean newPlay = s.getPlayer().getInsurancePlay();
		boolean oldPlay = s.isBettingInsurance();

		if (settings.getAnimationSettings().isImmediatelyPayEvenMoney() && newPlay && s.isBlackjack()) {
			payTakeEvenMoney(sm, s);
			return true;
		}

		if (newPlay != oldPlay) {
			s.setBettingInsurance(newPlay);
			return true;
		} else {
			return false;
		}
	}

	private void payTakeEvenMoney(AnimationManager sm, Spot s) {
		s.addChips(0, 1, false);
		s.setTookEvenMoney();
		sm.discardSpot(s.getSprite(), settings.getTimingSettings().getPayOutDelay());
	}

	private void payoutInsurance() {
		for (Spot s : spots) {
			if (s.isBettingInsurance()) {
				s.payInsurance();
			}
		}
	}

	private void collectInsurance() {
		for (Spot s : spots) {
			if (s.isBettingInsurance()) {
				s.takeInsurance();
			}
		}
	}

}
