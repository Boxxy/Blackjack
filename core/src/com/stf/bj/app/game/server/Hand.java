package com.stf.bj.app.game.server;

import java.util.ArrayList;
import java.util.List;

/**
 * This is specifically a black jack hand. Controls a group of cards as well
 * The main method is addCard, which adds a card and updates various important values.
 * All of the relevant values (Splittable, hasAce, etc are cached as object variables)
 * 
 * @author Boxxy
 *
 */
public class Hand{

	private List<Card> hand;
	private int hardTotal, softTotal;
	private boolean natural, splittable, hasAce, soft, busted, blackjack, doubled;

	/**
	 * Only constructor, creates a new empty hand with no variables set.
	 */
	public Hand() {
		hand = new ArrayList<Card>(13); //"Ace x 7 (Soft 17) + Five (Hard 12) + Ace x 4 (Hard 16) + Any Card"
		clearHand();
	}

	/**
	 * Completely resets the hand, as if a new hand was created.
	 */
	public void clearHand() {
		hand.clear();
		hardTotal = 0;
		softTotal = 0;
		natural = true;
		splittable = false;
		hasAce = false;
		soft = false;
		busted = false;
		blackjack = false;
		doubled = false;
	}

	/**
	 * Adds the given card to the hand.
	 * @param c - Card to add to the hand
	 */
	public void addCard(Card c) {
		if (softTotal > 20)
			throw new IllegalStateException("Added a card to a hand that was 21 or over");
		if (c == null)
			throw new IllegalArgumentException();

		hand.add(c);
		hardTotal += c.getValue();

		if (hardTotal > 21)
			busted = true;

		splittable = false;

		if (getSize() == 2)
			splittable = (hand.get(0).getValue() == hand.get(1).getValue());

		if (getSize() > 2)
			natural = false;

		if (c.isAce() || hasAce) {
			hasAce = true;
			if (hardTotal < 12) {
				softTotal = hardTotal + 10;
				soft = true;
				if (natural && softTotal == 21)
					blackjack = true;
			} else {
				softTotal = hardTotal;
				soft = false;
			}
		} else {
			softTotal = hardTotal;
			soft = false;
		}
	}

	/**
	 * Only works when *this* current hand is splittable (exactly two cards of the same value) and the passed recievingHand is empty.
	 * Resets the internal values of each hand, and then set's their natural flag to false.
	 * Lastly the 0th card in this hand is re-added to this hand. The 1st card in this hand is added to the recievingHand
	 * 
	 * 
	 * @param recievingHand - Empty hand which receives one of the split cards.
	 */
	public void split(Hand recievingHand) {
		if (getSize() != 2 || recievingHand.getSize() != 0 || !splittable)
			throw new IllegalStateException("Invalid Split Call " + (getSize() != 2) + (recievingHand.getSize() != 0) + (!splittable) + " .");

		recievingHand.addCard(hand.remove(1));
		recievingHand.natural = false;

		Card c = hand.remove(0);
		clearHand();
		natural = false;
		addCard(c);
	}

	public int getSize() {
		return hand.size();
	}

	public boolean isEmpty(){
		return hand.size() == 0;
	}

	public int getHardTotal() {
		return hardTotal;
	}

	public int getSoftTotal() {
		return softTotal;
	}

	/**
	 * Our definition of "natural" for a hand is that it is composed of the original 2 cards dealt by the dealer.
	 * This is used to determine whether Surrender, Double (depending on table DAS rules), and black jack are allowed/counted.
	 * @return Whether or not the hand is natural.
	 */
	public boolean isNatural() {
		return natural;
	}

	public boolean isSplittable() {
		return splittable;
	}

	public boolean isSoft() {
		return soft;
	}

	public boolean isBusted() {
		return busted;
	}

	public boolean isBlackjack() {
		return blackjack;
	}

	public boolean isDoubled() {
		return doubled;
	}

	public void setDoubled() {
		doubled = true;
	}

	public String toString() {
		String s = "[";
		boolean multi = false;
		for (Card c : hand) {
			if (multi)
				s += ", ";
			multi = true;
			s += c.toString();
		}
		return s + "]";
	}

	public Card getCard(int i) {
		if (getSize() == 0)
			throw new IllegalStateException("Tried to get card " + i + " on hand sized " + getSize());
		return hand.get(i);
	}

	/**
	 * Checks if this hand beats the given dealer hand
	 * @param dealer - Dealer's hand
	 * @return Positive number if this hand beats the dealer, Negative number if this hand loses to the dealer, 0 otherwise.
	 */
	public int compareToDealer(Hand dealer) {
		if (isBlackjack()) {
			if (dealer.isBlackjack()) {
				return 0;
			} else {
				return 1;
			}
		} else if (isBusted() || dealer.isBlackjack()) {
			return -1;
		} else if (dealer.isBusted()) {
			return 1;
		}
		return getSoftTotal() - dealer.getSoftTotal();
	}

}
