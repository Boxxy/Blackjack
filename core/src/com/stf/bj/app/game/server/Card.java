package com.stf.bj.app.game.server;
/**
 * Pretty simple card class. Just a combination of the Suit Enum and the Rank Enum.
 * Important public function getValue() returns the cards value (1 for Ace) (10 for T,J,Q,K)
 * @author Boxxy
 *
 */
public class Card {

	private final Ranks rank;
	private final Suits suit;

	public Card(Ranks rank, Suits suit) {
		if (rank==null || suit==null)
			throw new IllegalArgumentException();
		this.rank = rank;
		this.suit = suit;
	}

	public Ranks getRank() {
		return rank;
	}

	public Suits getSuit() {
		return suit;
	}

	/**
	 * 
	 * @return 1 for Ace, 10 for TJQK, otherwise number
	 */
	public int getValue() {
		return rank.getValue();
	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		
		if (this == o)
			return true;

		if (!(o instanceof Card))
			return false;

		Card c = (Card) o;
		return rank == c.getRank() && suit == c.getSuit();
	}

	@Override
	public String toString() {
		return (rank + " of " + suit);
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + rank.ordinal();
		result = 31 * result + suit.ordinal();
		return result;
	}
	
	public boolean isAce(){
		return rank.isAce();
	}
}
