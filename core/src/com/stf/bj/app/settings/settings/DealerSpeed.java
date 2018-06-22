package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class DealerSpeed extends Setting{

	public enum DealerSpeedSetting {
		SLOWEST("Slowest"), SLOW("Slow"), MEDIUM("Medium"), FAST("Fast"), FASTEST("Fastest"), INSTANT("Instant");
		
		private final String displayName;

		private DealerSpeedSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public DealerSpeed(Preferences preferences) {
		super(preferences, "Dealer Speed", DealerSpeedSetting.MEDIUM.ordinal());
		for(DealerSpeedSetting e : DealerSpeedSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
