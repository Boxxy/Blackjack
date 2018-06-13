package com.stf.bj.app.game.animation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.stf.bj.app.game.bj.Hand;
import com.stf.bj.app.game.server.Card;
import com.stf.bj.app.settings.AppSettings;

public class HandSprite {

	private final Hand hand;
	private final AppSettings settings;
	private final List<CardSprite> cardSprites;
	private Vector2 anchor;
	private Texture arrowTexture;
	private static final float MONEY_HAND_OFFSET_Y = -20f;
	private static final float ARROW_OFFSET_X = 20f;
	private static final float ARROW_OFFSET_Y = -70f;
	private static final float DOUBLED_CARD_OFFSET_X = 96f;
	private static final float DOUBLED_CARD_OFFSET_Y = 25f;
	private static final float CARD_WIDTH = 71f;
	private static final float CARD_HEIGHT = 96f;

	public HandSprite(Hand hand, AppSettings settings) {
		this.hand = hand;
		this.settings = settings;
		cardSprites = new ArrayList<CardSprite>();
	}

	public void tick(Batch batch, BitmapFont font) {
		for (CardSprite cs : cardSprites) {
			cs.tick(batch);
		}
		drawMoney(batch, font);
		if (hand.isTheCurrentPlayingHand()) {
			drawArrow(batch);
		}
	}

	private void drawArrow(Batch batch) {
		float x = ARROW_OFFSET_X;
		float y = ARROW_OFFSET_Y;
		batch.draw(arrowTexture, anchor.x + x, anchor.y + y);
	}

	private void drawMoney(Batch batch, BitmapFont font) {
		float y = MONEY_HAND_OFFSET_Y;

		font.draw(batch, hand.getChipDisplay(), anchor.x, anchor.y + y, CARD_WIDTH, Align.center, false);
	}

	public void clear() {
		cardSprites.clear();
	}

	public void addCard(Card card, boolean doubled) {
		float rotation = 0f;
		if (doubled && settings.getAnimationSettings().isDoubleCardSideways()) {
			rotation = 90f;
		}

		CardSprite cs = new CardSprite(card, AnimationManager.DECK_ANCHOR, doubled);
		cs.setVelocity(settings.getTimingSettings().getCardSpeed());
		cs.setDestination(getPlayerCardLocation(hand.getSize() - 1, doubled), rotation);
		cardSprites.add(cs);
	}

	private Vector2 getPlayerCardLocation(int cardIndex, boolean doubled) {
		if (cardIndex < 0) {
			throw new IllegalStateException();
		}
		float x = settings.getAnimationSettings().getHoriziontalCardOffset() * cardIndex;
		float y = settings.getAnimationSettings().getVerticalCardOffset() * cardIndex;

		if (doubled && settings.getAnimationSettings().isDoubleCardSideways()) {
			x += DOUBLED_CARD_OFFSET_X;
			y += DOUBLED_CARD_OFFSET_Y;
			if (settings.getAnimationSettings().getHoriziontalCardOffset() == 0) {
				x += (CARD_WIDTH - CARD_HEIGHT) / 2 + .5f;
			}
		}

		if (settings.getAnimationSettings().getVerticalCardOffset() < 0) {
			y -= settings.getAnimationSettings().getVerticalCardOffset() * 5;
		}

		return new Vector2(anchor.x + x, anchor.y + y);
	}

	public Hand getHand() {
		return hand;
	}

	public void setAnchor(Vector2 anchor, int delayBeforeMoving) {
		this.anchor = anchor;
		updateCardSpriteLocations(delayBeforeMoving);
	}

	private void updateCardSpriteLocations(int delayBeforeMoving) {
		for (int cardIndex = 0; cardIndex < cardSprites.size(); cardIndex++) {
			CardSprite cs = cardSprites.get(cardIndex);
			cs.addDelay(delayBeforeMoving);
			cs.setDestination(getPlayerCardLocation(cardIndex, cs.isDoubled()));
		}
	}

	public int size() {
		return cardSprites.size();
	}

	public void setDestinationForAllCards(Vector2 destination, int delayBeforeMoving) {
		for (CardSprite cs : cardSprites) {
			cs.addDelay(delayBeforeMoving);
			cs.setDestination(destination);
		}
	}

	public List<CardSprite> getCardSprites() {
		return cardSprites;
	}

	public void split(HandSprite recievingHandSprite) {
		recievingHandSprite.cardSprites.add(cardSprites.remove(1));
	}

	public void setArrowTexture(Texture arrowTexture) {
		this.arrowTexture = arrowTexture;
	}

}
