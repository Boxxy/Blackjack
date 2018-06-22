package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class PayoutBlackjack extends Setting{

	public enum PayoutBlackjackSetting {
		BEFORE_PLAY("After Deal"), AFTER_PLAY("After Play"); 
		
		private final String displayName;

		private PayoutBlackjackSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public PayoutBlackjack(Preferences preferences) {
		super(preferences, "Blackjack Payout", PayoutBlackjackSetting.BEFORE_PLAY.ordinal());
		for(PayoutBlackjackSetting e : PayoutBlackjackSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
