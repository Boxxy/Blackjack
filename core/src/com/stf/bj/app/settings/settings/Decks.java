package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class Decks extends Setting{

	public enum DecksSetting {
		ONE("1"), TWO("2"), FOUR("4"), SIX("6"), EIGHT("8"); 
		
		private final String displayName;

		private DecksSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public Decks(Preferences preferences) {
		super(preferences, "Number Of Decks (Not Working)", DecksSetting.SIX.ordinal());
		for(DecksSetting e : DecksSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
