package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class ResplitAces extends Setting{

	public enum ResplitAcesSetting {
		ALLOWED("Allowed"), NONE("No Resplit"); 
		
		private final String displayName;

		private ResplitAcesSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public ResplitAces(Preferences preferences) {
		super(preferences, "Resplit Aces", ResplitAcesSetting.ALLOWED.ordinal());
		for(ResplitAcesSetting e : ResplitAcesSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
