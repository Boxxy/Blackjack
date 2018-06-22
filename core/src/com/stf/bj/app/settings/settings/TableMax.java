package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class TableMax extends Setting{

	public enum TableMaxSetting {
		TWENTYFIVE("25"), ONE_HUNDRED("100"), TWO_HUNDRED("200"), ONE_THOUSAND("1000");
		
		private final String displayName;

		private TableMaxSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public TableMax(Preferences preferences) {
		super(preferences, "Table Max", TableMaxSetting.TWO_HUNDRED.ordinal());
		for(TableMaxSetting e : TableMaxSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
