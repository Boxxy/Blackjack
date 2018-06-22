package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class DealerSoft17 extends Setting{

	public enum DealerSoft17Setting {
		HIT("Hit"), STAY("Stay"); 
		
		private final String displayName;

		private DealerSoft17Setting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public DealerSoft17(Preferences preferences) {
		super(preferences, "Dealer Soft 17", DealerSoft17Setting.HIT.ordinal());
		for(DealerSoft17Setting e : DealerSoft17Setting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
