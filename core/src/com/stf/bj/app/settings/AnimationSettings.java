package com.stf.bj.app.settings;

import java.util.Random;

public class AnimationSettings {

	public enum FlipDealerCard {
		IMMEDIATELY, AFTER_SECOND_CARD;
	}
	
	public enum CoachSetting {
		SYSTEM_OUT, SCREEN_PRINT, BOTH;
	}

	private final FlipDealerCard flipDealerCard;
	private final boolean doubleCardSideways;
	private final float horiziontalCardOffset;
	private final float verticalCardOffset;
	private final boolean immediatelyPayEvenMoney;
	private final CoachSetting coachSetting;

	/**
	 * 
	 * @param flipDealerCard
	 * @param payAndCleanPlayerBlackjack
	 * @param doubleCardSideways
	 * @param horizontalCardOffset
	 * @param verticalCardOffset
	 * @param immediatelyPayEvenMoney
	 */
	public AnimationSettings(FlipDealerCard flipDealerCard, boolean doubleCardSideways, float horizontalCardOffset,
			float verticalCardOffset, boolean immediatelyPayEvenMoney, CoachSetting coachSetting) {
		this.flipDealerCard = flipDealerCard;
		this.doubleCardSideways = doubleCardSideways;
		this.horiziontalCardOffset = horizontalCardOffset;
		this.verticalCardOffset = verticalCardOffset;
		this.immediatelyPayEvenMoney = immediatelyPayEvenMoney;
		this.coachSetting = coachSetting;
	}

	public FlipDealerCard getFlipDealerCard() {
		return flipDealerCard;
	}

	public boolean isDoubleCardSideways() {
		return doubleCardSideways;
	}

	public float getHoriziontalCardOffset() {
		return horiziontalCardOffset;
	}

	public float getVerticalCardOffset() {
		return verticalCardOffset;
	}

	public boolean isImmediatelyPayEvenMoney() {
		return immediatelyPayEvenMoney;
	}

	public static AnimationSettings getClassic() {
		return new AnimationSettings(FlipDealerCard.IMMEDIATELY, false, 13f, 30f, false, CoachSetting.BOTH);
	}

	public static AnimationSettings getNew() {
		return new AnimationSettings(FlipDealerCard.AFTER_SECOND_CARD, true, 13f, 30f, true, CoachSetting.SCREEN_PRINT);
	}

	public static AnimationSettings getPratice() {
		return new AnimationSettings(FlipDealerCard.AFTER_SECOND_CARD, true, 13f, 30f, true, CoachSetting.SYSTEM_OUT);
	}

	public static AnimationSettings getTestNew() {
		return new AnimationSettings(FlipDealerCard.AFTER_SECOND_CARD, true, 0f, -20f, true, CoachSetting.BOTH);
	}

	public static AnimationSettings getTestClassic() {
		return new AnimationSettings(FlipDealerCard.IMMEDIATELY, false, 0f, -20f, false, CoachSetting.BOTH);
	}

	public static AnimationSettings getRandom(Random r) {
		if (r == null) {
			r = new Random(System.currentTimeMillis());
		}
		int i = r.nextInt(4);
		if (i == 0) {
			return getClassic();
		} else if (i == 1) {
			return getNew();
		} else if (i == 2) {
			return getTestNew();
		} else if (i == 3) {
			return getTestClassic();
		}

		return getNew();
	}

	public CoachSetting getCoachSetting() {
		return coachSetting;
	}

}
