package com.stf.bj.app.settings.settings;

import com.badlogic.gdx.Preferences;

public class EvenMoneyPayment extends Setting{

	public enum EvenMoneyPaymentSetting {
		IMMEDIATELY("Immediately"), AFTER_REVEAL("After Dealer Check"); 
		
		private final String displayName;

		private EvenMoneyPaymentSetting(String displayName) {
			this.displayName = displayName;
		}
	}
	
	public EvenMoneyPayment(Preferences preferences) {
		super(preferences, "Pay Even Money", EvenMoneyPaymentSetting.IMMEDIATELY.ordinal());
		for(EvenMoneyPaymentSetting e : EvenMoneyPaymentSetting.values()) {
			addOption(e.displayName, e.ordinal());
		}
	}

}
