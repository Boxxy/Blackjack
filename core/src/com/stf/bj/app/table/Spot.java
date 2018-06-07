package com.stf.bj.app.table;

import java.util.ArrayList;
import java.util.List;

/**
 * Spot contains all of the important logic for a single spot at the blackjack
 * table. No player based logic like cash pool is concerned, just money and
 * cards on the table. Actions such as splitting, surrendering, etc are done
 * here on the hands
 * 
 * @author Boxxy
 *
 */
public class Spot {

	private List<Hand> hands;
	private int currentHandIndex;
	private Hand currentHand;
	private boolean active;
	private int timesSplit;

	public Spot(int maximumNumberOfHands) {
		hands = new ArrayList<Hand>(maximumNumberOfHands);
		for (int i = 0; i < maximumNumberOfHands; i++) {
			hands.add(new Hand());
		}
		cleanUp();
	}

	public void cleanUp() {
		for (Hand h : hands) {
			h.clearHand();
		}
		active = false;
		timesSplit = 0;
		currentHand = null;
		currentHandIndex = -1;
	}

	/**
	 * Called after a hand bust or is cleaned up.
	 */
	public void clearCurrentHand() {
		currentHand.clearHand();
		goToNextHand();
	}

	public boolean isWaiting() {
		return currentHand != null;
	}

	public void dealCard(Card c) {
		if (currentHand == null && currentHandIndex != -1)
			throw new IllegalStateException("No current hand set to take card");
		if (currentHand == null) {
			currentHand = hands.get(0);
			currentHandIndex = 0;
		}
		currentHand.addCard(c);
	}

	public void goToNextHand() {
		currentHandIndex++;
		if (currentHandIndex >= hands.size() || currentHandIndex < 0) {
			currentHand = null;
			for (Hand h : hands) {
				if (!h.isEmpty())
					return;
			}
			//Since every hand is empty, we can also deactivate here
			deactivate();
		} else {
			if (hands.get(currentHandIndex).isEmpty()) {
				goToNextHand();
			} else {
				currentHand = hands.get(currentHandIndex);
			}
		}
	}

	public void stay() {
		goToNextHand();
	}

	public void hit(Card c) {
		dealCard(c);
	}

	public void doubleDown(Card c) {
		dealCard(c);
		currentHand.setDoubled();
	}

	public void split() {
		currentHand.split(hands.get(++timesSplit));
	}

	public void surrender() {
		cleanUp();
	}

	public boolean isBusted() {
		return currentHand.isBusted();
	}

	public boolean is21() {
		return currentHand.getSoftTotal() == 21;
	}

	public boolean isBlackjack() {
		return currentHand.isBlackjack();
	}

	public int getCurrentHandIndex() {
		return currentHandIndex;
	}

	public Hand getCurrentHand() {
		return currentHand;
	}

	/**
	 * Whether or not this hand is even in the game. If a spot has nothing but is is
	 * waiting for a deal, it is active If a spot has some hands, but has finished
	 * play, it is active After everything bust, a spot is not active
	 * 
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Called when a spot is in play for the next deal
	 */
	public void activate() {
		active = true;
	}

	public void deactivate() {
		active = false;
	}

	/**
	 * Called before paying out entire spot. TODO make better
	 */
	public void resetCurrentHandForPayout() {
		currentHandIndex = -1;
		currentHand=null;
		goToNextHand();
	}

	public boolean canDoubleDown() {
		return getCurrentHand().getSize() == 2;
	}

	public boolean canSurrender() {
		return getCurrentHand().getSize() == 2 && getCurrentHand().isNatural();
	}

	public boolean isNatural() {
		return getCurrentHand().isNatural();
	}

	public boolean canSplit() {
		return getCurrentHand().isSplittable();
	}

	public int getTimesSplit() {
		return timesSplit;
	}

	public boolean isAces() {
		if (getCurrentHand().getSize() != 2)
			return false;
		return getCurrentHand().getCard(0).isAce() && getCurrentHand().getCard(1).isAce();
	}

	public boolean needsCard() {
		return isWaiting() && getCurrentHand().getSize() == 1;
	}

}
