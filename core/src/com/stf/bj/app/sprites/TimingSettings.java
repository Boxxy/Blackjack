package com.stf.bj.app.sprites;

import java.util.Random;

public class TimingSettings {

	private final float cardSpeed;
	
	public TimingSettings(float cardSpeed) {
		this.cardSpeed = cardSpeed;
	}
	
	public float getCardSpeed() {
		return cardSpeed;
	}

	public static TimingSettings getClassic() {
		return new TimingSettings(12f);
	}
	public static TimingSettings getNew() {
		return new TimingSettings(18f);
	}
	
	public static TimingSettings getRandom() {
		Random r = new Random(System.currentTimeMillis());
		int i = r.nextInt(2);
		if(i == 0) {
			return getClassic();
		}else if (i == 1) {
			return getNew();
		}
		
		return getNew();
	}
	
	
}
