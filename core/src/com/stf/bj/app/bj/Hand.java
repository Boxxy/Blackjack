package com.stf.bj.app.bj;

import com.stf.bj.app.sprites.HandSprite;
import com.stf.bj.app.table.Card;

public class Hand {
	private double chipStacks = 0;
	private String chipDisplay = "";
	private final HandSprite sprite;
	private final Spot spot;
	private final com.stf.bj.app.table.Hand hand;
	private int actualIndex;
	private boolean currentPlayingHand = false;
	
	public Hand(Spot spot) {
		sprite = new HandSprite(this);
		this.spot = spot;
		this.hand = new com.stf.bj.app.table.Hand();
	}
	
	public double getChipStacks() {
		return chipStacks;
	}

	public String getChipDisplay() {
		return chipDisplay;
	}

	public double addChips(double stacks, double wager) {
		chipStacks += stacks;
		updateDisplayString(wager);
		return stacks * wager;
	}
	

	private void updateDisplayString(double wager) {
		if (chipStacks == 0) {
			chipDisplay = "";
		} else if (chipStacks == 1) {
			chipDisplay = String.valueOf(wager);
		} else if (chipStacks == .5) {
			chipDisplay = String.valueOf(wager / 2);
		} else if (chipStacks == 2.5) {
			chipDisplay = wager + " + " + wager * 1.5;
		} else {
			chipDisplay = wager + " x " + chipStacks;
		}

	}

	public HandSprite getSprite() {
		return sprite;
	}

	public void addCard(Card card) {
		hand.addCard(card);
		sprite.addCard(card);
	}

	public Spot getSpot() {
		return spot;
	}

	public boolean hasCards() {
		return hand.getSize() > 0;
	}

	public void setActualIndex(int actualIndex) {
		this.actualIndex = actualIndex;
	}
	
	public int getActualIndex() {
		return actualIndex;
	}

	public int getSize() {
		return hand.getSize();
	}

	public com.stf.bj.app.table.Hand getHand() {
		return hand;
	}

	public void clearCards() {
		hand.clearHand();
		actualIndex = 0;
	}

	public boolean isTheCurrentPlayingHand() {
		return currentPlayingHand ;
	}
	
	public void setCurrentPlayingHand(boolean currentPlayingHand) {
		this.currentPlayingHand = currentPlayingHand;
	}
}
