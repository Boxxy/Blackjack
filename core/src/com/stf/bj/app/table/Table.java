package com.stf.bj.app.table;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The table class contains all of the black jack game logic, the Table's spots,
 * table's rules, all Cards, Money that is in play, and the dealer. The tabel
 * class is completely unaware of the players, only the actions done on spots at
 * the table (e.g. first base bet 10 bucks, third base doubled down) This class
 * should be used by a controlling class that handles player input and sends
 * information to the players. Burned cards, The dealer hole card, and undealt
 * shoe cards can not be accessed from this class.
 * 
 * @author Boxxy
 *
 */
public class Table {

	private TableRules tableRules;

	/**
	 * Enum for Table State: Closed Idle (Open but empty) Ready To Deal (Open with
	 * bets on the table) Play (Cards on the table)
	 */
	public enum TableStates {
		CLOSED, IDLE, READY_TO_DEAL, PLAY;
	}

	private Shoe shoe;
	private Hand dealer;
	private List<Spot> spots;
	private TableStates state;
	private Queue<Event> eventsQueue;
	private Spot currentSpot;
	private int currentSpotIndex;
	private int currentHandIndex;

	public Table(TableRules tableRules) {
		if (tableRules == null)
			throw new IllegalArgumentException();
		this.tableRules = tableRules;
		shoe = new Shoe(tableRules.getDecks());
		dealer = new Hand();
		spots = new ArrayList<Spot>(tableRules.getSpots());
		for (int i = 0; i < tableRules.getSpots(); i++) {
			spots.add(new Spot(tableRules.getSplits()+1));
		}
		state = TableStates.CLOSED;
		eventsQueue = new LinkedList<Event>();
	}
	
	public boolean canClose() {
		return state == TableStates.IDLE;
	}

	public void closeTable() {
		if (!canClose())
			throw new IllegalStateException("Can't Close Table");
		registerEvent(new Event(EventType.TABLE_CLOSED));
		state = TableStates.CLOSED;
	}

	public boolean canOpenTable() {
		return state == TableStates.CLOSED;
	}

	public void openTable() {
		if (!canOpenTable())
			throw new IllegalStateException("Can't Open Table");
		state = TableStates.IDLE;
		registerEvent(new Event(EventType.TABLE_OPENED));
		shuffle();
	}

	private boolean canShuffle() {
		return (state == TableStates.IDLE || state == TableStates.CLOSED || state == TableStates.READY_TO_DEAL);
	}

	private void shuffle() {
		if (!canShuffle())
			throw new IllegalStateException("Can't shuffle now");
		shoe.shuffle();
		registerEvent(new Event(EventType.DECK_SHUFFLED));
		shoe.setCut(tableRules.getPenetration());
	}

	public boolean canStartDeal() {
		return state == TableStates.READY_TO_DEAL;
	}

	public boolean acceptingWagers() {
		return (state == TableStates.IDLE || state == TableStates.READY_TO_DEAL);
	}
	
	/**
	 * Checks if the current spot can hit/stand. This requires the game to be in a
	 * state where players are hitting.
	 * 
	 * @return True if the given spot can hit or stand at this time.
	 */
	public boolean inPlay() {
		if (state != TableStates.PLAY)
			return false;
		return (currentSpot != null);
	}

	public void startDeal() {
		if (!canStartDeal())
			throw new IllegalStateException("Can't Start Deal");
		registerEvent(new Event(EventType.DEAL_STARTED));
		deal();
		if (dealer.getCard(0).isAce()) {
			registerEvent(new Event(EventType.INSURANCE_OFFERED));
		}
		if (dealer.isBlackjack()) {
			registerEvent(new Event(EventType.DEALER_BLACKJACK));
			afterPlay();
		} else {
			cleanupBlackjacks();
			updateCurrentSpotAndHand();
			if (currentSpot!=null) {
				state = TableStates.PLAY;
				registerEvent(new Event(EventType.PLAY_STARTED));
				registerEvent((new Event(EventType.SPOT_STARTED_PLAY)).setSpotIndex(currentSpotIndex));
			} else {
				afterPlay();
			}
		}
	}

