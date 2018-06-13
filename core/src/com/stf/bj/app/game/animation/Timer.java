package com.stf.bj.app.game.animation;

import com.stf.bj.app.game.server.EventType;
import com.stf.bj.app.settings.TimingSettings;

public class Timer {
	private final TimingSettings settings;
	private int delay = 0;
	private int wagerInsuranceTimer = 0;
	
	public Timer(TimingSettings settings) {
		this.settings = settings;
	}
	
	public int getDelayForEventType(EventType eventType){
		switch(eventType) {
		case DEALER_BLACKJACK:
		case DEALER_BUSTED:
			return 0;
		case DEALER_GAINED_CARD:
		case DEALER_GAINED_FACE_DOWN_CARD:
			return settings.getDealerCardDelay();
		case SPOT_GAINED_CARD:
			return settings.getPlayerCardDelay();
		case DECK_SHUFFLED:
			return settings.getShuffleDelay();
		case INSURANCE_COLLECTED:
		case INSURANCE_PAID:
			return 0;
		case LOSE:
		case LOSE_DOUBLE:
		case LOSE_HALF:
		case PUSH:
		case WIN:
		case WIN_BLACKJACK:
		case WIN_DOUBLE:
			return settings.getPayOutDelay();
		case PLAYER_BUSTED:
			return settings.getBustDelay();
		case SPOT_DOUBLE:
		case SPOT_HIT:
		case SPOT_SPLIT:
		case SPOT_STAND:
		case SPOT_SURRENDER:
			return settings.getPlayDelay();
		default:
			return 0;
		}
		
	}
	
	public boolean hasDelay() {
		return delay > 0;
	}
	
	public void tickDelay() {
		delay--;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void setDelayForEventType(EventType eventType) {
		setDelay(getDelayForEventType(eventType));
	}

	public void dealTimerWageChanged() {
		if (wagerInsuranceTimer < settings.getWagerTimerAdditional())
			wagerInsuranceTimer = settings.getWagerTimerAdditional();
	}

	public boolean dealTimer() {
		wagerInsuranceTimer--;
		if (wagerInsuranceTimer < 0) {
			return true;
		}
		return false;
	}

	public void dealTimerSpeedUp() {
		wagerInsuranceTimer = settings.getWagerTimerAdditional();
	}

	public void insuranceTimerWageChanged() {
		if (wagerInsuranceTimer < settings.getInsuranceTimerAdditional())
			wagerInsuranceTimer = settings.getInsuranceTimerAdditional();
	}

	public boolean insuranceTimer() {
		wagerInsuranceTimer--;
		if (wagerInsuranceTimer < 0) {
			return true;
		}
		return false;
	}

	public void resetWagerTimer() {
		wagerInsuranceTimer = settings.getWagerTimerBase();
	}
	public void resetInsuranceTimer() {
		wagerInsuranceTimer = settings.getInsuranceTimerBase();
	}
	
	public int getWagerInsuranceTimer() {
		return wagerInsuranceTimer;
	}
	
}
