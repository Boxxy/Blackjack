package com.stf.bj.app.game.animation;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.stf.bj.app.game.bj.Spot;
import com.stf.bj.app.game.players.Coach;
import com.stf.bj.app.game.server.Card;
import com.stf.bj.app.settings.AppSettings;
import com.stf.bj.app.settings.AnimationSettings.CoachSetting;
import com.stf.bj.app.settings.AnimationSettings.FlipDealerCard;

public class AnimationManager {

	private final AppSettings settings;
	private final List<CardSprite> dealer = new ArrayList<CardSprite>();
	private final List<CardSprite> discard = new ArrayList<CardSprite>();
	private final List<SpotSprite> players;
	Batch batch;
	private static final float DEALER_CARD_SPACING_X = 80f;
	private static final float SPOT_Y = 200f;
	private static final float EDGE_SPOT_Y = 300f;
	public static final Vector2 DECK_ANCHOR = new Vector2(900f, 600f);
	public static final Vector2 DISCARD_ANCHOR = new Vector2(100f, 600f);
	public static final Vector2 PENETRATION_ANCHOR = new Vector2(200f, 600f);
	public static final Vector2 COACH_ANCHOR = new Vector2(200f, 550f);
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
	private String displayString = "";
	private Card dealerUpCard;
	private boolean secondDealerCardIsFaceDown;
	public String debugText = "";
	private String penetration;
	private Coach coach;

	public AnimationManager(Batch batch, List<Spot> spots, AppSettings settings, Skin skin) {
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
			if (s.isHuman() && settings.getAnimationSettings().getCoachSetting() != CoachSetting.SYSTEM_OUT) {
				coach = s.getHuman().getCoach();
			}
		}
		font = skin.getFont("medium");
		bigFont = skin.getFont("title");

	}

	public void render() {

		batch.begin();
		bigFont.draw(batch, displayString, MIDDLE_TEXT_ANCHOR.x, MIDDLE_TEXT_ANCHOR.y);
		font.draw(batch, debugText, 10, 718);
		font.draw(batch, penetration, PENETRATION_ANCHOR.x, PENETRATION_ANCHOR.y);
		if (coach != null)
			bigFont.draw(batch, coach.getMessage(), COACH_ANCHOR.x, COACH_ANCHOR.y, 900f, Align.topLeft, true);
		for (CardSprite cs : dealer) {
			cs.tick(batch);
		}

		for (CardSprite cs : discard) {
			cs.tick(batch);
		}

		for (SpotSprite ss : players) {
			ss.tick(batch, font);

		}

		batch.end();
	}

	public void addDealerCard(Card card) {
		int dealerCards = dealer.size();

		// When we actually get the second card, we quit early as a new card isn't added
		if (dealerCards == 2 && secondDealerCardIsFaceDown) {
			secondDealerCardIsFaceDown = false;
			setFaceDownDealerCard(1, card);
			return;
		}

		// When we get the hidden second card, flip the first card if it's hidden
		if (dealerCards == 1) {
			secondDealerCardIsFaceDown = true;
			if (settings.getAnimationSettings().getFlipDealerCard() == FlipDealerCard.AFTER_SECOND_CARD) {
				setFaceDownDealerCard(0, dealerUpCard); // TODO delay this
			}
		}

		if (dealerCards == 0
				&& settings.getAnimationSettings().getFlipDealerCard() == FlipDealerCard.AFTER_SECOND_CARD) {
			dealerUpCard = card;
			card = null;
		}

		addNewDealerCard(card);
	}

	private void addNewDealerCard(Card card) {
		CardSprite cs = new CardSprite(card, DECK_ANCHOR, false);
		cs.setDestination(getDealerCardLocation());
		dealer.add(cs);
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

	public void discardSpot(SpotSprite ss, int delayBeforeMoving) {
		for (HandSprite hs : ss.getHandSprites()) {
			discardHand(hs, delayBeforeMoving);
		}
	}

	public void discardHand(HandSprite hs, int delayBeforeMoving) { // Called after a bust, and general clean up phase
		hs.setDestinationForAllCards(DISCARD_ANCHOR, delayBeforeMoving);
		discard.addAll(hs.getCardSprites());
		hs.clear();
	}

	public void discardDealer(int delayBeforeMoving) {
		for (CardSprite cs : dealer) {
			cs.setDestination(DISCARD_ANCHOR);
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

	public void discardAllSprites(int delayBeforeMoving) {
		for (SpotSprite ss : players) {
			discardSpot(ss, delayBeforeMoving);
		}
		discardDealer(delayBeforeMoving);
	}

	public void setDisplayString(String s) {
		displayString = s;
	}

	public void updateHandPlacements(SpotSprite spotSprite, int delayBeforeMoving) {
		spotSprite.updateHandAnchors(delayBeforeMoving);
	}

	public void setPenetrationString(String penetration) {
		this.penetration = penetration;
	}

}
