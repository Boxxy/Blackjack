package com.stf.bj.app.game.players;

import com.badlogic.gdx.Input;
import com.stf.bj.app.game.bj.Spot;
import com.stf.bj.app.game.server.Event;
import com.stf.bj.app.game.server.EventType;
import com.stf.bj.app.settings.AppSettings;
import com.stf.bj.app.strategy.FullStrategy;

public class Human implements Player {

	int mySpotIndex;
	double wager = -1;
	double lastWager = -1;

	int currentCommand = -1;
	private static final int NULL_COMMAND = -1;
	boolean lastInsurancePlay = false;
	Spot spot;
	private final Coach coach;

	public Human(Spot spot, FullStrategy strategy, AppSettings settings) {
		this.spot = spot;
		this.coach = new Coach(spot, strategy, settings);
	}

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
			} else if (currentCommand == Input.Keys.R) {
				if (canSurrender) {
					p = Play.SURRENDER;
				}
			}
			currentCommand = NULL_COMMAND;
		}
		if (p != null) {
			coach.checkPlay(p, canDouble, canSplit, canSurrender);
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

		switch (e.getType()) {
		case TABLE_OPENED:
			coach.newHand();
			if (wager != -1) {
				lastWager = wager;
				wager = -1;
				lastInsurancePlay = false;
			}
			break;
		case DEAL_STARTED:
			coach.checkWager(spot.getWager());
			break;
		case DEALER_GAINED_CARD:
			coach.sendDealerCard(e.getCard());
			break;
		case SPOT_GAINED_CARD:
			coach.sendCard(e.getCard());
			break;
		case DECK_SHUFFLED:
			coach.sendShuffle();
			break;
		case INSURANCE_COLLECTED:
		case INSURANCE_PAID:
			coach.checkInsurancePlay();
			break;
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
				if (wager > 49.0) {
					wager += 25.0;
				} else {
					wager += 5.0;
				}
			}
			currentCommand = NULL_COMMAND;
		} else if (currentCommand == Input.Keys.D) {
			if (wager > 51.0) {
				wager -= 25.0;
			} else if (wager > 0) {
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

	public Coach getCoach() {
		return coach;
	}

}
