package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;
import com.stf.bj.app.settings.settings.Decks.DecksSetting;
import com.stf.bj.app.settings.settings.HumanSpot.HumanSpotSetting;

public class NewAppSettings {
	private final Decks decks;
	private final HumanSpot humanSpot;

	public NewAppSettings(Preferences prefs) {
		decks = new Decks(prefs);
		humanSpot = new HumanSpot(prefs);
	}
	
	public HumanSpotSetting getHumanSpotValue() {
		return HumanSpotSetting.values()[humanSpot.getValue()];
	}
	
	public Setting getHumanSpotSetting() {
		return humanSpot;
	}
	
	public int getDecksValue() {
		switch(DecksSetting.values()[decks.getValue()]) {
		case EIGHT:
			return 8;
		case FOUR:
			return 4;
		case ONE:
			return 1;
		case SIX:
			return 6;
		case TWO:
			return 2;
		default:
			throw new IllegalStateException();
		}
	}
	
	public Setting getDeckSetting() {
		return decks;
	}

	public void updateAndSave() {
		decks.updateAndSave();
		humanSpot.updateAndSave();
	}
}
