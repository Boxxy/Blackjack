package com.stf.bj.app.game.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Shoe is a simple class which contains the randomization for shuffling, as well as X number of decks of cards.
 * @author Boxxy
 *
 */
public class Shoe {

	private List<Card> shoe;
	private final Random r;
	private int index;
	private int cut;

	public Shoe(int decks) {
		if (decks < 1)
			throw new IllegalStateException();
		shoe = new ArrayList<Card>(52 * decks);
		for (int i = 0; i < decks; i++) {
			for (Ranks r : Ranks.values()) {
				for (Suits s : Suits.values()) {
					shoe.add(new Card(r, s));
				}
			}
		}
		r = new Random(System.currentTimeMillis());
		shuffle();
	}

	/**
	 * Shuffles the cards based on the Basic fisher yates shuffle https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm
	 * 
	 * Also resets the current card and the cut card.
	 */
	public void shuffle() {
		Card c1, c2;
		int j, n = shoe.size();
		for (int i = n - 1; i > 0; i--) {
			j = r.nextInt(i + 1);
			
			//Exchange j and i
			c1 = shoe.get(i);
			c2 = shoe.set(j, c1);
			shoe.set(i, c2);
		}
		index = 0;
		cut = 0;
	}

	public Card drawNext() {
		if (cut == 0)
			throw new IllegalStateException("Tried to draw without first setting cut card");
		return shoe.get(index++);
	}

	public void setCut(int cardsLeft) {
		this.cut = shoe.size() - cardsLeft;
	}

	public boolean needsShuffle() {
		return index > cut;
	}

	//TODO remove me before release, i should really write some swanky compiler directive to include/exclude this function.
	public void rigNextCardsDangerRemoveMe(List<Card> cards) {
		shoe.clear();
		shoe.addAll(0,cards);
		shuffle();
		setCut(26);
	}

}
