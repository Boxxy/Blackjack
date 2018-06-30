package com.stf.bj.app.game.players;

import java.util.Random;

import com.stf.bj.app.game.bj.Spot;
import com.stf.bj.app.settings.AppSettings;
import com.stf.bj.app.strategy.FullStrategy;
import com.stf.bj.app.strategy.Strategy;

public class BasicIndexCountingBot extends BasicCountingBot {

	public BasicIndexCountingBot(AppSettings settings, FullStrategy strategy, Random r, Spot s) {
		super(settings, strategy, r, s);
	}

	@Override
	public Play getMove(int handIndex, boolean canDouble, boolean canSplit, boolean canSurrender) {
		if (delay > 0) {
			delay--;
			return null;
		}

		return super.getMove((int) getTrueCount(), handIndex, canDouble, canSplit, canSurrender);
	}

}
