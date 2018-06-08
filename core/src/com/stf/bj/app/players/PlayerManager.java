package com.stf.bj.app.players;

import java.util.List;

import com.stf.bj.app.bj.Spot;
import com.stf.bj.app.sprites.AnimationSettings;
import com.stf.bj.app.sprites.SpriteManager;
import com.stf.bj.app.table.Event;
import com.stf.bj.app.table.EventType;
import com.stf.bj.app.table.Table;
import com.stf.bj.app.table.TableRules;

public class PlayerManager { //TODO i think I should just combine this with blackjack manager
	private final List<Spot> spots;
	private final AnimationSettings animationSettings;

	public PlayerManager(List<Spot> spotList, AnimationSettings animationSettings) {
		this.spots = spotList;
		this.animationSettings = animationSettings;
	}

	public void sendEvent(Event e) {
		for (Spot s : spots) {
			Player p = s.getPlayer();
			if (p == null)
				continue;
			p.sendEvent(e);
		}
		if (!e.hasSpot())
			return;
		Spot spot = spots.get(e.getSpotIndex());
		int hand = e.getHandIndex();
		if (e.getType().isPayout()) {
			if(spot.tookEvenMoney()) {
				return;
			}
			spot.addChips(hand, e.getType().getPayout(), false);
		}
		if (e.getType() == EventType.SPOT_DOUBLE) {
			spot.addChips(hand, 1, true); // TODO do we really want to do this here?
		}
	}

	public void addPlayer(Spot spot, PlayerType pt, TableRules rules) {
		Player p = null;
		switch (pt) {
		case BASIC_BOT:
			p = new BasicBot(rules);
			break;
		case BASIC_COUNTING_BOT:
			p = new BasicCountingBot(rules);
			break;
		case BASIC_INDEX_COUNTING_BOT:
			p = new BasicIndexCountingBot(rules);
			break;
		case HUMAN:
			p = new Human();
			break;
		default:
			throw new IllegalArgumentException("Player Type not yet supported");

		}
		spot.addPlayer(p);
		p.setSpot(spot.getIndex()); // TODO is this really needed?

	}

	public boolean updateWagers(Table table) {
		boolean updated = false;
		for (Spot s : spots) {
			if (s.getPlayer() == null)
				continue;
			if (updateWager(s, table)) {
				updated = true;
			}
		}
		return updated;
	}

	private boolean updateWager(Spot s, Table table) {
		double newWager = s.getPlayer().getWager();
		double oldWager = s.getWager();
		if (newWager < 0) {// No update from player
			if (table.canActivateSpot(s.getIndex()) && s.singleBetOut()) {
				table.activateSpot(s.getIndex());
				return true;
			}
			return false;
		} else if (newWager == 0 && oldWager == 0) { // Player said no bet and they already weren't betting
			return false;
		} else if (newWager == oldWager && !table.canActivateSpot(s.getIndex())) { // Player said same bet, and is
																					// already in
			return false;
		}

		// We actually have to update chips here
		s.removeAllChips();
		s.setWager(newWager);
		if (newWager == 0) {
			if (table.canDeactivateSpot(s.getIndex())) {
				table.deactivateSpot(s.getIndex());
			}
			return true;
		}

		s.addChips(0, 1, true);
		if (table.canActivateSpot(s.getIndex()))
			table.activateSpot(s.getIndex());
		return true;
	}

	public String getDisplayString(int spot) {
		return spots.get(spot).getChipDisplay();
	}

	public String getDisplayString(int spot, int hand) {
		return spots.get(spot).getHand(hand).getChipDisplay();
	}

	public void sendInput(int keyPressed) {
		for (Spot s : spots) {
			if (!s.isHuman()) {
				continue;
			}
			s.getHuman().sendInput(keyPressed);
		}
	}

	public boolean betsClean() {
		for (Spot s : spots) {
			if (!s.isReady()) {
				return false;
			}
		}
		return true;
	}

	public boolean updateInsurances(SpriteManager sm) {
		boolean updated = false;
		for (Spot s : spots) {
			if (s.getPlayer() == null)
				continue;
			if (!s.singleBetOut())
				continue;
			if (updateInsurance(sm, s)) {
				updated = true;
			}
		}
		return updated;
	}

	private boolean updateInsurance(SpriteManager sm, Spot s) {
		boolean newPlay = s.getPlayer().getInsurancePlay();
		boolean oldPlay = s.isBettingInsurance();
		
		if(animationSettings.isImmediatelyPayEvenMoney() && newPlay && s.isBlackjack()){
			payTakeEvenMoney(sm, s);
			return true;
		}
		
		if (newPlay != oldPlay) {
			s.setBettingInsurance(newPlay);
			return true;
		} else {
			return false;
		}
	}

	private void payTakeEvenMoney(SpriteManager sm, Spot s) {
		s.addChips(0, 1, false);
		s.setTookEvenMoney();
		sm.discardSpot(s.getSprite());
	}

	public void payoutInsurance() {
		for(Spot s : spots) {
			if(s.isBettingInsurance()) {
				s.payInsurance();
			}
		}
	}

	public void collectInsurance() {
		for(Spot s : spots) {
			if(s.isBettingInsurance()) {
				s.takeInsurance();
			}
		}
	}

}
