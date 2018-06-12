package com.stf.bj.app.game.players.strategy;

import com.stf.bj.app.game.players.Play;

public interface Strategy {
	public Play getPlay(int handTotal, int dealerUpCardValue, boolean handIsSoft, boolean canDouble, boolean canSplit, boolean canSurrender);
}
