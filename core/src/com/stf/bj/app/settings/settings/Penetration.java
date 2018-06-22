package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class Penetration extends Setting{

	public enum PenetrationSetting {
		HALF("1/2 Deck"), THREE_QUARTERS("3/4 Deck"), ONE("1 Deck"), DECK_AND_A_HALF("Deck and a Half"), TWO_DECKS("2 Decks"); 
		
		private final String displayName;

		private PenetrationSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public Penetration(Preferences preferences) {
		super(preferences, "Cards Remaining Before Shuffle", PenetrationSetting.HALF.ordinal());
		for(PenetrationSetting e : PenetrationSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
