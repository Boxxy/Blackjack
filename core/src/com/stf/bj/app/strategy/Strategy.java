package com.stf.bj.app.strategy;

import com.stf.bj.app.game.players.Play;
import com.stf.bj.app.strategy.FullStrategy.StrategyTypes;

public class Strategy {

	protected int hardPlays[][];
	protected int softPlays[][];
	protected int pairPlays[][];

	private boolean playInsurance;
	private double wager;

	// HIT = 1, STAY=2, DOUBLEHIT=3, DOUBLESTAY=4, SPLIT=5, SURRENDERHIT=6,
	// SURRENDERSTAY=7, SURRENDERSPLIT=8
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
		return hardPlays[total - 3][dealerCardValue - 1];
	}

	private int getSoftPlay(int total, int dealerCardValue) {
		return softPlays[total - 12][dealerCardValue - 1];
	}

	private int getSplitPlay(int total, int dealerCardValue) {
		return pairPlays[total / 2 - 1][dealerCardValue - 1];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int row = 0; row < StrategyTypes.HARD.getRows(); row++) {
			for (int column = 0; column < 10; column++) {
				sb.append(hardPlays[row][column]);
			}
		}
		for (int row = 0; row < StrategyTypes.SOFT.getRows(); row++) {
			for (int column = 0; column < 10; column++) {
				sb.append(softPlays[row][column]);
			}
		}
		for (int row = 0; row < StrategyTypes.PAIRS.getRows(); row++) {
			for (int column = 0; column < 10; column++) {
				sb.append(pairPlays[row][column]);
			}
		}
		return sb.toString();
	}

	public static Strategy fromString(String s) {
		Strategy strategy = new Strategy();

		strategy.hardPlays = new int[19][10];
		strategy.softPlays = new int[10][10];
		strategy.pairPlays = new int[10][10];

		int counter = 0;
		for (int row = 0; row < 19; row++) {
			for (int column = 0; column < 10; column++) {
				strategy.hardPlays[row][column] = Integer.valueOf(s.substring(counter, counter + 1));
				counter++;
			}
		}
		for (int row = 0; row < 10; row++) {
			for (int column = 0; column < 10; column++) {
				strategy.softPlays[row][column] = Integer.valueOf(s.substring(counter, counter + 1));
				counter++;
			}
		}
		for (int row = 0; row < 10; row++) {
			for (int column = 0; column < 10; column++) {
				strategy.pairPlays[row][column] = Integer.valueOf(s.substring(counter, counter + 1));
				counter++;
			}
		}

		return strategy;
	}

	public boolean getInsurance() {
		return playInsurance;
	}

	public void setInsurance(boolean playInsurance) {
		this.playInsurance = playInsurance;
	}

	public double getWager() {
		return wager;
	}

	public void setWager(double wager) {
		this.wager = wager;
	}

	public int getTacticValue(StrategyTypes type, int row, int column) {
		switch (type) {
		case HARD:
			return hardPlays[row][column];
		case PAIRS:
			return pairPlays[row][column];
		case SOFT:
			return softPlays[row][column];
		default:
			throw new IllegalStateException();
		}
	}

	public void setTacticValue(StrategyTypes type, int row, int column, int tacticValue) {
		switch (type) {
		case HARD:
			if (hardPlays[row][column] != tacticValue) {
				System.out.println("Updating hard " + row + " " + column + " to " + tacticValue);
			}
			hardPlays[row][column] = tacticValue;
			return;
		case PAIRS:
			if (pairPlays[row][column] != tacticValue) {
				System.out.println("Updating pair " + row + " " + column + " to " + tacticValue);
			}
			pairPlays[row][column] = tacticValue;
			return;
		case SOFT:
			if (softPlays[row][column] != tacticValue) {
				System.out.println("Updating soft " + row + " " + column + " to " + tacticValue);
			}
			softPlays[row][column] = tacticValue;
			return;
		default:
			throw new IllegalStateException();
		}

	}

}
