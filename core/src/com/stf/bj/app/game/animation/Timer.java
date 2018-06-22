package com.stf.bj.app.game.animation;

import com.stf.bj.app.game.server.EventType;
import com.stf.bj.app.settings.AppSettings;
import com.stf.bj.app.settings.settings.DealerSpeed.DealerSpeedSetting;

public class Timer {
	private int delay = 0;
	private int wagerInsuranceTimer = 0;
	private final int dealerDelay;

	public Timer(AppSettings settings) {
		dealerDelay = calculatedDealerDelay(settings.getDealerSpeed());
	}

	private int calculatedDealerDelay(DealerSpeedSetting dealerSpeedSetting) {
		switch (dealerSpeedSetting) {
		case SLOWEST:
			return 150;
		case SLOW:
			return 100;
		case MEDIUM:
			return 60;
		case FAST:
			return 40;
		case FASTEST:
			return 20;
		case INSTANT:
			return 0;
		default:
			throw new IllegalStateException();
		}
	}

	public int getDelayForEventType(EventType eventType) {
		switch (eventType) {
		case DEALER_BLACKJACK:
		case DEALER_BUSTED:
			return 0;
		case DEALER_GAINED_CARD:
		case DEALER_GAINED_FACE_DOWN_CARD:
			return dealerDelay * 2;
		case SPOT_GAINED_CARD:
			return dealerDelay;
		case DECK_SHUFFLED:
			return dealerDelay * 4;
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
			return dealerDelay;
		case PLAYER_BUSTED:
			return getBustDelay();
		case SPOT_DOUBLE:
		case SPOT_HIT:
		case SPOT_SPLIT:
		case SPOT_STAND:
		case SPOT_SURRENDER:
			return dealerDelay;
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
		if (wagerInsuranceTimer < dealerDelay)
			wagerInsuranceTimer = dealerDelay;
	}

	public boolean dealTimer() {
		wagerInsuranceTimer--;
		if (wagerInsuranceTimer < 0) {
			return true;
		}
		return false;
	}

	public void dealTimerSpeedUp() {
		wagerInsuranceTimer = dealerDelay;
	}

	public void insuranceTimerWageChanged() {
		if (wagerInsuranceTimer < dealerDelay)
			wagerInsuranceTimer = dealerDelay;
	}

	public boolean insuranceTimer() {
		wagerInsuranceTimer--;
		if (wagerInsuranceTimer < 0) {
			return true;
		}
		return false;
	}

	public void resetWagerTimer() {
		wagerInsuranceTimer = dealerDelay * 5;
	}

	public void resetInsuranceTimer() {
		wagerInsuranceTimer = dealerDelay * 5;
	}

	public int getWagerInsuranceTimer() {
		return wagerInsuranceTimer;
	}

	public int getBustDelay() {
		return dealerDelay * 2;
	}
	public int getDealerDelay() {
		//Maybe add some randomness here?
		return dealerDelay;
	}

}
