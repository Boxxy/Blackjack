package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class NumberOfSplits extends Setting{

	public enum NumberOfSplitsSetting {
		ONE("1"), TWO("2"), THREE("3"); 
		
		private final String displayName;

		private NumberOfSplitsSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public NumberOfSplits(Preferences preferences) {
		super(preferences, "Number of Splits", NumberOfSplitsSetting.THREE.ordinal());
		for(NumberOfSplitsSetting e : NumberOfSplitsSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
