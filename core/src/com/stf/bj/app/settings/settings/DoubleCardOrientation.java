package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class DoubleCardOrientation extends Setting{

	public enum DoubleCardOrientationSetting {
		SIDEWAYS("Sideways"), VERTICAL("Vertical (Normal)"); 
		
		private final String displayName;

		private DoubleCardOrientationSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public DoubleCardOrientation(Preferences preferences) {
		super(preferences, "Double Card Orientation", DoubleCardOrientationSetting.SIDEWAYS.ordinal());
		for(DoubleCardOrientationSetting e : DoubleCardOrientationSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
