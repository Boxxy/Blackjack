package com.stf.bj.app.strategy;

import com.badlogic.gdx.Preferences;
import com.stf.bj.app.game.players.Play;

public class FullStrategy {

	public enum StrategyTypes {
		HARD(19), SOFT(10), PAIRS(10);

		private final int rows;

		private StrategyTypes(int rows) {
			this.rows = rows;
		}

		public int getRows() {
			return rows;
		}
	}

	private Strategy[] strategies = new Strategy[19];
	private static String DEFAULT_STRATEGY = "111111111111111111111111111111111111111111111111111111111111133333111113333333313333333333111222111112222211111222221111622222111662222211672222222222222222222222222222222222222222222222222211113311111111331111111333111111133311111113331111113333111114444422112222242222222222222222222222225555555555111555511111155551111111111111133333333115555511111555555111855555555525555525522222222222";
	private static boolean DEFAULT_INSURANCE = false;
	private static float DEFAULT_WAGER = 5f;

	public FullStrategy(Preferences prefs) {
		loadFromPreferences(prefs);
	}

	private int trueCountToIndex(int trueCount) {
		if (trueCount < -9) {
			trueCount = -9;
		} else if (trueCount > 9) {
			trueCount = 9;
		}
		return trueCount + 9;
	}

	private Strategy getStrategyFromCount(int trueCount) {
		return strategies[trueCountToIndex(trueCount)];
	}

	public Play getPlay(int trueCount, int handTotal, int dealerUpCardValue, boolean handIsSoft, boolean canDouble,
			boolean canSplit, boolean canSurrender) {
		Strategy s = getStrategyFromCount(trueCount);
		return s.getPlay(handTotal, dealerUpCardValue, handIsSoft, canDouble, canSplit, canSurrender);
	}

	public boolean getInsurance(int trueCount) {
		Strategy s = getStrategyFromCount(trueCount);
		return s.getInsurance();
	}

	public double getWager(int trueCount) {
		Strategy s = getStrategyFromCount(trueCount);
		return s.getWager();
	}

	public void setWager(int trueCount, double wager) {
		Strategy s = getStrategyFromCount(trueCount);
		s.setWager(wager);	
	}

	private void setStrategy(int trueCount, Strategy strategy) {
		strategies[trueCountToIndex(trueCount)] = strategy;
	}

	private void loadFromPreferences(Preferences prefs) {
		for (int count = -9; count < 10; count++) {
			setStrategy(count, Strategy.fromString(prefs.getString("Strategy" + count, DEFAULT_STRATEGY)));
			getStrategyFromCount(count).setWager(prefs.getFloat("Wager" + count, DEFAULT_WAGER));
			getStrategyFromCount(count).setInsurance(prefs.getBoolean("Insurance" + count, DEFAULT_INSURANCE));
		}
	}

	public Tactics getTactic(int count, StrategyTypes type, int row, int column) {
		int tacticValue = getStrategyFromCount(count).getTacticValue(type, row, column);
		return Tactics.fromValue(tacticValue);
	}

	public void setTactic(int count, StrategyTypes type, int row, int column, int tacticValue) {
		getStrategyFromCount(count).setTacticValue(type, row, column, tacticValue);

	}

	public void save(Preferences prefs, int count) {
		Strategy s = getStrategyFromCount(count);
		prefs.putString("Strategy" + count, s.toString());
		prefs.putFloat("Wager"+count, (float) s.getWager());
		prefs.putBoolean("Insurance"+count, s.getInsurance());
	}

	public void copy(int countFrom, int countTo) {
		if (countFrom == countTo) {
			return;
		}
		System.out.println("Coping from " + countFrom + " to " + countTo);
		Strategy from = getStrategyFromCount(countFrom);
		Strategy to = getStrategyFromCount(countTo);
		for (StrategyTypes st : StrategyTypes.values()) {
			for (int row = 0; row < st.getRows(); row++) {
				for (int column = 0; column < 10; column++) {
					to.setTacticValue(st, row, column, from.getTacticValue(st, row, column));
				}
			}
		}
		to.setInsurance(from.getInsurance());
		to.setWager(from.getWager());
	}
}
