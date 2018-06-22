package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class TableMin extends Setting{

	public enum TableMinSetting {
		FIVE("5"), TEN("10"), TWENTYFIVE("25");
		
		private final String displayName;

		private TableMinSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public TableMin(Preferences preferences) {
		super(preferences, "Table Min", TableMinSetting.FIVE.ordinal());
		for(TableMinSetting e : TableMinSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
