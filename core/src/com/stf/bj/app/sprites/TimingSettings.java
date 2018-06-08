package com.stf.bj.app.sprites;

import java.util.Random;

public class TimingSettings {

	private final float cardSpeed;
	private final int eventDelay;

	private final int wagerTimerBase;
	private final int wagerTimerAdditional;
	private final int insuranceTimerBase;
	private final int insuranceTimerAdditional;

	public TimingSettings(float cardSpeed, int eventDelay, int wagerTimerBase, int wagerTimerAdditional,
			int insuranceTimerBase, int insuranceTimerAdditional) {
		this.cardSpeed = cardSpeed;
		this.eventDelay = eventDelay;
		this.wagerTimerBase = wagerTimerBase;
		this.wagerTimerAdditional = wagerTimerAdditional;
		this.insuranceTimerBase = insuranceTimerBase;
		this.insuranceTimerAdditional = insuranceTimerAdditional;
	}

	public float getCardSpeed() {
		return cardSpeed;
	}

	public static TimingSettings getClassic() {
		return new TimingSettings(12f, 50, 200, 100, 300, 100);
	}

	public static TimingSettings getNew() {
		return new TimingSettings(18f, 10, 150, 50, 200, 50);
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

	public int getEventDelay() {
		return eventDelay;
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

}
