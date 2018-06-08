package com.stf.bj.app.players;

import java.util.Random;

import com.stf.bj.app.AppSettings;
import com.stf.bj.app.players.strategy.BaseHoChunkStrategy;
import com.stf.bj.app.players.strategy.Strategy;
import com.stf.bj.app.table.Event;
import com.stf.bj.app.table.EventType;

public class BasicBot implements Player {

	private Strategy strategy;
	private int handTotals[];
	private boolean handAces[];
	private int dealerUpCardValue = -1;
	private int mySpotIndex = -1;
	private int timesSplit = 0;
	private int splits;
	protected int delay = 0;
	protected final int baseDelay;
	private final Random r;
	private double wager = -1.0;

	public BasicBot(AppSettings settings) {
		this.splits = settings.getTableRules().getSplits();
		handTotals = new int[splits + 1];
		handAces = new boolean[splits + 1];
		r = new Random();
		setStrategy(new BaseHoChunkStrategy());
		int delayFromSettings = settings.getTimingSettings().getBaseBotDelay();
		if (delayFromSettings > 0) {
			baseDelay = delayFromSettings / 2 + r.nextInt(delayFromSettings);
		} else {
			baseDelay = 0;
		}
		setWager();
		resetDelay();
	}

	private void setWager() {
		if (wager == -1.0) {
			int rInt = r.nextInt(12);
			if (rInt < 6) {
				wager = 5.0 * (1 + rInt);
			} else if (rInt < 9) {
				wager = 10.0;
			} else {
				wager = 5.0;
			}
		} else {
			int rInt = r.nextInt(20);
			if (rInt == 0) {
				wager += 5.0;
			} else if (rInt == 1 && wager > 6.0) {
				wager -= 5.0;
			}
		}
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	protected void reset() {
		for (int i = 0; i < splits; i++) {
			handTotals[i] = 0;
			handAces[i] = false;
		}
		dealerUpCardValue = -12;
		timesSplit = 0;

		setWager();

	}

	@Override
	public Play getMove(int handIndex, boolean canDouble, boolean canSplit, boolean canSurrender) {
		if (delay > 0) {
			delay--;
			return null;
		}
		Play play;
		boolean isSoft = !canSplit && getHandSoft(handIndex);
		int total = getHandTotal(handIndex, isSoft);
		play = strategy.getPlay(total, dealerUpCardValue, isSoft, canDouble, canSplit, canSurrender);
		return play;
	}

	private void resetDelay() {
		if (baseDelay > 0) {
			delay = baseDelay / 2 + r.nextInt(baseDelay);
		} else {
			delay = 0;
		}
	}

	protected int getHandTotal(int handIndex, boolean isSoft) {
		int total = handTotals[handIndex];
		if (isSoft) {
			total += 10;
		}
		return total;
	}

	protected boolean getHandSoft(int handIndex) {
		int total = handTotals[handIndex];
		return (handAces[handIndex] && total < 12);
	}

	@Override
	public boolean getInsurancePlay() {
		return false;
	}

	@Override
	public void setSpot(int spotIndex) {
		mySpotIndex = spotIndex;
	}

	@Override
	public void sendEvent(Event e) {
		if (mySpotIndex < 0)
			return;
		if (e.hasSpot() && e.getSpotIndex() != mySpotIndex)
			return;
		if (e.getType() == EventType.TABLE_OPENED) {
			resetDelay();
		} else if (e.getType() == EventType.DEAL_STARTED) {
			reset();
		} else if (e.getType() == EventType.DEALER_GAINED_CARD) {
			if (dealerUpCardValue < 1) {
				dealerUpCardValue = e.getCard().getValue();
			}
		} else if (e.getType() == EventType.SPOT_GAINED_CARD) {
			addCardToHand(e.getCard().getValue(), e.getHandIndex());
			resetDelay();
		} else if (e.getType() == EventType.SPOT_SPLIT) {
			addSplit(e.getHandIndex());
		}
	}

	private void addCardToHand(int value, int handIndex) {
		handTotals[handIndex] += value;
		if (value == 1) {
			handAces[handIndex] = true;
		}
	}

	private void addSplit(int handIndex) {
		int nextHandIndex = ++timesSplit;
		handTotals[handIndex] = handTotals[handIndex] / 2;
		handTotals[nextHandIndex] = handTotals[handIndex];
		handAces[nextHandIndex] = handAces[handIndex];
	}

	@Override
	public double getWager() {
		if (delay > 0) {
			delay--;
			return -1;
		}
		return wager;
	}

}
