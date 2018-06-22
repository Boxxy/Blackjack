package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class NumberOfSpots extends Setting{

	public enum NumberOfSpotsSetting {
		SIX("6"), FIVE("5"), FOUR("4"), THREE("3"), TWO("2"), ONE("1"); 
		
		private final String displayName;

		private NumberOfSpotsSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public NumberOfSpots(Preferences preferences) {
		super(preferences, "Number of Spots", NumberOfSpotsSetting.SIX.ordinal());
		for(NumberOfSpotsSetting e : NumberOfSpotsSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
