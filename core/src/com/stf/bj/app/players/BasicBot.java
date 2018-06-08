package com.stf.bj.app.players;

import java.util.Random;

import com.stf.bj.app.AppSettings;
import com.stf.bj.app.players.strategy.BaseHoChunkStrategy;
import com.stf.bj.app.players.strategy.Strategy;
import com.stf.bj.app.table.Event;
import com.stf.bj.app.table.EventType;
import com.stf.bj.app.table.TableRules;

public class BasicBot implements Player {

	Strategy strategy;
	int handTotals[];
	boolean handAces[];
	int dealerUpCardValue = -1;
	int mySpotIndex = -1;
	int timesSplit = 0;
	int splits;
	int delay = 0;
	protected final int maxDelay;

	public BasicBot(AppSettings settings){
		this.splits = settings.getTableRules().getSplits();
		handTotals = new int[splits + 1];
		handAces = new boolean[splits + 1];
		Random r = new Random();
		setStrategy(new BaseHoChunkStrategy());
		int delayFromSettings = settings.getTimingSettings().getBaseBotDelay();
		//maxDelay = delayFromSettings/2 + r.nextInt(delayFromSettings * 2);
		maxDelay = delayFromSettings;
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
	}


	@Override
	public Play getMove(int handIndex, boolean canDouble, boolean canSplit, boolean canSurrender) {
		if (delay < maxDelay) {
			delay++;
			return null;
		} else {
			delay = 0;
		}
		Play play;
		boolean isSoft = !canSplit && getHandSoft(handIndex);
		int total = getHandTotal(handIndex, isSoft);
		play = strategy.getPlay(total, dealerUpCardValue,
				isSoft, canDouble, canSplit, canSurrender);
		return play;
	}

	protected int getHandTotal(int handIndex, boolean isSoft) {
		int total = handTotals[handIndex];
		if (isSoft) {
			total+= 10;
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
		if (e.getType() == EventType.DEAL_STARTED) {
			reset();
		} else if (e.getType() == EventType.DEALER_GAINED_CARD) {
			if (dealerUpCardValue < 1) {
				dealerUpCardValue = e.getCard().getValue();
			}
		} else if (e.getType() == EventType.SPOT_GAINED_CARD) {
			addCardToHand(e.getCard().getValue(), e.getHandIndex());
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
		handTotals[handIndex] = handTotals[handIndex]/2;
		handTotals[nextHandIndex] = handTotals[handIndex];
		handAces[nextHandIndex] = handAces[handIndex];
	}

	@Override
	public double getWager() {
		if (delay < maxDelay) {
			delay++;
			return -1;
		} else {
			delay = 0;
		}
		return 5.0;
	}

}
