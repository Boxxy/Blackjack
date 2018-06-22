package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class BotPlayers extends Setting{

	public enum BotPlayersSetting {
		REALISTIC("Realistic"), BASIC("Basic Strategy"), COUNTING("Counter"), INDEX("Counter with Index Play"), NONE("None"); 
		
		private final String displayName;

		private BotPlayersSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public BotPlayers(Preferences preferences) {
		super(preferences, "Bot Players", BotPlayersSetting.REALISTIC.ordinal());
		for(BotPlayersSetting e : BotPlayersSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
