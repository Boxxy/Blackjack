package com.stf.bj.app.sprites;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.stf.bj.app.AppSettings;
import com.stf.bj.app.bj.Hand;
import com.stf.bj.app.table.Card;

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

	public int tick(SpriteBatch batch, BitmapFont font) {
		int spritesFinished = 0;
		for(CardSprite cs : cardSprites) {
			if (cs.tick(batch)) {
				spritesFinished++;
			}
		}
		drawMoney(batch, font);
		if(hand.isTheCurrentPlayingHand()) {
			drawArrow(batch);
		}
		return spritesFinished;
	}

	private void drawArrow(SpriteBatch batch) {
		float x = ARROW_OFFSET_X;
		float y = ARROW_OFFSET_Y;
		batch.draw(arrowTexture, anchor.x + x, anchor.y + y);
	}

	private void drawMoney(SpriteBatch batch,BitmapFont font) {
		float y = MONEY_HAND_OFFSET_Y;

		
		font.draw(batch, hand.getChipDisplay(), anchor.x, anchor.y + y);
	}

	public void clear() {
		cardSprites.clear();
	}

	public void addCard(Card card, boolean doubled) {
		float rotation = 0f;
		if(doubled && settings.getAnimationSettings().isDoubleCardSideways()) {
			rotation = 90f;
		}
		
		CardSprite cs = new CardSprite(card, SpriteManager.DECK_ANCHOR, doubled);
		cs.setVelocity(settings.getTimingSettings().getCardSpeed());
		cs.setDestination(getPlayerCardLocation(hand.getSize() - 1, doubled), rotation);
		cardSprites.add(cs);
	}

	private Vector2 getPlayerCardLocation(int cardIndex, boolean doubled) {
		if(cardIndex < 0) {
			throw new IllegalStateException();
		}
		float x = settings.getAnimationSettings().getHoriziontalCardOffset() * cardIndex;
		float y = settings.getAnimationSettings().getVerticalCardOffset() * cardIndex;
		
		if(doubled && settings.getAnimationSettings().isDoubleCardSideways()) {
			x += DOUBLED_CARD_OFFSET_X;
			y += DOUBLED_CARD_OFFSET_Y;
			if(settings.getAnimationSettings().getHoriziontalCardOffset() == 0) {
				x += (CARD_WIDTH - CARD_HEIGHT)/2 + .5f ;
			}
		}
		
		if(settings.getAnimationSettings().getVerticalCardOffset() < 0) {
			y -= settings.getAnimationSettings().getVerticalCardOffset() * 5;
		}
		
		
		
		return new Vector2(anchor.x + x, anchor.y + y);
	}

	public Hand getHand() {
		return hand;
	}

	public int setAnchor(Vector2 anchor) {
		this.anchor = anchor;
		return updateCardSpriteLocations();
	}

	private int updateCardSpriteLocations() {
		for(int cardIndex = 0; cardIndex < cardSprites.size(); cardIndex++) {
			CardSprite cs = cardSprites.get(cardIndex);
			cs.setDestination(getPlayerCardLocation(cardIndex, cs.isDoubled()));
		}
		return cardSprites.size();
	}

	public int size() {
		return cardSprites.size();
	}

	public int setDestinationForAllCards(Vector2 destination) {
		int spritesMoved = 0;
		for(CardSprite cs : cardSprites) {
			cs.setDestination(destination);
			spritesMoved++;
		}
		return spritesMoved;
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
