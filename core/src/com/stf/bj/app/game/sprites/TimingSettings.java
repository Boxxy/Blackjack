package com.stf.bj.app.game.sprites;

import java.util.Random;

public class TimingSettings {

	private final float cardSpeed;

	private final int wagerTimerBase;
	private final int wagerTimerAdditional;
	private final int insuranceTimerBase;
	private final int insuranceTimerAdditional;

	private final int shuffleDelay;
	private final int newPlayerCardDelay;
	private final int newDealerCardDelay;
	private final int playDelay;
	private final int payOutDelay;
	private final int bustDelay;

	private final int baseBotDelay;

	public TimingSettings(float cardSpeed, int wagerTimerBase, int wagerTimerAdditional, int insuranceTimerBase,
			int insuranceTimerAdditional, int shuffleDelay, int newPlayerCardDelay, int newDealerCardDelay,
			int playDelay, int payOutDelay, int bustDelay, int baseBotDelay) {
		this.cardSpeed = cardSpeed;
		this.wagerTimerBase = wagerTimerBase;
		this.wagerTimerAdditional = wagerTimerAdditional;
		this.insuranceTimerBase = insuranceTimerBase;
		this.insuranceTimerAdditional = insuranceTimerAdditional;
		this.shuffleDelay = shuffleDelay;
		this.newPlayerCardDelay = newPlayerCardDelay;
		this.newDealerCardDelay = newDealerCardDelay;
		this.playDelay = playDelay;
		this.payOutDelay = payOutDelay;
		this.bustDelay = bustDelay;
		this.baseBotDelay = baseBotDelay;
	}

	public float getCardSpeed() {
		return cardSpeed;
	}

	public static TimingSettings getSlow() {
		return new TimingSettings(12f, 600, 150, 300, 100, 300, 60, 120, 50, 50, 150, 100);
	}

	public static TimingSettings getFast() {
		return new TimingSettings(18f, 350, 80, 200, 50, 200, 30, 40, 20, 20, 100, 50);
	}
	
	public static TimingSettings getPratice() {
		return new TimingSettings(18f, 350, 80, 200, 50, 200, 50, 70, 20, 20, 150, 50);
	}

	public static TimingSettings getInstant() {
		return new TimingSettings(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
	}

	public static TimingSettings getInstantPlayer() {
		return new TimingSettings(0, 200, 40, 150, 50, 0, 0, 0, 0, 0, 0, 0);
	}

	public static TimingSettings getRandom(Random r) {
		if (r == null) {
			r = new Random(System.currentTimeMillis());
		}
		int i = r.nextInt(4);
		if (i == 0) {
			return getSlow();
		} else if (i == 1) {
			return getFast();
		} else if (i == 2) {
			return getInstant();
		}else if (i == 3) {
			return getInstantPlayer();
		}

		return getFast();
	}

	public int getWagerTimerBase() {
		return wagerTimerBase;
	}

	public int getWagerTimerAdditional() {
		return wagerTimerAdditional;
	}

	public int getInsuranceTimerBase() {
		return insuranceTimerBase;
	}

	public int getInsuranceTimerAdditional() {
		return insuranceTimerAdditional;
	}

	public int getShuffleDelay() {
		return shuffleDelay;
	}

	public int getPlayerCardDelay() {
		return newPlayerCardDelay;
	}

	public int getDealerCardDelay() {
		return newDealerCardDelay;
	}

	public int getPlayDelay() {
		return playDelay;
	}

	public int getPayOutDelay() {
		return payOutDelay;
	}

	public int getBustDelay() {
		return bustDelay;
	}

	public int getBaseBotDelay() {
		return baseBotDelay;
	}

}
