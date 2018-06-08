package com.stf.bj.app.bj;

import java.util.ArrayList;
import java.util.List;

import com.stf.bj.app.players.Play;
import com.stf.bj.app.players.PlayerManager;
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
	private final PlayerManager playerManager;
	int timer = 0;
	private static final int DEAL_TIMER_BASE = 150;
	private static final int DEAL_TIMER_RESET = 100;
	private static final int INSURANCE_TIMER_BASE = 500;
	private static final int INSURANCE_TIMER_RESET = 300;
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
		playerManager = new PlayerManager(spots, animationSettings);
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
			if (playerManager.updateInsurances(sm)) {
				insuranceTimerWageChanged();
			}
			if (insuranceTimer()) {
				insuranceMode = false;
				sm.setDisplayString("");
			}

		} else if (table.acceptingWagers()) {
			if (playerManager.updateWagers(table)) {
				dealTimerWageChanged();
			}
			if (table.canStartDeal()) {
				if (dealTimer() && playerManager.betsClean()) {
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
			playerManager.sendEvent(e);
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
		case DEALER_BLACKJACK:
			playerManager.payoutInsurance();
		case PLAY_STARTED:
			playerManager.collectInsurance();
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
			if (rules.getPayAndCleanPlayerBlackjack() == PayAndCleanPlayerBlackjack.PLAY_START) {
				sm.discardSpot(spot.getSprite());
				return true;
			} else {
				return false;
			}
		default:
			return false;
		}
	}

	public void input(int keyPressed) {
		playerManager.sendInput(keyPressed);
	}

	public TableRules getRules() {
		return rules;
	}

	public void addPlayer(int spot, PlayerType pt) {
		playerManager.addPlayer(spots.get(spot), pt, rules);
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public List<Spot> getSpots() {
		return spots;
	}

	public AnimationSettings getAnimationSettings() {
		return animationSettings;
	}

}
