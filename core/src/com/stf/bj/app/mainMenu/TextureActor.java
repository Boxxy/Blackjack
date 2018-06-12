package com.stf.bj.app.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextureActor extends Actor {

	private final TextureRegion textureRegion;

	public TextureActor(String path) {
		super();
		textureRegion = new TextureRegion(new Texture(path));
		setHeight(textureRegion.getRegionHeight());
		setWidth(textureRegion.getRegionWidth());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
	}
}
