package com.stf.bj.app.bj;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.stf.bj.app.AppSettings;
import com.stf.bj.app.players.BasicBot;
import com.stf.bj.app.players.BasicCountingBot;
import com.stf.bj.app.players.BasicIndexCountingBot;
import com.stf.bj.app.players.Human;
import com.stf.bj.app.players.Play;
import com.stf.bj.app.players.Player;
import com.stf.bj.app.players.PlayerType;
import com.stf.bj.app.sprites.AnimationSettings;
import com.stf.bj.app.sprites.SpriteManager;
import com.stf.bj.app.sprites.Timer;
import com.stf.bj.app.table.Card;
import com.stf.bj.app.table.Event;
import com.stf.bj.app.table.EventType;
import com.stf.bj.app.table.Ranks;
import com.stf.bj.app.table.Suits;
import com.stf.bj.app.table.Table;
import com.stf.bj.app.table.TableRules;
import com.stf.bj.app.table.TableRules.PayAndCleanPlayerBlackjack;

public class BjManager {
	Table table;
	int playerSpot = 0;
	private final List<Spot> spots;
	private final AppSettings settings;
	private final Timer timer;

	private enum GamePhase {
		WAGER, INSURANCE, PLAY, OTHER
	}

	private GamePhase gamePhase = GamePhase.OTHER;

	public BjManager(AppSettings settings) {
		this.settings = settings;
		table = new Table(settings.getTableRules());
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

	public void tick(SpriteManager sm) {

		if(timer.hasDelay()) {
			timer.tickDelay();
			return;
		}

		processEvents(sm);
		
		if(timer.hasDelay()) {
			return;
		}
		
		switch (gamePhase) {
		case INSURANCE:
			if(!table.inInsurance()) {
				throw new IllegalStateException();
			}
			if (updateInsurances(sm)) {
				timer.insuranceTimerWageChanged();
			}
			if (timer.insuranceTimer()) {
				table.closeInsurance();
				sm.setDisplayString("");
			}
			break;
		case OTHER:
			break;
		case PLAY:
			if(!table.inPlay()) {
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
				default:
					throw new IllegalArgumentException("Unsupported move");
				}
			}
			break;
		case WAGER:
			if(!table.acceptingWagers()) {
				throw new IllegalStateException();
			}
			if (updateWagers(table)) {
				timer.dealTimerWageChanged();
			}
			if (table.canStartDeal()) {
				if (timer.dealTimer() && betsClean()) {
					table.startDeal();
				}
			}
			break;
		default:
			throw new IllegalStateException();

		}
	}

	public void processEvents(SpriteManager sm) {
		while (table.hasNewEvent() && !timer.hasDelay()) {
			Event e = table.grabLastEvent();
			System.out.println("Processed " + Gdx.graphics.getFrameId() + " " + e);
			timer.setDelayForEventType(e.getType());
			

			for (Spot s : spots) {
				Player p = s.getPlayer();
				if (p == null)
					continue;
				p.sendEvent(e);
			}

			if (e.hasSpot()) {
				processSpotEvent(sm, e, spots.get(e.getSpotIndex()));
			} else {
				processTableEvent(sm, e);
			}
		}
	}

	private void processTableEvent(SpriteManager sm, Event e) {
		switch (e.getType()) {
		case DEALER_GAINED_CARD:
			sm.addDealerCard(e.getCard());
			break;
		case DEALER_GAINED_FACE_DOWN_CARD:
			sm.addDealerCard(null);
			break;
		case DEAL_STARTED:
			gamePhase = GamePhase.OTHER;
			sm.setDisplayString("");
			sm.newDeal();
			break;
		case DEALER_ENDED_TURN:
			break;
		case TABLE_OPENED:
			gamePhase = GamePhase.WAGER;
			sm.setDisplayString("Place your bets!");
			for (Spot s : spots) {
				s.clearCards();
			}
			sm.discardAllSprites();
			break;
		case INSURANCE_OFFERED:
			gamePhase = GamePhase.INSURANCE;
			sm.setDisplayString("Insurance / even money?");
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
			break;
		case PLAY_FINISHED:
			gamePhase = GamePhase.OTHER;
			break;
		default:
			break;
		}
	}

	private void processSpotEvent(SpriteManager sm, Event e, Spot spot) {
		switch (e.getType()) {
		case PLAYER_BUSTED:
			spot.addBust(e.getHandIndex());
			sm.discardHand(spot.getHand(e.getHandIndex()).getSprite());
			sm.updateHandPlacements(spot.getSprite());
			break;
		case SPOT_GAINED_CARD:
			spot.addCard(e.getCard(), e.getHandIndex());
			sm.addMovingSprite(1);
			break;
		case SPOT_SPLIT:
			spot.addSplit(e.getHandIndex());
			sm.updateHandPlacements(spot.getSprite());
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
				sm.discardSpot(spot.getSprite());
			}
			break;
		case SPOT_DOUBLE:
			spot.setDoubled(e.getHandIndex());
			break;
		default:
			if (e.getType().isPayout() && !spot.tookEvenMoney()) {
				spot.addChips(e.getHandIndex(), e.getType().getPayout(), false);
			}
			break;
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

	public void addPlayer(int spotIndex, PlayerType pt) {
		Player p = null;
		Spot spot = spots.get(spotIndex);
		switch (pt) {
		case BASIC_BOT:
			p = new BasicBot(settings.getTableRules());
			break;
		case BASIC_COUNTING_BOT:
			p = new BasicCountingBot(settings.getTableRules());
			break;
		case BASIC_INDEX_COUNTING_BOT:
			p = new BasicIndexCountingBot(settings.getTableRules());
			break;
		case HUMAN:
			p = new Human();
			break;
		default:
			throw new IllegalArgumentException("Player Type not yet supported");

		}
		spot.addPlayer(p);
		p.setSpot(spot.getIndex()); // TODO is this really needed?

	}

	private boolean updateWagers(Table table) {
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

	private boolean updateWager(Spot s, Table table) {
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

	private boolean updateInsurances(SpriteManager sm) {
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

	private boolean updateInsurance(SpriteManager sm, Spot s) {
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

	private void payTakeEvenMoney(SpriteManager sm, Spot s) {
		s.addChips(0, 1, false);
		s.setTookEvenMoney();
		sm.discardSpot(s.getSprite());
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