	/**
	 * Loop over every active spot. If the spot has blackjack, clean the spot up and
	 * register a black jack win for that spot
	 */
	private void cleanupBlackjacks() {

		for (int spotIndex = 0; spotIndex < spots.size(); spotIndex++) {
			Spot spot = spots.get(spotIndex);
			if (!spot.isActive())
				continue;
			if (spot.getCurrentHand().isBlackjack()) {
				spot.cleanUp();
				registerEvent(new Event(EventType.WIN_BLACKJACK).setSpotIndex(spotIndex).setHandIndex(0));
			}
		}
	}
	
	/**
	 * Called after the last spot has made it's move. Flips the dealers hidden card.
	 * If any spots remain, the dealer will hit as needed Payouts are recorded,
	 * cards are cleaned up Table is reopened, deck is shuffle if needed.
	 */
	private void afterPlay() {
		registerEvent(new Event(EventType.DEALER_GAINED_CARD).setCard(dealer.getCard(1)));
		if (spotsHaveCards())
			dealerTurn();
		payOut();
		cleanUpCards();
		registerEvent(new Event(EventType.TABLE_OPENED));
		state = TableStates.IDLE;
		if (shoe.needsShuffle())
			shuffle();
	}

	/**
	 * Checks if any spots still have cards on the table (Are active)
	 * 
	 * @return True if there is atleast one spot with cards still on the table
	 */
	private boolean spotsHaveCards() {
		for (Spot s : spots)
			if (s.isActive())
				return true;
		return false;
	}

	/**
	 * Adds as many cards as needed to the dealer's hand to reach the table
	 * requirement. If the dealer's hand total is large enough, this method does
	 * nothing (no cards are added).
	 */
	private void dealerTurn() {
		while (dealerNeedsToHit())
			addDealerCard();
		if (dealer.isBusted()) {
			registerEvent(new Event(EventType.DEALER_BUSTED));
		} 
		
		registerEvent(new Event(EventType.DEALER_ENDED_TURN));
	}

	/**
	 * Checks if the dealers hand is under 17 and requires another card Depending on
	 * table rules, will also require the dealer to hit with soft 17
	 * 
	 * @return True if the dealer needs another card
	 */
	private boolean dealerNeedsToHit() {
		if (tableRules.dealerHitSoft17()) {
			return dealer.getSoftTotal() < 18 && dealer.getHardTotal() < 17;
		} else {
			return dealer.getSoftTotal() < 17;
		}
	}

	/**
	 * Loops over hands in each spot. Payout events are recorded for each hand, and
	 * the hand is cleaned up.
	 */
	private void payOut() {
		for (int i = 0; i < spots.size(); i++) {
			Spot s = spots.get(i);
			if (s.isActive()) {
				s.resetCurrentHandForPayout();
			}

			while (s.isActive()) {
				Hand hand = s.getCurrentHand();
				EventType payoutEventType = getPayoutEventTypeFromHand(hand);
				registerEvent(new Event(payoutEventType).setSpotIndex(i).setHandIndex(s.getCurrentHandIndex()));
				s.clearCurrentHand();
			}
		}
	}

	/**
	 * Compares the given hand with the dealers hand to decide if the hand wins or
	 * loses. Additional handling for blackjacks and doubles.
	 * 
	 * @param hand
	 *            - Hand to compare with the dealer's hand
	 * @return One of 6 possible Events: WIN, WIN_DOUBLE, WIN_BLACKJACK, PUSH,
	 *         LOSE_DOUBLE, LOSE
	 */
	private EventType getPayoutEventTypeFromHand(Hand hand) {
		int comparison = hand.compareToDealer(dealer);
		if (comparison > 0) {
			if (hand.isBlackjack()) {
				return EventType.WIN_BLACKJACK;
			} else if (hand.isDoubled()) {
				return EventType.WIN_DOUBLE;
			} else {
				return EventType.WIN;
			}
		} else if (comparison == 0) {
			return EventType.PUSH;
		} else {
			if (hand.isDoubled()) {
				return EventType.LOSE_DOUBLE;
			} else {
				return EventType.LOSE;
			}
		}
	}

	/**
	 * Resets the dealers hand, as well as all spots, as if they were at a fresh
	 * table.
	 */
	private void cleanUpCards() {
		dealer.clearHand();
		for (Spot spot : spots) {
			spot.cleanUp();
		}
	}

