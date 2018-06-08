package com.stf.bj.app.sprites;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.stf.bj.app.AppSettings;
import com.stf.bj.app.bj.Spot;
import com.stf.bj.app.sprites.AnimationSettings.FlipDealerCard;
import com.stf.bj.app.table.Card;

public class SpriteManager {

	private final AppSettings settings;
	private final List<CardSprite> dealer = new ArrayList<CardSprite>();
	private final List<CardSprite> discard = new ArrayList<CardSprite>();
	private final List<SpotSprite> players;
	SpriteBatch batch;
	private static final float DEALER_CARD_SPACING_X = 80f;
	private static final float SPOT_Y = 200f;
	private static final float EDGE_SPOT_Y = 300f;
	public static final Vector2 DECK_ANCHOR = new Vector2(1000f, 600f);
	public static final Vector2 SHOE_ANCHOR = new Vector2(100f, 600f);
	private static final Vector2 MIDDLE_TEXT_ANCHOR = new Vector2(500f, 400f);
	private static final Vector2 DEALER_ANCHOR = new Vector2(800f, 600f);
	private static final Vector2 SPOT_0_ANCHOR = new Vector2(1100f, EDGE_SPOT_Y);
	private static final Vector2 SPOT_1_ANCHOR = new Vector2(900f, SPOT_Y);
	private static final Vector2 SPOT_2_ANCHOR = new Vector2(700f, SPOT_Y);
	private static final Vector2 SPOT_3_ANCHOR = new Vector2(500f, SPOT_Y);
	private static final Vector2 SPOT_4_ANCHOR = new Vector2(300f, SPOT_Y);
	private static final Vector2 SPOT_5_ANCHOR = new Vector2(100f, EDGE_SPOT_Y);
	private static final Vector2 SPOT_ANCHORS[] = { SPOT_0_ANCHOR, SPOT_1_ANCHOR, SPOT_2_ANCHOR, SPOT_3_ANCHOR,
			SPOT_4_ANCHOR, SPOT_5_ANCHOR };
	private final BitmapFont font;
	private final BitmapFont bigFont;
	int movingSprites = 0;
	int delay = 0;
	int delayThreshold = 0;
	private String displayString = "";
	private Card dealerUpCard;
	private boolean secondDealerCardIsFaceDown;

	public SpriteManager(SpriteBatch batch, List<Spot> spots, AppSettings settings) {
		this.batch = batch;
		this.settings = settings;
		players = new ArrayList<SpotSprite>();
		Texture arrowTexture = new Texture(Gdx.files.internal("arrow.png"));
		for (Spot s : spots) {
			s.getSprite().setAnchor(SPOT_ANCHORS[s.getIndex()]);
			players.add(s.getSprite());
			s.getSprite().setArrowTexture(arrowTexture);
			s.getSprite().setCardSpacing(settings.getAnimationSettings().getHoriziontalCardOffset(),
					settings.getAnimationSettings().getVerticalCardOffset());
		}
		font = new BitmapFont();
		bigFont = new BitmapFont();
		bigFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		bigFont.getData().setScale(3f);

	}

	public boolean busy() {
		if (delayThreshold > 0)
			return true;
		if (movingSprites < 0)
			throw new IllegalStateException("Lost some sprites");
		return movingSprites > 0;
	}

	public void render() {
		boolean noUpdates = false;
		if (delayThreshold > delay) {
			delay++;
			noUpdates = true;
		} else {
			delay = 0;
			delayThreshold = 0;
		}

		batch.begin();

		bigFont.draw(batch, displayString, MIDDLE_TEXT_ANCHOR.x, MIDDLE_TEXT_ANCHOR.y);

		for (CardSprite cs : dealer) {
			if (cs.tick(batch, noUpdates))
				movingSprites--;
		}

		for (CardSprite cs : discard) {
			if (cs.tick(batch, noUpdates))
				movingSprites--;
		}

		for (SpotSprite ss : players) {
			movingSprites -= ss.tick(batch, font, noUpdates);

		}

		if (movingSprites < 0) {
			throw new IllegalStateException("Lost track of some sprites");
		}

		batch.end();

	}

	public void addDealerCard(Card card) {
		int dealerCards = dealer.size();
		
		//When we actually get the second card, we quit early as a new card isn't added
		if(dealerCards == 2 && secondDealerCardIsFaceDown) {
			secondDealerCardIsFaceDown = false;
			setFaceDownDealerCard(1,card);
			return;
		}
		
		//When we get the hidden second card, flip the first card if it's hidden
		if(dealerCards == 1) {
			secondDealerCardIsFaceDown = true;
			if(settings.getAnimationSettings().getFlipDealerCard() == FlipDealerCard.AFTER_SECOND_CARD) {
				setFaceDownDealerCard(0,dealerUpCard); //TODO delay this
			}
		}
		
		if(dealerCards == 0 && settings.getAnimationSettings().getFlipDealerCard() == FlipDealerCard.AFTER_SECOND_CARD) {
			dealerUpCard = card;
			card = null;
		}

		addNewDealerCard(card);
	}
	
	private void addNewDealerCard(Card card) {
		CardSprite cs = new CardSprite(card, DECK_ANCHOR, false);
		cs.setDestination(getDealerCardLocation());
		dealer.add(cs);
		movingSprites++;
	}
	
	private void setFaceDownDealerCard(int cardIndex, Card card) {
		dealer.get(cardIndex).setTextureFromCard(card);
	}

	public void newDeal() {
		for (SpotSprite ss : players) {
			ss.clear();
		}
		dealer.clear();
		discard.clear();
	}

	public void discardSpot(SpotSprite ss) {
		for (HandSprite hs : ss.getHandSprites()) {
			discardHand(hs);
		}
	}

	public void discardHand(HandSprite hs) { // Called after a bust, and general clean up phase
		movingSprites += hs.setDestinationForAllCards(SHOE_ANCHOR);
		discard.addAll(hs.getCardSprites());
		hs.clear();
	}

	public void discardDealer() {
		for (CardSprite cs : dealer) {
			cs.setDestination(SHOE_ANCHOR);
			movingSprites++;
		}
		discard.addAll(dealer);
		dealer.clear();
	}

	private Vector2 getDealerCardLocation() {
		return getDealerCardLocation(dealer.size());
	}

	private Vector2 getDealerCardLocation(int cardIndex) {
		float x = -DEALER_CARD_SPACING_X * cardIndex;
		return new Vector2(DEALER_ANCHOR.x + x, DEALER_ANCHOR.y);
	}

	public void discardAllSprites() {
		for (SpotSprite ss : players) {
			discardSpot(ss);
		}
		discardDealer();
	}

	public void setDelay(int i) {
		delay = 0;
		delayThreshold = i;

	}

	public void setDisplayString(String s) {
		displayString = s;
	}

	public void addMovingSprite(int spritesMoved) {
		movingSprites += spritesMoved;

	}

	public void updateHandPlacements(SpotSprite spotSprite) {
		movingSprites += spotSprite.updateHandAnchors();
	}

}
