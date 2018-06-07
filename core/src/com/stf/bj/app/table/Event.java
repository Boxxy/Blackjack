package com.stf.bj.app.table;

/**
 * A record of an event that happened at the blackjack table. All events have an
 * EventType such as TABLE_OPENED. Some event types also are associated with
 * spots such as SPOT_BUSTED Some event types are associated with cards such as
 * DEALER_GAINED_CARD Some event types are associated with both spots and cards
 * such as PLAYER_GAINED_CARD
 * 
 * When stored in order, multiple Event classes will perfectly capture all RNG,
 * Player and House decisions that happened at a table. There are some
 * additional events that are stored for convenience but contain no information
 * that could not be previously inferred, such as SPOT_FINISHED_PLAY and
 * SPOT_STARTED_PLAY
 * 
 * @author Boxxy
 *
 */
public class Event {
	private final EventType type;
	private int spotIndex;
	private int handIndex;
	private Card card;
	private static final int NULL_INT = -69;

	/**
	 * Creates a new Event instance based on the given event type
	 * 
	 * @param type
	 *            - Type of Event
	 */
	public Event(EventType type) {
		this.type = type;
		spotIndex = NULL_INT;
		handIndex = NULL_INT;
	}

	public EventType getType() {
		return type;
	}

	/**
	 * Set's the spot in which this event occurred, can only be called once for an
	 * event.
	 * 
	 * @param spotIndex
	 * @return The same event, useful for chaining.
	 */
	public Event setSpotIndex(int spotIndex) {
		if (spotIndex < 0)
			throw new IllegalArgumentException("Invalid spot " + spotIndex + " passed to event " + type);
		if (this.spotIndex != NULL_INT)
			throw new IllegalStateException("Spot in event set twice");
		this.spotIndex = spotIndex;
		return this;
	}
	/**
	 * Set's the hand in which this event occurred, can only be called once for an
	 * event.
	 * 
	 * @param handIndex
	 * @return The same event, useful for chaining.
	 */
	public Event setHandIndex(int handIndex) {
		if (handIndex < 0)
			throw new IllegalArgumentException("Invalid hand " + handIndex + " passed to event " + type);
		if (this.handIndex != NULL_INT)
			throw new IllegalStateException("Hand in event set twice");
		this.handIndex = handIndex;
		return this;
	}

	/**
	 * Set's the card this event is referring to, can only be called once for an
	 * event.
	 * 
	 * @param card
	 * @return The same event, useful for chaining.
	 */
	public Event setCard(Card card) {
		if (this.card != null)
			throw new IllegalStateException("Card in event set twice");
		this.card = card;
		return this;
	}

	public boolean hasSpot() {
		return spotIndex != NULL_INT;
	}
	public boolean hasHand() {
		return handIndex != NULL_INT;
	}

	public int getSpotIndex() {
		return spotIndex;
	}
	public int getHandIndex() {
		return handIndex;
	}

	public Card getCard() {
		return card;
	}

	public String toString() {
		String s = "";

		if (hasSpot())
			s += "Spot: " + spotIndex;
		s = rpad(s, 9);
		
		if (hasHand())
			s += "Hand: " + handIndex;
		s = rpad(s, 18);

		if (type != null)
			s += type.toString();
		s = rpad(s, 39);

		if (card != null)
			s += "" + card;
		return s;
	}

	/**
	 * Temporary debug method, SHOULD NOT EXIST IN FINAL
	 */
	public String rpad(String inStr, int finalLength) {
		return (inStr + "                                                                             ").substring(0,
				finalLength);
	}

}
