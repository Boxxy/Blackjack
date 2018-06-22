package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class BotSpeed extends Setting{

	public enum BotSpeedSetting {
		SLOWEST("Slowest"), SLOW("Slow"), MEDIUM("Medium"), FAST("Fast"), FASTEST("Fastest"), INSTANT("Instant");
		
		private final String displayName;

		private BotSpeedSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public BotSpeed(Preferences preferences) {
		super(preferences, "Bot Speed", BotSpeedSetting.MEDIUM.ordinal());
		for(BotSpeedSetting e : BotSpeedSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