	/**
	 * Opts a spot to play in the next round of blackjack. This will need to be
	 * called every round (hand). Note that wager is not recorded at the table
	 * level, and is the responsibility of the calling object. This would be similar
	 * to a player putting money on the table at a spot before the round was dealt.
	 * 
	 * @param spotIndex
	 */
	public void activateSpot(int spotIndex) {
		if (!canActivateSpot(spotIndex))
			throw new IllegalStateException("Can't set activate spot " + spotIndex + " state of " + state);
		spots.get(spotIndex).activate();
		registerEvent(new Event(EventType.SPOT_JOINED).setSpotIndex(spotIndex));

		if (state != TableStates.READY_TO_DEAL) {
			registerEvent(new Event(EventType.TABLE_READY_TO_DEAL));
			state = TableStates.READY_TO_DEAL;
		}

	}

	/**
	 * Checks if a specified spot at the table can be activated. This requires the
	 * table to be in a state that is allowing new spots to join, not mid round.
	 * This also requires the spot to be unoccupied.
	 * 
	 * @param spotIndex
	 * @return True if the player can activate the spot.
	 */
	public boolean canActivateSpot(int spotIndex) {
		if (spots.get(spotIndex).isActive())
			return false;
		return (state == TableStates.IDLE || state == TableStates.READY_TO_DEAL);
	}

	/**
	 * Opts a spot out of playing in the next round of blackjack. This will only be
	 * called if a player joins, and then wants to leaves before the round is dealt.
	 * Note that wager is not recorded at the table level, and is the responsibility
	 * of the calling object. This would be similar to a player putting money on the
	 * table at a spot, and then pulling it off before the cards were dealt.
	 * 
	 * @param spotIndex
	 */
	public void deactivateSpot(int spotIndex) {
		if (!canDeactivateSpot(spotIndex))
			throw new IllegalStateException("Can't deactivate spot " + spotIndex + " state of " + state);
		spots.get(spotIndex).deactivate();
		registerEvent(new Event(EventType.SPOT_LEFT).setSpotIndex(spotIndex));

		if (state != TableStates.IDLE && !spotsHaveCards()) {
			registerEvent(new Event(EventType.TABLE_OPENED));
			state = TableStates.IDLE;
		}
	}

	/**
	 * Checks if a specified spot at the table can be deactivated. This requires the
	 * table to be in a state that is allowing spots to back out, not mid round.
	 * This also requires the spot to be occupied.
	 * 
	 * @param spotIndex
	 * @return True if the player can deactivate the spot.
	 */
	public boolean canDeactivateSpot(int spotIndex) {
		if (!spots.get(spotIndex).isActive())
			return false;
		return (state == TableStates.IDLE || state == TableStates.READY_TO_DEAL);
	}

	/**
	 * For each spot that has been activated, add a card and publicize the card
	 * addition with an event. Then publicly add a card to the dealer. Again for
	 * each spot Then add a card to the dealer and announce it via event, but do not
	 * say what the card is (Face down card)
	 */
	private void deal() {
		dealEachSpotOnce();
		addDealerCard();
		dealEachSpotOnce();
		dealer.addCard(shoe.drawNext()); // Adds the card to the hand, but does not register an event
		registerEvent(new Event(EventType.DEALER_GAINED_FACE_DOWN_CARD)); // Registers an event that the dealer gained a
																			// card, but we do not say what the card is.
	}

	/**
	 * Publicly adds one card to each spot that is in the round.
	 */
	private void dealEachSpotOnce() {
		int spotIndex;
		for (spotIndex = 0; spotIndex < spots.size(); spotIndex++) {
			if (spots.get(spotIndex).isActive()) {
				spots.get(spotIndex).dealCard(getCardForSpot(spotIndex,0));
			}
		}
	}

	/**
	 * Grabs a card from the shoe and broadcast that this card is going to the given
	 * spot. NOTE that this tag does not add the card to the spot/hand.
	 * 
	 * @param spotIndex
	 *            - Spot which is going to receive this card
	 * @param handIndex 
	 *            - Hand of spot which is going to receive this card
	 * @return The card that was grabbed from the shoe.
	 */
	private Card getCardForSpot(int spotIndex, int handIndex) {
		Card c = shoe.drawNext();
		registerEvent(new Event(EventType.SPOT_GAINED_CARD).setCard(c).setSpotIndex(spotIndex).setHandIndex(handIndex));
		return c;
	}

	/**
	 * Publicly draws a card and adds it to the dealers hand.
	 */
	private void addDealerCard() {
		Card c = shoe.drawNext();
		registerEvent(new Event(EventType.DEALER_GAINED_CARD).setCard(c));
		dealer.addCard(c);
	}

