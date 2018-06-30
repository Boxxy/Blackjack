package com.stf.bj.app.strategy;

public enum Tactics {
	HIT(" H ", 1), STAY(" S ", 2), DOUBLE_HIT(" D ", 3), DOUBLE_STAY("DS", 4), SPLIT(" P ", 5), SURRENDER_HIT("RH",
			6), SURRENDER_STAY("RS", 7), SURRENDER_SPLIT("RP", 8);

	private final String display;
	private final int value;

	private Tactics(String display, int value) {
		this.display = display;
		this.value = value;
	}

	public String getDisplay() {
		return display;
	}

	public int getValue() {
		return value;
	}

	public static Tactics getNext(Tactics currentTactic) {
		int ordinal = currentTactic.ordinal();
		ordinal++;
		if (ordinal == values().length) {
			ordinal = 0;
		}
		return values()[ordinal];
	}

	public static Tactics getPrevious(Tactics currentTactic) {
		int ordinal = currentTactic.ordinal();
		ordinal--;
		if (ordinal < 0) {
			ordinal = values().length - 1;
		}
		return values()[ordinal];
	}

	public static Tactics fromValue(int tacticValue) {
		for (Tactics t : values()) {
			if (t.getValue() == tacticValue) {
				return t;
			}
		}
		throw new IllegalStateException();
	}
}
