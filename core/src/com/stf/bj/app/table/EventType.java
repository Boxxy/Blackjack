package com.stf.bj.app.table;

/**
 * A type of event that can occur at a table, some examples include: SPOT_HIT,
 * DEALER_GAINED_CARD, TABLE_READY_TO_DEAL, WIN_DOUBLE
 * 
 * @author Boxxy
 *
 */
public enum EventType {

	SPOT_STAND, SPOT_HIT, SPOT_DOUBLE, SPOT_SPLIT, SPOT_SURRENDER, SPOT_GAINED_CARD, DEALER_GAINED_CARD, DEALER_GAINED_FACE_DOWN_CARD, SPOT_JOINED, SPOT_LEFT, TABLE_OPENED, TABLE_CLOSED, TABLE_READY_TO_DEAL, DEAL_STARTED, DECK_SHUFFLED, PLAY_STARTED, PLAY_FINISHED, INSURANCE_OFFERED, DEALER_BLACKJACK, PLAYER_BUSTED, DEALER_BUSTED, PUSH(
			SubType.PAYOUT, 0), WIN(SubType.PAYOUT, 1), LOSE(SubType.PAYOUT, -1), WIN_BLACKJACK(SubType.PAYOUT,
					1.5), WIN_DOUBLE(SubType.PAYOUT, 2), LOSE_DOUBLE(SubType.PAYOUT, -2), LOSE_HALF(SubType.PAYOUT,
							-.5), SPOT_FINISHED_PLAY, SPOT_STARTED_PLAY, DEALER_ENDED_TURN, SPOT_ONTO_NEXT_HAND, INSURANCE_PAID, INSURANCE_COLLECTED;

	private EventType() {
		this(null, 0);
	}

	private EventType(SubType subType, double payOut) {
		this.subType = subType;
		this.payOut = payOut;
	}

	private final SubType subType;
	private final double payOut;

	private enum SubType {
		PAYOUT,
	}

	public boolean isPayout() {
		return subType == SubType.PAYOUT;
	}

	public double getPayout() {
		if (!isPayout()) {
			throw new IllegalStateException();
		}
		return payOut;
	}
}
