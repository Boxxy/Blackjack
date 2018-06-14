package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class HumanSpot extends Setting{

	public enum HumanSpotSetting {
		RANDOM("Random"), SIX("6 (Third Base)"), FIVE("5"), FOUR("4"), THREE("3"), TWO("2"), ONE("1 (First Base)"), NONE("Bots Only"); 
		
		private final String displayName;

		private HumanSpotSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public HumanSpot(Preferences preferences) {
		super(preferences, "Player spot", HumanSpotSetting.NONE.ordinal());
		for(HumanSpotSetting e : HumanSpotSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
