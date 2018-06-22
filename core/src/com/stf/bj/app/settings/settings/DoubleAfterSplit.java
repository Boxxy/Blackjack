package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class DoubleAfterSplit extends Setting{

	public enum DoubleAfterSplitSetting {
		ALLOWED("Allowed"), NONE("None"); 
		
		private final String displayName;

		private DoubleAfterSplitSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public DoubleAfterSplit(Preferences preferences) {
		super(preferences, "Double After Split", DoubleAfterSplitSetting.NONE.ordinal());
		for(DoubleAfterSplitSetting e : DoubleAfterSplitSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
