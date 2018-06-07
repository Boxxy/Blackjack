package com.stf.bj.app.players;

import com.stf.bj.app.table.Event;

public interface Player{
	public void setSpot(int spotIndex);
	public void sendEvent(Event e);
	public Play getMove(int handIndex, boolean canDouble, boolean canSplit, boolean canSurrender);
	public boolean getInsurancePlay();
	public double getWager();
}
