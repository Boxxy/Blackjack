package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class Surrender extends Setting{

	public enum SurrenderSetting {
		LATE("Late Surrender"), NONE("None"); 
		
		private final String displayName;

		private SurrenderSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public Surrender(Preferences preferences) {
		super(preferences, "Surrender", SurrenderSetting.NONE.ordinal());
		for(SurrenderSetting e : SurrenderSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
