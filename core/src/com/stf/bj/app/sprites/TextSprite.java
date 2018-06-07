package com.stf.bj.app.sprites;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TextSprite extends Sprite {

	private String text;
	private final BitmapFont font;

	// TODO consider making this a return of a static constant method in Sprite
	public TextSprite(BitmapFont font) {
		super();
		setVisible(true);
		setLocation(new Vector2(0, 0));
		setVelocity(1000f);
		this.font = font;
	}

	public void setText(String text) {
		this.text = text;
	}

	protected void render(SpriteBatch batch) {
		font.draw(batch, text, getLocation().x, getLocation().y);
	}

}
