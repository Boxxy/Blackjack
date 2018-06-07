package com.stf.bj.app.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Sprite {
	private Texture texture;
	private Vector2 location = new Vector2(0, 0);
	private Vector2 destination = new Vector2(0, 0);

	private float velocity = 12f;
	private boolean visible, moving;

	public Sprite() {
	}

	public void setTexture(String path) {
		texture = new Texture(Gdx.files.internal(path));
	}

	public void setVisible(boolean v) {
		visible = v;
	}

	public void setLocation(Vector2 v) {
		location.x = v.x;
		location.y = v.y;
	}

	public Vector2 getLocation() {
		return location;
	}

	public void setDestination(Vector2 v) {
		destination.x = v.x;
		destination.y = v.y;
		moving = true;
	}

	public void setVelocity(float v) {
		velocity = v;
	}

	public boolean tick(SpriteBatch batch, boolean noUpdates) {

		boolean stoppedMoving = false;
		if (!noUpdates) {
			if (!visible)
				return false;
			if (moving) {
				stoppedMoving = updateLocation();
			}
		}
		render(batch);
		return stoppedMoving;
	}

	protected void render(SpriteBatch batch) {
		batch.draw(texture, location.x, location.y);
	}

	protected boolean updateLocation() {
		float distance = location.dst(destination);
		if (distance < velocity) {
			location.x = destination.x;
			location.y = destination.y;
			moving = false;
			return true;
		}
		Vector2 wayToGo = destination.cpy();
		wayToGo.sub(location);
		wayToGo.limit(velocity);
		location.add(wayToGo);
		return false;
	}
}
