package com.stf.bj.app.players;

import com.badlogic.gdx.Input;
import com.stf.bj.app.table.Event;
import com.stf.bj.app.table.EventType;

public class Human implements Player {

	int mySpotIndex;
	double wager = -1;
	double lastWager = -1;

	int currentCommand = -1;
	private static final int NULL_COMMAND = -1;
	boolean lastInsurancePlay = false;

	@Override
	public Play getMove(int handIndex, boolean canDouble, boolean canSplit, boolean canSurrender) {
		Play p = null;
		if (currentCommand != NULL_COMMAND) {
			if (currentCommand == Input.Keys.Q) {
				p = Play.HIT;
			} else if (currentCommand == Input.Keys.W) {
				p = Play.STAND;
			} else if (currentCommand == Input.Keys.A) {
				if (canDouble) {
					p = Play.DOUBLEDOWN;
				}
			} else if (currentCommand == Input.Keys.S) {
				if (canSplit) {
					p = Play.SPLIT;
				}
			}
			currentCommand = NULL_COMMAND;
		}

		return p;
	}

	@Override
	public boolean getInsurancePlay() {
		if (currentCommand == Input.Keys.E) {
			lastInsurancePlay = true;
			currentCommand = NULL_COMMAND;
		} else if (currentCommand == Input.Keys.D) {
			lastInsurancePlay = false;
			currentCommand = NULL_COMMAND;
		}
		return lastInsurancePlay;
	}

	@Override
	public void setSpot(int spotIndex) {
		mySpotIndex = spotIndex;
	}

	@Override
	public void sendEvent(Event e) {
		if (e.getType() == EventType.TABLE_OPENED && wager != -1) {
			lastWager = wager;
			wager = -1;
			lastInsurancePlay = false;
		}
	}

	@Override
	public double getWager() {
		updateWager();
		return wager;
	}

	private void updateWager() {
		if (currentCommand == Input.Keys.E) {
			if (wager == -1) {
				if (lastWager != -1) {
					wager = lastWager;
				} else {
					wager = 5.0;
				}
			} else {
				wager += 5.0;
			}
			currentCommand = NULL_COMMAND;
		} else if (currentCommand == Input.Keys.D) {
			if (wager > 0) {
				wager -= 5.0;
			} else {
				wager = 0;
			}
			currentCommand = NULL_COMMAND;
		}

	}

	public void sendInput(int keyPressed) {

		if (keyPressed == NULL_COMMAND) {
			return;
		}
		if (currentCommand != keyPressed) {
			currentCommand = keyPressed;
		} else {
			currentCommand = NULL_COMMAND;
		}
	}

}
