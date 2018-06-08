package com.stf.bj.app.bj;

import java.util.ArrayList;
import java.util.List;

import com.stf.bj.app.players.BasicBot;
import com.stf.bj.app.players.BasicCountingBot;
import com.stf.bj.app.players.BasicIndexCountingBot;
import com.stf.bj.app.players.Human;
import com.stf.bj.app.players.Play;
import com.stf.bj.app.players.Player;
import com.stf.bj.app.players.PlayerType;
import com.stf.bj.app.sprites.AnimationSettings;
import com.stf.bj.app.sprites.SpriteManager;
import com.stf.bj.app.table.Card;
import com.stf.bj.app.table.Event;
import com.stf.bj.app.table.Ranks;
import com.stf.bj.app.table.Suits;
import com.stf.bj.app.table.Table;
import com.stf.bj.app.table.TableRules;
import com.stf.bj.app.table.TableRules.PayAndCleanPlayerBlackjack;

public class BjManager {
	private static final int BUST_TIMER_BASE = 150;
	Table table;
	private final TableRules rules;
	int playerSpot = 0;
	int timer = 0;
	private static final int DEAL_TIMER_BASE = 150;
	private static final int DEAL_TIMER_RESET = 100;
	private static final int INSURANCE_TIMER_BASE = 300;
	private static final int INSURANCE_TIMER_RESET = 100;
	private final List<Spot> spots;
	private final AnimationSettings animationSettings;

	boolean insuranceMode = false;

	public BjManager(TableRules rules, AnimationSettings animationSettings) {
		this.animationSettings = animationSettings;
		table = new Table(rules);
		this.rules = rules;
		spots = new ArrayList<Spot>();
		for (int spotIndex = 0; spotIndex < rules.getSpots(); spotIndex++) {
			spots.add(new Spot(spotIndex, rules.getSplits() + 1));
		}
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
		if (insuranceMode) {
			if (updateInsurances(sm)) {
				insuranceTimerWageChanged();
			}
			if (insuranceTimer()) {
				insuranceMode = false;
				sm.setDisplayString("");
			}

		} else if (table.acceptingWagers()) {
			if (updateWagers(table)) {
				dealTimerWageChanged();
			}
			if (table.canStartDeal()) {
				if (dealTimer() && betsClean()) {
					table.startDeal();
				}
			}
		} else if (table.inPlay()) {
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
		}
	}

	private void dealTimerWageChanged() {
		if (timer > DEAL_TIMER_RESET)
			timer = DEAL_TIMER_RESET;
	}

	private boolean dealTimer() {
		timer++;
		if (timer > DEAL_TIMER_BASE) {
			timer = 0;
			return true;
		}
		return false;
	}

	private void insuranceTimerWageChanged() {
		if (timer > INSURANCE_TIMER_RESET)
			timer = INSURANCE_TIMER_RESET;
	}

	private boolean insuranceTimer() {
		timer++;
		if (timer > INSURANCE_TIMER_BASE) {
			timer = 0;
			return true;
		}
		return false;
	}

	public boolean processEvents(SpriteManager sm) {
		boolean takeRenderBreak = false;
		while (table.hasNewEvent() && !takeRenderBreak && !insuranceMode) {
			Event e = table.grabLastEvent();

			for (Spot s : spots) {
				Player p = s.getPlayer();
				if (p == null)
					continue;
				p.sendEvent(e);
			}

			if (e.hasSpot()) {
				takeRenderBreak = processSpotEvent(sm, e, spots.get(e.getSpotIndex()));
			} else {
				takeRenderBreak = processTableEvent(sm, e);
			}
		}
		return takeRenderBreak;
	}

	private boolean processTableEvent(SpriteManager sm, Event e) {
		switch (e.getType()) {
		case DEALER_GAINED_CARD:
			sm.addDealerCard(e.getCard());
			return true;
		case DEALER_GAINED_FACE_DOWN_CARD:
			sm.addDealerCard(null);
			return true;
		case DEAL_STARTED:
			sm.setDisplayString("");
			sm.newDeal();
			return false;
		case DEALER_ENDED_TURN:
			sm.setDelay(150);
			return true;
		case TABLE_OPENED:
			sm.setDisplayString("Place your bets!");
			for (Spot s : spots) {
				s.clearCards();
			}
			sm.discardAllSprites();
			return false;
		case INSURANCE_OFFERED:
			insuranceMode = true;
			sm.setDisplayString("Insurance / even money?");
			return false;
		case INSURANCE_PAID:
			payoutInsurance();
		case INSURANCE_COLLECTED:
			collectInsurance();
		default:
			return false;
		}
	}

	private boolean processSpotEvent(SpriteManager sm, Event e, Spot spot) {
		switch (e.getType()) {
		case PLAYER_BUSTED:
			spot.addBust(e.getHandIndex());
			sm.discardHand(spot.getHand(e.getHandIndex()).getSprite());
			sm.updateHandPlacements(spot.getSprite());
			sm.setDelay(BUST_TIMER_BASE);
			return true;
		case SPOT_GAINED_CARD:
			spots.get(e.getSpotIndex()).addCard(e.getCard(), e.getHandIndex());
			sm.addMovingSprite(1);
			return true;
		case SPOT_SPLIT:
			spot.addSplit(e.getHandIndex());
			sm.updateHandPlacements(spot.getSprite());
			return true;
		case SPOT_STARTED_PLAY:
			spot.setInPlay(true);
			return false;
		case SPOT_FINISHED_PLAY:
			spot.setInPlay(false);
			return false;
		case SPOT_ONTO_NEXT_HAND:
			spot.ontoNextHand();
			return false;
		case WIN_BLACKJACK:
			if (rules.getPayAndCleanPlayerBlackjack() == PayAndCleanPlayerBlackjack.PLAY_START && !spot.tookEvenMoney()) {
				spot.clearCards();
				sm.discardSpot(spot.getSprite());
				return true;
			} else {
				return false;
			}
		case SPOT_DOUBLE:
			spot.addChips(e.getHandIndex(), 1, true);
			return false;
		default:
			if (e.getType().isPayout()) {
				if (spot.tookEvenMoney()) {
					return false;
				}
				spot.addChips(e.getHandIndex(), e.getType().getPayout(), false);
				return true;
			}
			return false;
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

	public TableRules getRules() {
		return rules;
	}

	public List<Spot> getSpots() {
		return spots;
	}

	public AnimationSettings getAnimationSettings() {
		return animationSettings;
	}

	public void addPlayer(int spotIndex, PlayerType pt) {
		Player p = null;
		Spot spot = spots.get(spotIndex);
		switch (pt) {
		case BASIC_BOT:
			p = new BasicBot(rules);
			break;
		case BASIC_COUNTING_BOT:
			p = new BasicCountingBot(rules);
			break;
		case BASIC_INDEX_COUNTING_BOT:
			p = new BasicIndexCountingBot(rules);
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

		if (animationSettings.isImmediatelyPayEvenMoney() && newPlay && s.isBlackjack()) {
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
