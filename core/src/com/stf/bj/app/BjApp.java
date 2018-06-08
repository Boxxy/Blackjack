package com.stf.bj.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.stf.bj.app.bj.BjManager;
import com.stf.bj.app.players.PlayerType;
import com.stf.bj.app.sprites.AnimationSettings;
import com.stf.bj.app.sprites.CardSprite;
import com.stf.bj.app.sprites.AnimationManager;
import com.stf.bj.app.table.Ranks;
import com.stf.bj.app.table.TableRules;

public class BjApp extends ApplicationAdapter {
	SpriteBatch batch;
	private OrthographicCamera camera;
	private BjManager bjManager;
	AnimationManager spriteManager;
	boolean everyOtherClick;
	CardSprite lastSprite;
	boolean mouseDown;
	int lastRank = 0;
	int lastSuit = 0;

	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 720);
		camera.update();
		createBlackjack();
	}

	private void createBlackjack() {
		AppSettings settings = AppSettings.getRandom();
		bjManager = new BjManager(settings);
		Random r = new Random(System.currentTimeMillis());
		int playerSpot = r.nextInt(settings.getTableRules().getSpots());
		bjManager.addPlayer(playerSpot, PlayerType.HUMAN);
		for(int spot = 0; spot < settings.getTableRules().getSpots(); spot++) {
			if(spot == playerSpot) {
				continue;
			}
			bjManager.addPlayer(spot, PlayerType.getRandom());
		}
		bjManager.openTable();

		// Player manager is a at different level, should we really do it this way? TODO
		spriteManager = new AnimationManager(batch, bjManager.getSpots(), settings);

		List<Ranks> ranks = new ArrayList<Ranks>();
		ranks.add(Ranks.ACE);
		ranks.add(Ranks.ACE);
		ranks.add(Ranks.TEN);
		ranks.add(Ranks.TEN);
		ranks.add(Ranks.ACE);
		ranks.add(Ranks.TEN);
		//bjManager.shadyShit(ranks);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.1f, .8f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		bjManager.input(handleInput());
		bjManager.tick(spriteManager);
		spriteManager.render();
	}

	private int handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			return Input.Keys.Q;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
			return Input.Keys.W;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			return Input.Keys.A;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			return Input.Keys.S;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			return Input.Keys.E;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			return Input.Keys.D;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			return Input.Keys.R;
		}
		return -1;
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
