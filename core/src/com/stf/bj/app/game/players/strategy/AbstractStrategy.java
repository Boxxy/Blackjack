package com.stf.bj.app.game.players.strategy;

import com.stf.bj.app.game.players.Play;

public abstract class AbstractStrategy implements Strategy {

	protected int hardTotals[][];
	protected int softTotals[][];
	protected int pairs[][];

	// HIT = 1, STAY=2, DOUBLEHIT=3, DOUBLESTAY=4, SPLIT=5, SURRENDERHIT=6,
	// SURRENDERSTAY=7, SURRENDERSPLIT=8
	@Override
	public Play getPlay(int handTotal, int dealerUpCardValue, boolean handIsSoft, boolean canDouble, boolean canSplit,
			boolean canSurrender) {
		int tableValue;
		if (canSplit) {
			if (handIsSoft) {
				handTotal -= 10;
			}
			tableValue = getSplitPlay(handTotal, dealerUpCardValue);
		} else if (handIsSoft) {
			tableValue = getSoftPlay(handTotal, dealerUpCardValue);
		} else {
			tableValue = getHardPlay(handTotal, dealerUpCardValue);
		}
		return tableValueToPlay(tableValue, canDouble, canSurrender);
	}

	// HIT = 1, STAY=2, DOUBLEHIT=3, DOUBLESTAY=4, SPLIT=5, SURRENDERHIT=6,
	// SURRENDERSTAY=7, SURRENDERSPLIT=8
	private Play tableValueToPlay(int tableValue, boolean canDouble, boolean canSurrender) {
		if (tableValue == 1) {
			return Play.HIT;
		} else if (tableValue == 2) {
			return Play.STAND;
		} else if (tableValue == 3) {
			if (canDouble) {
				return Play.DOUBLEDOWN;
			} else {
				return Play.HIT;
			}
		} else if (tableValue == 4) {
			if (canDouble) {
				return Play.DOUBLEDOWN;
			} else {
				return Play.STAND;
			}
		} else if (tableValue == 5) {
			return Play.SPLIT;
		} else if (tableValue == 6) {
			if (canSurrender) {
				return Play.SURRENDER;
			} else {
				return Play.HIT;
			}
		} else if (tableValue == 7) {
			if (canSurrender) {
				return Play.SURRENDER;
			} else {
				return Play.STAND;
			}
		} else if (tableValue == 8) {
			if (canSurrender) {
				return Play.SURRENDER;
			} else {
				return Play.SPLIT;
			}
		}
		throw new IllegalStateException();
	}

	private int getHardPlay(int total, int dealerCardValue) {
		return hardTotals[total - 3][dealerCardValue - 1];
	}

	private int getSoftPlay(int total, int dealerCardValue) {
		return softTotals[total - 12][dealerCardValue - 1];
	}

	private int getSplitPlay(int total, int dealerCardValue) {
		return pairs[total / 2 - 1][dealerCardValue - 1];
	}

}
