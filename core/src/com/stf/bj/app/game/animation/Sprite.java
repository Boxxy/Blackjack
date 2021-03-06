package com.stf.bj.app.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Sprite {
	private TextureRegion texture;
	private Vector2 location = new Vector2(0, 0);
	private Vector2 destination = new Vector2(0, 0);
	private Vector2 destinationToAdd = null;

	private float velocity = 12f;
	private boolean visible, moving;
	private float rotation = 0;
	private float targetRotation = 0;
	private float rotationalVelocity;

	private int delay;
	private int delayToAdd;

	public void setTexture(String path) {
		texture = new TextureRegion(new Texture(Gdx.files.internal(path)));
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
		setDestination(v, 0f);
	}

	public void setDestination(Vector2 v, float targetRotation) {
		if (moving) {
			destinationToAdd = v;
			return;
		}
		destination.x = v.x;
		destination.y = v.y;
		moving = true;
		if (velocity <= 0f) {
			location = destination;
			rotation = targetRotation;
			rotationalVelocity = 0;
			moving = false;
			return;
		}

		this.targetRotation = targetRotation;
		float rotationalDifference = targetRotation - rotation;
		if (rotationalDifference != 0) {
			float distance = location.dst(destination);
			float time = Math.abs(distance / velocity);
			rotationalVelocity = rotationalDifference / (time - 1f);
		}
	}

	public void setVelocity(float v) {
		velocity = v;
	}

	public void tick(Batch batch) {
		if (!visible) {
			return;
		}
		if (moving) {
			updateLocation();
		}
		render(batch);
	}

	protected void render(Batch batch) {
		batch.draw(texture, location.x, location.y, 0f, 0f, texture.getRegionWidth(), texture.getRegionHeight(), 1f, 1f,
				rotation);
	}

	protected boolean updateLocation() {
		if (delay > 0) {
			delay--;
			return false;
		}
		if (delayToAdd > 0) {
			delayToAdd--;
		}
		float distance = location.dst(destination);
		if (distance <= velocity) {
			location.x = destination.x;
			location.y = destination.y;
			rotation = targetRotation;
			moving = false;
			
			if (delayToAdd > 0) {
				delay = delayToAdd;
				delayToAdd = 0;
			}
			if (destinationToAdd != null) {
				setDestination(destinationToAdd);
				destinationToAdd = null;
			}
			return true;
		}
		Vector2 wayToGo = destination.cpy();
		wayToGo.sub(location);
		wayToGo.limit(velocity);
		location.add(wayToGo);

		float rotationalDifference = targetRotation - rotation;
		if (Math.abs(rotationalDifference) < Math.abs(rotationalVelocity)) {
			rotation = targetRotation;
		} else {
			rotation += rotationalVelocity;
		}

		return false;
	}

	public void addDelay(int delay) {
		if (moving) {
			delayToAdd = delay;
		} else {
			this.delay = delay;
		}
	}
}
