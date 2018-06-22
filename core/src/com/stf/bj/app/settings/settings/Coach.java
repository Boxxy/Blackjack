package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class Coach extends Setting{

	public enum CoachSetting {
		ON_SCREEN("On Screen"), IN_LOG("In Log"); 
		
		private final String displayName;

		private CoachSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public Coach(Preferences preferences) {
		super(preferences, "Alert Misplays", CoachSetting.ON_SCREEN.ordinal());
		for(CoachSetting e : CoachSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
