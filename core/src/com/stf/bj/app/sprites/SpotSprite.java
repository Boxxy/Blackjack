package com.stf.bj.app.sprites;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.stf.bj.app.bj.Hand;
import com.stf.bj.app.bj.Spot;

public class SpotSprite {
	private final Spot spot;
	private final List<HandSprite> handSprites;
	private Vector2 anchor;
	private static final float HAND_SPACING_X = 50f;
	private static final float MONEY_SPOT_OFFSET_Y = -150f;
	private static final float INSURANCE_SPOT_OFFSET_X = 20f;
	private static final float INSURANCE_SPOT_OFFSET_Y = 170f;

	public SpotSprite(Spot spot, List<HandSprite> handSprites) {
		this.spot = spot;
		this.handSprites = handSprites;
	}

	public int tick(SpriteBatch batch, BitmapFont font, boolean noUpdates) {
		int finishedSprites = 0;
		for (HandSprite hs : handSprites) {
			finishedSprites += hs.tick(batch, font, noUpdates);
		}
		drawMoney(batch, font);
		return finishedSprites;
	}

	private void drawMoney(SpriteBatch batch, BitmapFont font) {
		font.draw(batch, spot.getChipDisplay(), anchor.x, anchor.y + MONEY_SPOT_OFFSET_Y);
		if(spot.isBettingInsurance()) {
			font.draw(batch, String.valueOf(spot.getWager()/2), anchor.x + INSURANCE_SPOT_OFFSET_X, anchor.y + INSURANCE_SPOT_OFFSET_Y);
		}
	}

	public void clear() {
		for (HandSprite hs : handSprites) {
			hs.clear();
		}
		resetHandAnchors();
	}

	public void setAnchor(Vector2 anchor) {
		this.anchor = anchor;
		resetHandAnchors();
	}

	public void resetHandAnchors() {
		for (HandSprite hs : handSprites) {
			hs.setAnchor(anchor);
		}
	}

	public int updateHandAnchors() {
		int activeHands = spot.getActiveHands();
		if (activeHands == 0)
			return 0;

		int spritesMoved = 0;
		for (HandSprite hs : handSprites) {
			if (!hs.getHand().hasCards()) {
				continue;
			}
			// only 1 hand should have a handAnchor = spotAnchor
			float x = (activeHands - 1) * HAND_SPACING_X;
			x += hs.getHand().getActualIndex() * -HAND_SPACING_X * 2;
			spritesMoved += hs.setAnchor(new Vector2(anchor.x + x, anchor.y));
		}
		return spritesMoved;
	}

	public List<HandSprite> getHandSprites() {
		return handSprites;
	}

	public void setArrowTexture(Texture arrowTexture) {
		for(HandSprite hs : handSprites) {
			hs.setArrowTexture(arrowTexture);
		}
		
	}

	public void setCardSpacing(float horiziontalCardOffset, float verticalCardOffset) {
		// TODO Auto-generated method stub
		
	}
}
