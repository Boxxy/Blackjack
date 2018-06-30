package com.stf.bj.app.game.players;

import com.stf.bj.app.game.bj.Spot;
import com.stf.bj.app.game.server.Card;
import com.stf.bj.app.game.server.Hand;
import com.stf.bj.app.settings.AppSettings;
import com.stf.bj.app.strategy.FullStrategy;

public class Coach {
	private final Spot spot;
	private final AppSettings settings;
	private final FullStrategy strategy;
	double trueCount = 0;
	int count = 0;
	int cardsSeen = 0;
	int dealerUpCard = -15;
	double decksLeft = 0;

	private String message = "";

	public Coach(Spot spot, FullStrategy strategy, AppSettings settings) {
		this.spot = spot;
		this.settings = settings;
		this.strategy = strategy;
		System.out.println("Greetings, I am your coach!");
	}

	public void checkWager(double wager) {
		if (wager == 0)
			return;
		if (wager != getWager()) {
			String s = generateWagerMesssage(wager);
			setMessage(s);
		}

	}

	private String generateWagerMesssage(double wager) {
		return "Missed wager: count was " + count + " (" + getTrueCount() + ") and you waged " + wager + " instead of "
				+ getWager();
	}

	public void newHand() {
		dealerUpCard = -15;
		message = "";
	}

	public void sendDealerCard(Card card) {
		if (dealerUpCard < 0) {
			dealerUpCard = card.getValue();
		}
		sendCard(card);
	}

	public void sendCard(Card card) {
		cardsSeen++;
		count += getCount(card);
		if (cardsSeen % 26 == 0) {
			decksLeft = (settings.getDecks() * 52 - cardsSeen) / 52.0;
		}
	}

	private int getCount(Card card) {
		switch (card.getRank()) {
		case ACE:
			return -2;
		case EIGHT:
			return 0;
		case FIVE:
			return 2;
		case FOUR:
			return 2;
		case JACK:
			return -2;
		case KING:
			return -2;
		case NINE:
			return 0;
		case QUEEN:
			return -2;
		case SEVEN:
			return 1;
		case SIX:
			return 2;
		case TEN:
			return -2;
		case THREE:
			return 2;
		case TWO:
			return 1;
		default:
			throw new IllegalStateException();
		}
	}

	public void sendShuffle() {
		trueCount = 0;
		count = 0;
		cardsSeen = 0;
		decksLeft = settings.getDecks();
	}

	public void checkPlay(Play playDone, boolean canDouble, boolean canSplit, boolean canSurrender) {
		Hand h = spot.getCurrentHand().getHand();
		Play idealPlay = getProperPlay(h, canDouble, canSplit, canSurrender);
		if (playDone != idealPlay) {
			String s = generatePlayErrorString(idealPlay, playDone, h,
					wasPlayCorrectWithAnyStrategy(playDone, h, canDouble, canSplit, canSurrender));
			setMessage(s);
		}
	}

	private void setMessage(String s) {
		message = s;
		System.out.println(s);
	}

	private String generatePlayErrorString(Play idealPlay, Play playDone, Hand h, boolean sometimesCorrect) {
		String s = "Missed play: " + (h.isSoft() ? " soft " : " ") + h.getSoftTotal() + " vs " + dealerUpCard;
		s += "\n      You " + playDone + " instead of " + idealPlay;
		if (sometimesCorrect) {
			s += "\n         At some counts you would have made the correct play, but the count was " + count + " ("
					+ getTrueCount() + ")";
		}
		return s;
	}

	private Play getProperPlay(Hand h, boolean canDouble, boolean canSplit, boolean canSurrender) {
		return strategy.getPlay(getTrueCount(), h.getSoftTotal(), dealerUpCard, h.isSoft(), canDouble, canSplit,
				canSurrender);
	}

	private boolean wasPlayCorrectWithAnyStrategy(Play p, Hand h, boolean canDouble, boolean canSplit,
			boolean canSurrender) {

		for (int count = -9; count < 10; count++) {
			if (p == strategy.getPlay(count, h.getSoftTotal(), dealerUpCard, h.isSoft(), canDouble, canSplit,
					canSurrender)) {
				return true;
			}
		}

		return false;
	}

	public void checkInsurancePlay() {
		if (spot.getWager() == 0) {
			return;
		}
		boolean boughtInsurance = (spot.isBettingInsurance() || spot.tookEvenMoney());
		boolean shouldHave = strategy.getInsurance(getTrueCount());

		if (shouldHave != boughtInsurance) {
			String s = generateInsuranceMessage(shouldHave);
			setMessage(s);
		}
	}

	private String generateInsuranceMessage(boolean shouldHave) {
		return "Missed Insurance: True count was " + count + " (" + getTrueCount() + "), so you should "
				+ (shouldHave ? "" : "NOT ") + " have bought insurance";
	}

	private double getWager() {
		return strategy.getWager(getTrueCount());
	}

	private int getTrueCount() {
		return (int) (count / decksLeft);
	}

	public String getMessage() {
		return message;
	}

}
