package com.stf.bj.app.game.players;

import java.util.Random;

import com.stf.bj.app.game.bj.Spot;
import com.stf.bj.app.game.players.strategy.BaseHoChunkStrategy;
import com.stf.bj.app.game.players.strategy.Strategy;
import com.stf.bj.app.game.server.Event;
import com.stf.bj.app.game.server.EventType;
import com.stf.bj.app.settings.AppSettings;

public class BasicBot implements Player {

	private Strategy strategy;
	private int dealerUpCardValue = -1;
	private int mySpotIndex = -1;
	protected int delay = 0;
	protected final int baseDelay;
	protected final Random r;
	private final Spot spot;
	

	public BasicBot(AppSettings settings, Random r, Spot spot) {
		this.spot = spot;
		if(r == null) {
			r = new Random(System.currentTimeMillis());
		}
		this.r = r;
		setStrategy(new BaseHoChunkStrategy());
		int delayFromSettings;
		switch(settings.getBotSpeed()) {
		case FAST:
			delayFromSettings = 50;
			break;
		case FASTEST:
			delayFromSettings = 25;
			break;
		case INSTANT:
			delayFromSettings = 0;
			break;
		case MEDIUM:
			delayFromSettings = 75;
			break;
		case SLOW:
			delayFromSettings = 110;
			break;
		case SLOWEST:
			delayFromSettings = 180;
			break;
		default:
			throw new IllegalStateException();
		}
		
		if (delayFromSettings > 0) {
			baseDelay = delayFromSettings / 2 + r.nextInt(delayFromSettings);
		} else {
			baseDelay = 0;
		}
		resetDelay();
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	protected void reset() {
		dealerUpCardValue = -122;
	}

	@Override
	public Play getMove(int handIndex, boolean canDouble, boolean canSplit, boolean canSurrender) {
		if (delay > 0) {
			delay--;
			return null;
		}
		Play play;
		boolean isSoft = isHandSoft(handIndex);
		int total = getHandSoftTotal(handIndex);
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

	protected int getHandSoftTotal(int handIndex) {
		return spot.getHand(handIndex).getHand().getSoftTotal();
	}
	protected int getHandHardTotal(int handIndex) {
		return spot.getHand(handIndex).getHand().getHardTotal();
	}

	protected boolean isHandSoft(int handIndex) {
		return spot.getHand(handIndex).getHand().isSoft();
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
			resetDelay();
		}
	}

	@Override
	public double getWager() {
		if (delay > 0) {
			delay--;
			return -1;
		}
		return 5;
	}

}
