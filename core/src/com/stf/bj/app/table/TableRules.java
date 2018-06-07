package com.stf.bj.app.table;

public class TableRules {
	private final int decks, spots, penetration, splits;
	private final boolean dealerHitSoft17, surrenderAllowed, doubleAfterSplit, aceReSplit;

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
	public TableRules(int decks, int spots, boolean dealerHitSoft17, boolean surrenderAllowed, boolean doubleAfterSplit, boolean aceReSplit, int penetration, int splits) {
		this.decks = decks;
		this.spots = spots;
		this.dealerHitSoft17 = dealerHitSoft17;
		this.surrenderAllowed = surrenderAllowed;
		this.doubleAfterSplit = doubleAfterSplit;
		this.aceReSplit = aceReSplit;
		this.penetration = penetration;
		this.splits = splits;
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

	public static TableRules STANDARD_MYSTIC_RULES() {
		return new TableRules(6, 6, true, false, true, true, 5*52+52/4, 3);
	}
	public static TableRules STANDARD_HOCHUNK_RULES() {
		return new TableRules(6, 6, true, false, true, false, 52*4+52/2, 2);
	}
	public static TableRules STANDARD_WITH_SURRENDER() {
		return new TableRules(6, 6, true, true, true, true, 5*52+52/4, 3);
	}

	public static TableRules STANDARD_NO_ACE_RESPLIT() {
		return new TableRules(6, 6, true, false, true, false, 5*52+52/4, 3);
	}
}