package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class CardSpeed extends Setting{

	public enum CardSpeedSetting {
		SLOWEST("Slowest"), SLOW("Slow"), MEDIUM("Medium"), FAST("Fast"), FASTEST("Fastest"), INSTANT("Instant");
		
		private final String displayName;

		private CardSpeedSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public CardSpeed(Preferences preferences) {
		super(preferences, "Card Speed", CardSpeedSetting.MEDIUM.ordinal());
		for(CardSpeedSetting e : CardSpeedSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
