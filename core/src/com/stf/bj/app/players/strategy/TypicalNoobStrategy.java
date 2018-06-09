package com.stf.bj.app.players.strategy;

import com.stf.bj.app.players.Play;

public class TypicalNoobStrategy implements Strategy {

	
	//HIT = 1, STAY=2, DOUBLEHIT=3, DOUBLESTAY=4, SPLIT=5, SURRENDERHIT=6, SURRENDERSTAY=7, SURRENDERSPLIT=8
	private int hardTotals[][] = {
		 //  A  2  3  4  5  6  7  8  9  T
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, //Player 3
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 3, 1, 1, 1, 1},
			{1, 3, 3, 3, 3, 3, 1, 1, 1, 1}, //Player 9
			{1, 3, 3, 3, 3, 3, 3, 3, 3, 1},
			{1, 3, 3, 3, 3, 3, 3, 3, 3, 1},
			{1, 2, 2, 2, 2, 2, 1, 1, 1, 1}, //Player 12
			{1, 1, 1, 2, 2, 2, 1, 1, 1, 1},
			{1, 2, 2, 2, 2, 2, 1, 1, 1, 1},
			{1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, //Player 16
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2}  //Player 21
	};
	
	private int softTotals[][] = {
	     //  A  2  3  4  5  6  7  8  9  T
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, //Player 12
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, //Player 14 (A+3)
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, //Player 16 (A+5)
			{1, 2, 2, 2, 2, 2, 2, 1, 1, 1}, //Player 17 (A+6)
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, //Player 19 (A+8)
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2}  //Player 21
	};
	
	private int pairs[][] = {
	     //  A  2  3  4  5  6  7  8  9  T
			{5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, //Player AA
			{1, 1, 5, 5, 5, 5, 1, 1, 1, 1},
			{1, 1, 5, 5, 5, 5, 1, 1, 1, 1}, //Player 33
			{1, 1, 5, 5, 5, 5, 1, 1, 1, 1},
			{1, 3, 3, 3, 3, 3, 3, 3, 3, 1}, //Player 55
			{1, 2, 2, 2, 5, 5, 1, 1, 1, 1}, //Player 66
			{1, 5, 5, 5, 5, 5, 1, 1, 1, 1},
			{5, 5, 5, 5, 5, 5, 5, 5, 5, 5}, //Player 88
			{2, 5, 5, 5, 5, 5, 5, 5, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2, 2, 2}  //Player TT
	};

	@Override
	public Play getPlay(int handTotal, int dealerUpCardValue, boolean handIsSoft, boolean canDouble, boolean canSplit,
			boolean canSurrender) {
		int tableValue;
		if (canSplit) {
			tableValue = getSplitPlay(handTotal, dealerUpCardValue);
		} else if (handIsSoft) {
			tableValue = getSoftPlay(handTotal, dealerUpCardValue);
		} else {
			tableValue = getHardPlay(handTotal, dealerUpCardValue);
		}
		return tableValueToPlay(tableValue, canDouble, canSurrender);
	}

	// HIT = 1, STAY=2, DOUBLEHIT=3, DOUBLESTAY=4, SPLIT=5, SURRENDERHIT=6, SURRENDERSTAY=7, SURRENDERSPLIT=8
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