	/**
	 * Registers a public hit for the current spot, as well as the card gained. Additionally
	 * checks for bust and 21.
	 * 
	 */
	public void hit() {
		if (!inPlay())
			throw new IllegalStateException("Can not hit");
		registerEvent(new Event(EventType.SPOT_HIT).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
		currentSpot.hit(getCardForSpot(currentSpotIndex, currentHandIndex));
		if (handleBust())
			return;
		handle21();
	}

	/**
	 * Checks if the current spot is busted (Over 21 on the current hand) If so, a player
	 * bust is registered as well as a player loss / double loss. The hand is then
	 * removed from the spot and the table moves on to the next hand.
	 * 
	 * @return True if the hand was busted and handled.
	 */
	private boolean handleBust() {
		if (currentSpot.isBusted()) {
			registerEvent(new Event(EventType.PLAYER_BUSTED).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
			if (currentSpot.getCurrentHand().isDoubled()) {
				registerEvent(new Event(EventType.LOSE_DOUBLE).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
			} else {
				registerEvent(new Event(EventType.LOSE).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
			}
			currentSpot.clearCurrentHand();
			handleHandFinished();
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if the current spot currently has a hand value of 21. If so, moves on
	 * to the next hand.
	 * 
	 * @return true if the hand was 21 and handled
	 */
	private boolean handle21() {
		if (currentSpot.is21()) {
			currentSpot.goToNextHand();
			handleHandFinished();
			return true;
		}
		return false;
	}

	/**
	 * Called after the current hand finishes for whatever reason (Bust, 21, stand, etc.)
	 * 
	 * First we check to see what spot at the table is up next for play.
	 * 
	 * If no spots are up, we move on to the dealer phase.
	 * 
	 * If a spot is up, but it is a different spot than the spot of the hand that just finished, we notify the table that the previous spot is done
	 * 
	 * Lastly, we check if the next hand up is from the same spot and needs a card because it was created
	 * from a split (the hand has one card), we add another card and continue the play. 
     *
	 *
	 */
	private void handleHandFinished() {
		int previousSpotIndex = currentSpotIndex;
		updateCurrentSpotAndHand();
		
		//that was the last hand of the shoe to play
		if (currentSpot == null) {
			registerEvent((new Event(EventType.SPOT_FINISHED_PLAY)).setSpotIndex(previousSpotIndex));
			registerEvent(new Event(EventType.PLAY_FINISHED));
			afterPlay();
			return;
		}
		
		//that was the last hand of that spot, so we move to a new spot
		if(currentSpotIndex!=previousSpotIndex) {
			registerEvent((new Event(EventType.SPOT_FINISHED_PLAY)).setSpotIndex(previousSpotIndex));
			registerEvent((new Event(EventType.SPOT_STARTED_PLAY)).setSpotIndex(currentSpotIndex));
			return;
		}
		
		//there is another hand with that spot, if so it would definitely need a card
		if (currentSpot.needsCard()) {
			registerEvent((new Event(EventType.SPOT_ONTO_NEXT_HAND)).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
			currentSpot.dealCard(getCardForSpot(currentSpotIndex,currentHandIndex));
			handleFreshlySplitHand();
			return;
		} 
		
		throw new IllegalStateException("A hand finished but not sure how we got here");
	}

	private void updateCurrentSpotAndHand() {

		for (int spotIndex = 0; spotIndex < spots.size(); spotIndex++) {
			if (spots.get(spotIndex).isWaiting()) {
				currentSpotIndex = spotIndex;
				currentSpot = spots.get(spotIndex);
				currentHandIndex = currentSpot.getCurrentHandIndex();
				return;
			}
		}
		currentSpotIndex = -1;
		currentSpot = null;
	}

	/**
	 * Processes whether or not a hand that was just created via split can have
	 * action taken upon it. If no action can be legally taken (21 or Ace resplit)
	 * we move on to the next hand.
	 * 
	 */
	private void handleFreshlySplitHand() {
		if (handle21())
			return;

		boolean justSplitAces;
		justSplitAces = currentSpot.getCurrentHand().getCard(0).isAce();
		if (!justSplitAces) {
			return;
		}

		if (canSplit()) {
			return;
		}

		currentSpot.stay();
		handleHandFinished();
	}

	/**
	 * Publicly registers the current spot as staying. Moves on to the next hand.
	 */
	public void stand() {
		if (!inPlay())
			throw new IllegalStateException("Can not stay");
		registerEvent(new Event(EventType.SPOT_STAND).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
		currentSpot.stay();
		handleHandFinished();
	}

	public boolean canDouble() {
		if (!inPlay())
			return false;
		if (!currentSpot.canDoubleDown())
			return false;
		if (!currentSpot.isNatural() && !tableRules.doubleAfterSplit())
			return false;
		return true;
	}

	public boolean canSurrender() {
		if (!inPlay())
			return false;
		if (!currentSpot.canSurrender())
			return false;
		if (!tableRules.surrenderAllowed())
			return false;
		return true;
	}
	
	/**
	 * Publicly registers the current spot as surrendering. The hand is collected and a half payout is given.
	 */
	public void surrender() {
		if (!canSurrender())
			throw new IllegalStateException("Can not Surrender");
		registerEvent(new Event(EventType.SPOT_SURRENDER).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
		registerEvent(new Event(EventType.LOSE_HALF).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
		currentSpot.clearCurrentHand();
		handleHandFinished();
	}

	/**
	 * Publicly registers the current spot as doubling. The hand is flagged as a
	 * double wager (No money is handled by this object). A new card is added to the
	 * current hand, table moves on to the next hand.
	 */
	public void doubleDown() {
		if (!canDouble())
			throw new IllegalStateException("Can not Double");
		registerEvent(new Event(EventType.SPOT_DOUBLE).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
		currentSpot.doubleDown(getCardForSpot(currentSpotIndex, currentHandIndex));
		if (handleBust())
			return;
		currentSpot.stay();
		handleHandFinished();
	}

	/**
	 * Does quite a few checks to ensure the current spot can split their hand.
	 * Includes making sure it's the play phase, basic split check (matching cards),
	 * table split limit check, aces resplit checks.
	 * 
	 * @return True if the current spot can split.
	 */
	public boolean canSplit() {
		if (state != TableStates.PLAY)
			return false;
		if (currentSpot==null)
			return false;
		if (!currentSpot.canSplit())
			return false;
		if (currentSpot.getTimesSplit() >= tableRules.getSplits())
			return false;
		if (!tableRules.aceReSplit() && !currentSpot.isNatural() && currentSpot.isAces())
			return false;
		return true;
	}

	/**
	 * Splits the current spot's current hand into two hands and adds a new card to
	 * that hand. Calls handleFreshlySplitHand to check ace split rules and 21.
	 * 
	 */
	public void split() {
		Card newCard;
		if (!canSplit())
			throw new IllegalStateException("Can not Split");
		registerEvent(new Event(EventType.SPOT_SPLIT).setSpotIndex(currentSpotIndex).setHandIndex(currentHandIndex));
		currentSpot.split();
		newCard = getCardForSpot(currentSpotIndex, currentHandIndex);
		currentSpot.dealCard(newCard);
		handleFreshlySplitHand();
	}

	/**
	 * Adds the given Event e to the events queue
	 * 
	 * @param e
	 */
	private void registerEvent(Event e) {
		eventsQueue.add(e);
	}

	/**
	 * @return True if there is an event available on th queue
	 */
	public boolean hasNewEvent() {
		return !eventsQueue.isEmpty();
	}

	/**
	 * @return The oldest event on the queue
	 */
	public Event grabLastEvent() {
		return eventsQueue.remove();
	}

	public Hand getHandTEMPDEBUGMETHOD() {
		// TODO remove me eventually this only exist because i write lazy testing
		// methods. Perhaps i should do more dependency injection???
		return currentSpot.getCurrentHand();
	}
	
	public Card getDealerUpCarTEMPDEBUGMETHOD() {
		return dealer.getCard(0);
	}
	
	public int getCurrentSpot() {
		//TODO remove me eventually this only exist for testing
		return currentSpotIndex;
	}
	public int getCurrentHand() {
		//TODO remove me eventually this only exist for testing
		return currentHandIndex;
	}

	// TODO REMOVE ME EVENTUALLY OR BETTER ENCAPSULATE ME FOR UNIT TEST ONLY
	public void RigCardsUnitTestMethod(List<Card> cards) {
		shoe.rigNextCardsDangerRemoveMe(cards);
	}

}
