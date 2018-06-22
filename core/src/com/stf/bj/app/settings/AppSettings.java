package com.stf.bj.app.settings;

import com.badlogic.gdx.Preferences;
import com.stf.bj.app.settings.settings.Decks.DecksSetting;
import com.stf.bj.app.settings.settings.HumanSpot.HumanSpotSetting;
import com.stf.bj.app.settings.settings.NumberOfSpots.NumberOfSpotsSetting;
import com.stf.bj.app.settings.settings.DealerSoft17.DealerSoft17Setting;
import com.stf.bj.app.settings.settings.DoubleAfterSplit.DoubleAfterSplitSetting;
import com.stf.bj.app.settings.settings.NumberOfSplits.NumberOfSplitsSetting;
import com.stf.bj.app.settings.settings.ResplitAces.ResplitAcesSetting;
import com.stf.bj.app.settings.settings.Surrender.SurrenderSetting;
import com.stf.bj.app.settings.settings.PayoutBlackjack.PayoutBlackjackSetting;
import com.stf.bj.app.settings.settings.Penetration;
import com.stf.bj.app.settings.settings.Penetration.PenetrationSetting;
import com.stf.bj.app.settings.settings.FlipDealerCard.FlipDealerCardSetting;
import com.stf.bj.app.settings.settings.EvenMoneyPayment.EvenMoneyPaymentSetting;
import com.stf.bj.app.settings.settings.DoubleCardOrientation.DoubleCardOrientationSetting;
import com.stf.bj.app.settings.settings.BotPlayers;
import com.stf.bj.app.settings.settings.BotSpeed;
import com.stf.bj.app.settings.settings.CardSpeed;
import com.stf.bj.app.settings.settings.DealerSoft17;
import com.stf.bj.app.settings.settings.DealerSpeed;
import com.stf.bj.app.settings.settings.Decks;
import com.stf.bj.app.settings.settings.DoubleAfterSplit;
import com.stf.bj.app.settings.settings.DoubleCardOrientation;
import com.stf.bj.app.settings.settings.EvenMoneyPayment;
import com.stf.bj.app.settings.settings.FlipDealerCard;
import com.stf.bj.app.settings.settings.HumanSpot;
import com.stf.bj.app.settings.settings.NumberOfSplits;
import com.stf.bj.app.settings.settings.NumberOfSpots;
import com.stf.bj.app.settings.settings.PayoutBlackjack;
import com.stf.bj.app.settings.settings.ResplitAces;
import com.stf.bj.app.settings.settings.Setting;
import com.stf.bj.app.settings.settings.Surrender;
import com.stf.bj.app.settings.settings.TableMax;
import com.stf.bj.app.settings.settings.TableMin;
import com.stf.bj.app.settings.settings.BotPlayers.BotPlayersSetting;
import com.stf.bj.app.settings.settings.BotSpeed.BotSpeedSetting;
import com.stf.bj.app.settings.settings.CardSpeed.CardSpeedSetting;
import com.stf.bj.app.settings.settings.Coach;
import com.stf.bj.app.settings.settings.Coach.CoachSetting;
import com.stf.bj.app.settings.settings.DealerSpeed.DealerSpeedSetting;
import com.stf.bj.app.settings.settings.TableMin.TableMinSetting;
import com.stf.bj.app.settings.settings.TableMax.TableMaxSetting;

public class AppSettings {
	private final Decks decks;
	private final NumberOfSpots numberOfSpots;
	private final HumanSpot humanSpot;
	private final DealerSoft17 dealerSoft17;
	private final DoubleAfterSplit doubleAfterSplit;
	private final NumberOfSplits numberOfSplits;
	private final ResplitAces resplitAces;
	private final Surrender surrender;
	private final PayoutBlackjack payoutBlackjack;
	private final FlipDealerCard flipDealerCard;
	private final EvenMoneyPayment evenMoneyPayment;
	private final DoubleCardOrientation doubleCardOrientation;
	private final BotPlayers botPlayers;
	private final BotSpeed botSpeed;
	private final CardSpeed cardSpeed;
	private final DealerSpeed dealerSpeed;
	private final TableMin tableMin;
	private final TableMax tableMax;
	private final Penetration penetration;
	private final Coach coach;

