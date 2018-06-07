package com.stf.bj.app.sprites;

import com.badlogic.gdx.math.Vector2;
import com.stf.bj.app.table.Card;

public class CardSprite extends Sprite{

	public CardSprite(Card card, Vector2 location) {
		super();
		setTextureFromCard(card);
		setLocation(location);
		setVisible(true);
		setVelocity(12f);
	}

	private String getPathFromCard(Card c) {
		String s = "Playing Cards/";
		if (c == null)
			return s + "back.png";

		switch (c.getSuit()) {
		case CLUBS:
			s += "c";
			break;
		case DIAMONDS:
			s += "d";
			break;
		case HEARTS:
			s += "h";
			break;
		case SPADES:
			s += "s";
			break;
		default:
			throw new IllegalStateException();
		}

		switch (c.getRank()) {
		case ACE:
			s += "a";
			break;
		case EIGHT:
			s += "8";
			break;
		case FIVE:
			s += "5";
			break;
		case FOUR:
			s += "4";
			break;
		case JACK:
			s += "j";
			break;
		case KING:
			s += "k";
			break;
		case NINE:
			s += "9";
			break;
		case QUEEN:
			s += "q";
			break;
		case SEVEN:
			s += "7";
			break;
		case SIX:
			s += "6";
			break;
		case TEN:
			s += "t";
			break;
		case THREE:
			s += "3";
			break;
		case TWO:
			s += "2";
			break;
		default:
			throw new IllegalStateException();
		}

		return s += ".png";
	}

	public void setTextureFromCard(Card card) {
		setTexture(getPathFromCard(card));
	}

}
