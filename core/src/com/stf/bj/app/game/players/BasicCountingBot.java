package com.stf.bj.app.game.players;

import java.util.Random;

import com.stf.bj.app.game.bj.Spot;
import com.stf.bj.app.game.server.Event;
import com.stf.bj.app.game.server.EventType;
import com.stf.bj.app.settings.AppSettings;
import com.stf.bj.app.strategy.FullStrategy;

public class BasicCountingBot extends BasicBot {

	int count;
	int penetration;
	int totalCards;

	public BasicCountingBot(AppSettings settings, FullStrategy strategy, Random r, Spot s) {
		super(settings, strategy, r, s);
		totalCards = settings.getDecks() * 52;
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
		return strategy.getWager((int) getTrueCount());
	}

	protected double getTrueCount() {
		return count * 52.0 / (totalCards - penetration);
	}
	
	@Override
	public boolean getInsurancePlay() {
		return strategy.getInsurance((int) getTrueCount());
	}

}