	public AppSettings(Preferences prefs) {
		decks = new Decks(prefs);
		humanSpot = new HumanSpot(prefs);
		numberOfSpots = new NumberOfSpots(prefs);
		dealerSoft17 = new DealerSoft17(prefs);
		doubleAfterSplit = new DoubleAfterSplit(prefs);
		numberOfSplits = new NumberOfSplits(prefs);
		resplitAces = new ResplitAces(prefs);
		surrender = new Surrender(prefs);
		payoutBlackjack = new PayoutBlackjack(prefs);
		flipDealerCard = new FlipDealerCard(prefs);
		evenMoneyPayment = new EvenMoneyPayment(prefs);
		doubleCardOrientation = new DoubleCardOrientation(prefs);
		botPlayers = new BotPlayers(prefs);
		botSpeed = new BotSpeed(prefs);
		cardSpeed = new CardSpeed(prefs);
		dealerSpeed = new DealerSpeed(prefs);
		tableMin = new TableMin(prefs);
		tableMax = new TableMax(prefs);
		penetration = new Penetration(prefs);
		coach = new Coach(prefs);
	}

	public void updateAndSave() {
		decks.updateAndSave();
		humanSpot.updateAndSave();
		numberOfSpots.updateAndSave();
		dealerSoft17.updateAndSave();
		doubleAfterSplit.updateAndSave();
		numberOfSplits.updateAndSave();
		resplitAces.updateAndSave();
		surrender.updateAndSave();
		payoutBlackjack.updateAndSave();
		flipDealerCard.updateAndSave();
		evenMoneyPayment.updateAndSave();
		doubleCardOrientation.updateAndSave();
		botPlayers.updateAndSave();
		botSpeed.updateAndSave();
		cardSpeed.updateAndSave();
		dealerSpeed.updateAndSave();
		tableMin.updateAndSave();
		tableMax.updateAndSave();
		penetration.updateAndSave();
		coach.updateAndSave();
	}

	public boolean dealerHitSoft17() {
		return DealerSoft17Setting.values()[dealerSoft17.getValue()] == DealerSoft17Setting.HIT;
	}

	public Setting getDealerSoft17Setting() {
		return dealerSoft17;
	}

	public int getDecks() {
		switch (DecksSetting.values()[decks.getValue()]) {
		case EIGHT:
			return 8;
		case FOUR:
			return 4;
		case ONE:
			return 1;
		case SIX:
			return 6;
		case TWO:
			return 2;
		default:
			throw new IllegalStateException();
		}
	}

	public Setting getDeckSetting() {
		return decks;
	}

	public int getNumberOfSpots() {
		switch (NumberOfSpotsSetting.values()[numberOfSpots.getValue()]) {
		case ONE:
			return 1;
		case TWO:
			return 2;
		case THREE:
			return 3;
		case FOUR:
			return 4;
		case FIVE:
			return 5;
		case SIX:
			return 6;
		default:
			throw new IllegalStateException();
		}
	}

	public Setting getNumberOfSpotsSetting() {
		return numberOfSpots;
	}

	public HumanSpotSetting getHumanSpot() {
		return HumanSpotSetting.values()[humanSpot.getValue()];
	}

	public int getHumanSpotInt() {
		switch (HumanSpotSetting.values()[humanSpot.getValue()]) {
		case FIVE:
			return 4;
		case FOUR:
			return 3;
		case NONE:
			return -66;
		case ONE:
			return 0;
		case RANDOM:
			return -77;
		case SIX:
			return 5;
		case THREE:
			return 2;
		case TWO:
			return 1;
		default:
			throw new IllegalStateException();

		}
	}

	public Setting getHumanSpotSetting() {
		return humanSpot;
	}

	public boolean doubleAfterSplit() {
		return DoubleAfterSplitSetting.values()[doubleAfterSplit.getValue()] == DoubleAfterSplitSetting.ALLOWED;
	}

	public Setting getDoubleAfterSplitSetting() {
		return doubleAfterSplit;
	}

	public int getSplits() {
		switch (NumberOfSplitsSetting.values()[numberOfSplits.getValue()]) {
		case ONE:
			return 1;
		case THREE:
			return 3;
		case TWO:
			return 2;
		default:
			throw new IllegalStateException();
		}
	}

