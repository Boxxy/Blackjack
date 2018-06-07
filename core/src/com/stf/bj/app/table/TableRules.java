package com.stf.bj.app.table;

import java.util.Random;

public class TableRules {
	private final int decks, spots, penetration, splits;
	private final boolean dealerHitSoft17, surrenderAllowed, doubleAfterSplit, aceReSplit;

	public enum PayAndCleanPlayerBlackjack{
		PLAY_START, CLEAN_UP;
	}
	private final PayAndCleanPlayerBlackjack payAndCleanPlayerBlackjack;

	/**
	 * 
	 * @param decks
	 * @param spots
	 * @param dealerHitSoft17
	 * @param surrenderAllowed
	 * @param doubleAfterSplit
	 * @param aceReSplit
	 * @param penetration
	 * @param splits
	 */
	public TableRules(int decks, int spots, boolean dealerHitSoft17, boolean surrenderAllowed, boolean doubleAfterSplit, boolean aceReSplit, int penetration, int splits, PayAndCleanPlayerBlackjack payAndCleanPlayerBlackjack) {
		this.decks = decks;
		this.spots = spots;
		this.dealerHitSoft17 = dealerHitSoft17;
		this.surrenderAllowed = surrenderAllowed;
		this.doubleAfterSplit = doubleAfterSplit;
		this.aceReSplit = aceReSplit;
		this.penetration = penetration;
		this.splits = splits;
		this.payAndCleanPlayerBlackjack = payAndCleanPlayerBlackjack;
	}

	public boolean dealerHitSoft17() {
		return dealerHitSoft17;
	}

	public boolean surrenderAllowed() {
		return surrenderAllowed;
	}

	public boolean doubleAfterSplit() {
		return doubleAfterSplit;
	}

	public boolean aceReSplit() {
		return aceReSplit;
	}

	public int getDecks() {
		return decks;
	}

	public int getSpots() {
		return spots;
	}
	
	public int getPenetration(){
		return penetration;
	}
	
	public int getSplits(){
		return splits;
	}

	public PayAndCleanPlayerBlackjack getPayAndCleanPlayerBlackjack() {
		return payAndCleanPlayerBlackjack;
	}

	public static TableRules mystic6() {
		return new TableRules(6, 6, true, false, true, true, 5*52+52/4, 3, PayAndCleanPlayerBlackjack.PLAY_START);
	}

	public static TableRules mystic4Surrender() {
		return new TableRules(4, 6, true, true, true, true, 3*52+52/4, 3, PayAndCleanPlayerBlackjack.PLAY_START);
	}
	public static TableRules hochunk() {
		return new TableRules(6, 6, true, false, true, false, 52*4+52/2, 2, PayAndCleanPlayerBlackjack.CLEAN_UP);
	}
	
	public static TableRules getRandom() {
		Random r = new Random(System.currentTimeMillis());
		int i = r.nextInt(2);
		if(i == 0) {
			return mystic6();
		}else if(i == 1) {
			return hochunk();
		}
		return mystic6();
	}
}