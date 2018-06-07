package com.stf.bj.app.players;

import com.stf.bj.app.players.strategy.HoChunkStrategyDown0;
import com.stf.bj.app.players.strategy.HoChunkStrategyUp0;
import com.stf.bj.app.players.strategy.HoChunkStrategyUp6;
import com.stf.bj.app.players.strategy.Strategy;
import com.stf.bj.app.table.Event;
import com.stf.bj.app.table.EventType;
import com.stf.bj.app.table.TableRules;

public class BasicIndexCountingBot extends BasicCountingBot {

	public BasicIndexCountingBot(TableRules rules) {
		super(rules);
	}

	Strategy below0Strategy = new HoChunkStrategyDown0();
	Strategy above0Strategy = new HoChunkStrategyUp0();
	Strategy above6Strategy = new HoChunkStrategyUp6();

	public Play getMove(int handIndex, boolean canDouble, boolean canSplit, boolean canSurrender) {

		double trueCount = getTrueCount();
		if (trueCount < 0) {
			setStrategy(below0Strategy);
		} else if (trueCount < 6) {
			setStrategy(above0Strategy);
		} else {
			setStrategy(above6Strategy);
		}

		return super.getMove(handIndex, canDouble, canSplit, canSurrender);
	}

}
