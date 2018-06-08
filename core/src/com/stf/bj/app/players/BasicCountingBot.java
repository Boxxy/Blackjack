package com.stf.bj.app.players;

import com.stf.bj.app.AppSettings;
import com.stf.bj.app.table.Event;
import com.stf.bj.app.table.EventType;
import com.stf.bj.app.table.TableRules;

public class BasicCountingBot extends BasicBot {

	int count;
	int penetration;
	int totalCards;

	public BasicCountingBot(AppSettings settings) {
		super(settings);
		totalCards = settings.getTableRules().getDecks() * 52;
	}

	@Override
	public void sendEvent(Event e) {
		super.sendEvent(e);
		if (e.getType() == EventType.DEALER_GAINED_CARD || e.getType() == EventType.SPOT_GAINED_CARD) {
			updateCount(e.getCard().getValue());
		} else if (e.getType() == EventType.DECK_SHUFFLED) {
			shuffle();
		}
	}

	private void updateCount(int newCardValue) {
		if (newCardValue == 10 || newCardValue == 1) {
			count += -2;
		} else if (newCardValue == 7 || newCardValue == 2) {
			count += 1;
		} else if (newCardValue == 3 || newCardValue == 4 || newCardValue == 5 || newCardValue == 6) {
			count += 2;
		}
		penetration++;
	}

	private void shuffle() {
		penetration = 0;
		count = 0;
	}

	@Override
	public double getWager() {
		if (delay > 0) {
			delay--;
			return -1;
		}

		double trueCount = getTrueCount();
		if (trueCount < 2) {
			return 5;
		} else if (trueCount < 3) {
			return 10;
		} else if (trueCount < 4) {
			return 15;
		} else if (trueCount < 7) {
			return 25;
		} else {
			return 50;
		}
	}

	protected double getTrueCount() {
		return count * 52.0 / (totalCards - penetration);
	}
	
	@Override
	public boolean getInsurancePlay() {
		return getTrueCount() >= 6;
	}

}
