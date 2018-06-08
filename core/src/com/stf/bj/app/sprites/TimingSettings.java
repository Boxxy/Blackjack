package com.stf.bj.app.sprites;

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

	public TimingSettings(float cardSpeed, int wagerTimerBase, int wagerTimerAdditional,
			int insuranceTimerBase, int insuranceTimerAdditional, int shuffleDelay, int newPlayerCardDelay, int newDealerCardDelay, int playDelay, int payOutDelay, int bustDelay, int baseBotDelay) {
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

	public static TimingSettings getClassic() {
		return new TimingSettings(12f, 200, 100, 300, 100, 500, 80, 120, 50, 50, 150, 100);
	}

	public static TimingSettings getNew() {
		return new TimingSettings(18f, 150, 50, 200, 50, 300, 30, 40, 20, 40, 100, 50);
	}

	public static TimingSettings getRandom() {
		Random r = new Random(System.currentTimeMillis());
		int i = r.nextInt(2);
		if (i == 0) {
			return getClassic();
		} else if (i == 1) {
			return getNew();
		}

		return getNew();
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
