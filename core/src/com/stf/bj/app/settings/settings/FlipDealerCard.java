package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class FlipDealerCard extends Setting{

	public enum FlipDealerCardSetting {
		IMMEDIATELY("Immediately"), AFTER_SECOND_CARD("After Both Cards Dealt"); 
		
		private final String displayName;

		private FlipDealerCardSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public FlipDealerCard(Preferences preferences) {
		super(preferences, "Reveal the Dealer Card", FlipDealerCardSetting.AFTER_SECOND_CARD.ordinal());
		for(FlipDealerCardSetting e : FlipDealerCardSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
