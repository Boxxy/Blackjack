package com.stf.bj.app.game.players;

import java.util.Random;

import com.stf.bj.app.game.bj.Spot;
import com.stf.bj.app.game.players.strategy.HoChunkStrategyDown0;
import com.stf.bj.app.game.players.strategy.HoChunkStrategyUp0;
import com.stf.bj.app.game.players.strategy.HoChunkStrategyUp6;
import com.stf.bj.app.game.players.strategy.Strategy;
import com.stf.bj.app.settings.AppSettings;

public class BasicIndexCountingBot extends BasicCountingBot {

	public BasicIndexCountingBot(AppSettings settings, Random r, Spot s) {
		super(settings, r, s);
	}

	Strategy below0Strategy = new HoChunkStrategyDown0();
	Strategy above0Strategy = new HoChunkStrategyUp0();
	Strategy above6Strategy = new HoChunkStrategyUp6();

	public Play getMove(int handIndex, boolean canDouble, boolean canSplit, boolean canSurrender) {
		if (delay > 0) {
			delay--;
			return null;
		}
		
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
