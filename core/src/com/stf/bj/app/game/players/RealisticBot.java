package com.stf.bj.app.game.players;

import java.util.Random;

import com.stf.bj.app.game.bj.Spot;
import com.stf.bj.app.game.players.strategy.BaseHoChunkStrategy;
import com.stf.bj.app.game.players.strategy.Strategy;
import com.stf.bj.app.game.players.strategy.TypicalNoobStrategy;
import com.stf.bj.app.game.server.Event;
import com.stf.bj.app.game.server.EventType;
import com.stf.bj.app.settings.AppSettings;
import com.stf.bj.app.settings.TableRules;

public class RealisticBot extends BasicBot {

	private enum InsuranceStrategy {
		NEVER, EVEN_MONEY_ONLY, GOOD_HANDS_ONLY, RARELY, OFTEN;
	}

	private enum PlayAbility {
		PERFECT, NOOB, RANDOM;
	}

	private enum BetChanging {
		NEVER, RANDOM;
	}

	private final InsuranceStrategy insuranceStrategy;
	private final PlayAbility playAbility;
	private final Random r;
	private double wager = -1.0;
	private Strategy good;
	private Strategy bad;
	private final BetChanging betChanging;
	private boolean insurancePlay = false;
	private boolean calculatedInsurancePlay = false;

	public RealisticBot(AppSettings settings, Random r, Spot s) {
		super(settings, r, s);

		if (r == null) {
			r = new Random(System.currentTimeMillis());
		}
		this.r = r;
		insuranceStrategy = InsuranceStrategy.values()[r.nextInt(InsuranceStrategy.values().length)];
		playAbility = PlayAbility.values()[r.nextInt(PlayAbility.values().length)];
		setBaseBet();
		if (playAbility == PlayAbility.NOOB) {
			setStrategy(new TypicalNoobStrategy());
		} else if (playAbility == PlayAbility.RANDOM) {
			good = new TypicalNoobStrategy();
			bad = new BaseHoChunkStrategy();
		}
		betChanging = BetChanging.RANDOM;
	}

	@Override
	public void sendEvent(Event e) {
		super.sendEvent(e);
		if (e.getType() == EventType.DECK_SHUFFLED) {
			if (betChanging == BetChanging.RANDOM && r.nextInt(2) == 0) {
				wager = 5;
			}
		}
	}

	@Override
	public double getWager() {
		if (delay > 0) {
			delay--;
			return -1;
		}
		return wager;
	}

	private void setBaseBet() {
		int rInt = r.nextInt(12);
		if (rInt < 6) {
			wager = 5.0 * (1 + rInt);
		} else if (rInt < 9) {
			wager = 10.0;
		} else {
			wager = 5.0;
		}

	}

	@Override
	public boolean getInsurancePlay() {

		if (insuranceStrategy == InsuranceStrategy.NEVER) {
			return false;
		}

		if (!calculatedInsurancePlay) {
			insurancePlay = calculateInsurancePlay();
			calculatedInsurancePlay = true;
		}

		return insurancePlay;
	}

	private boolean calculateInsurancePlay() {

		switch (insuranceStrategy) {
		case EVEN_MONEY_ONLY:
			return getHandSoftTotal(0) == 21;
		case GOOD_HANDS_ONLY:
			return getHandSoftTotal(0) > 16 + r.nextInt(3);
		case OFTEN:
			return r.nextInt(2) == 0;
		case RARELY:
			return r.nextInt(5) == 0;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public Play getMove(int handIndex, boolean canDouble, boolean canSplit, boolean canSurrender) {
		if (delay > 0) {
			delay--;
			return null;
		}
		return super.getMove(handIndex, canDouble, canSplit, canSurrender);
	}

	@Override
	protected void reset() {
		super.reset();
		calculatedInsurancePlay = false;
		int random = r.nextInt(10);
		if (random == 0 && playAbility == PlayAbility.RANDOM) {
			setStrategy(good);
		} else if (random == 1 && playAbility == PlayAbility.RANDOM) {
			setStrategy(bad);
		} else if ((random == 2 || random == 3) && betChanging == BetChanging.RANDOM) {
			wager += 5;
		} else if (random == 4 && betChanging == BetChanging.RANDOM) {
			if (wager > 6) {
				wager -= 5;
			}
		}

	}

}
