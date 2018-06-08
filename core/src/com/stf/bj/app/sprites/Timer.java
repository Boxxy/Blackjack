package com.stf.bj.app.sprites;

import com.stf.bj.app.table.EventType;

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
			return settings.getEventDelay();
		case DEALER_BUSTED:
			return settings.getEventDelay();
		case DEALER_GAINED_CARD:
			return settings.getEventDelay();
		case DEALER_GAINED_FACE_DOWN_CARD:
			return settings.getEventDelay();
		case DECK_SHUFFLED:
			return settings.getEventDelay();
		case INSURANCE_COLLECTED:
			return settings.getEventDelay();
		case INSURANCE_PAID:
			return settings.getEventDelay();
		case LOSE:
			return settings.getEventDelay();
		case LOSE_DOUBLE:
			return settings.getEventDelay();
		case LOSE_HALF:
			return settings.getEventDelay();
		case PLAYER_BUSTED:
			return settings.getEventDelay();
		case PUSH:
			return settings.getEventDelay();
		case SPOT_DOUBLE:
			return settings.getEventDelay();
		case SPOT_GAINED_CARD:
			return settings.getEventDelay();
		case SPOT_HIT:
			return settings.getEventDelay();
		case SPOT_SPLIT:
			return settings.getEventDelay();
		case SPOT_STAND:
			return settings.getEventDelay();
		case SPOT_SURRENDER:
			return settings.getEventDelay();
		case WIN:
			return settings.getEventDelay();
		case WIN_BLACKJACK:
			return settings.getEventDelay();
		case WIN_DOUBLE:
			return settings.getEventDelay();
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
		if (wagerInsuranceTimer > settings.getWagerTimerAdditional())
			wagerInsuranceTimer = settings.getWagerTimerAdditional();
	}

	public boolean dealTimer() {
		wagerInsuranceTimer++;
		if (wagerInsuranceTimer > settings.getWagerTimerBase()) {
			wagerInsuranceTimer = 0;
			return true;
		}
		return false;
	}

	public void insuranceTimerWageChanged() {
		if (wagerInsuranceTimer > settings.getInsuranceTimerAdditional())
			wagerInsuranceTimer = settings.getInsuranceTimerAdditional();
	}

	public boolean insuranceTimer() {
		wagerInsuranceTimer++;
		if (wagerInsuranceTimer > settings.getInsuranceTimerBase()) {
			wagerInsuranceTimer = 0;
			return true;
		}
		return false;
	}
	
}