	public Setting getNumberOfSplitsSetting() {
		return numberOfSplits;
	}

	public boolean aceResplitAllowed() {
		return ResplitAcesSetting.values()[resplitAces.getValue()] == ResplitAcesSetting.ALLOWED;
	}

	public Setting getResplitAcesSetting() {
		return resplitAces;
	}

	public boolean surrenderAllowed() {
		return SurrenderSetting.values()[surrender.getValue()] == SurrenderSetting.LATE;
	}

	public Setting getSurrenderSetting() {
		return surrender;
	}

	public boolean flipDealerCardImmediately() {
		return FlipDealerCardSetting.values()[flipDealerCard.getValue()] == FlipDealerCardSetting.IMMEDIATELY;
	}

	public Setting getFlipDealerCardSetting() {
		return flipDealerCard;
	}

	public PayoutBlackjackSetting getPayoutBlackjack() {
		return PayoutBlackjackSetting.values()[payoutBlackjack.getValue()];
	}

	public Setting getPayoutBlackjackSetting() {
		return payoutBlackjack;
	}

	public boolean payEvenMoneyImmediately() {
		return EvenMoneyPaymentSetting.values()[evenMoneyPayment.getValue()] == EvenMoneyPaymentSetting.IMMEDIATELY;
	}

	public Setting getEvenMoneyPaymentSetting() {
		return evenMoneyPayment;
	}

	public boolean isDoubleCardSideways() {
		return DoubleCardOrientationSetting.values()[doubleCardOrientation.getValue()] == DoubleCardOrientationSetting.SIDEWAYS;
	}

	public Setting getDoubleCardOrientationSetting() {
		return doubleCardOrientation;
	}

	public BotPlayersSetting getBotPlayerType() {
		return BotPlayersSetting.values()[botPlayers.getValue()];
	}

	public Setting getBotPlayersSetting() {
		return botPlayers;
	}

	public BotSpeedSetting getBotSpeed() {
		return BotSpeedSetting.values()[botSpeed.getValue()];
	}

	public Setting getBotSpeedSetting() {
		return botSpeed;
	}

	public CardSpeedSetting getCardSpeed() {
		return CardSpeedSetting.values()[cardSpeed.getValue()];
	}

	public Setting getCardSpeedSetting() {
		return cardSpeed;
	}

	public DealerSpeedSetting getDealerSpeed() {
		return DealerSpeedSetting.values()[dealerSpeed.getValue()];
	}

	public Setting getDealerSpeedSetting() {
		return dealerSpeed;
	}

	public int getTableMin() {
		switch (TableMinSetting.values()[tableMin.getValue()]) {
		case FIVE:
			return 10;
		case TEN:
			return 10;
		case TWENTYFIVE:
			return 25;
		default:
			throw new IllegalStateException();
		}
	}

	public Setting getTableMinSetting() {
		return tableMin;
	}

	public int getTableMax() {
		switch (TableMaxSetting.values()[tableMax.getValue()]) {
		case ONE_HUNDRED:
			return 100;
		case ONE_THOUSAND:
			return 1000;
		case TWENTYFIVE:
			return 25;
		case TWO_HUNDRED:
			return 200;
		default:
			throw new IllegalStateException();
		}
	}

	public Setting getTableMaxSetting() {
		return tableMax;
	}

	public int getPenetration() {
		switch (PenetrationSetting.values()[penetration.getValue()]) {
		case DECK_AND_A_HALF:
			return 78;
		case HALF:
			return 26;
		case ONE:
			return 52;
		case THREE_QUARTERS:
			return 39;
		case TWO_DECKS:
			return 104;
		default:
			throw new IllegalStateException();
		}
	}

	public Setting getPenetrationSetting() {
		return penetration;
	}

	public boolean coachOnScreen() {
		return CoachSetting.values()[surrender.getValue()] == CoachSetting.ON_SCREEN;
	}

	public Setting getCoachSetting() {
		return coach;
	}

	public float getHorizontalCardOffset() {
		return 13f;
	}

	public float getVerticalCardOffset() {
		return 20f;
	}

}
