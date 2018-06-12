package com.stf.bj.app.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stf.bj.app.game.bj.BjManager;
import com.stf.bj.app.game.players.PlayerType;
import com.stf.bj.app.game.server.Ranks;
import com.stf.bj.app.game.server.TableRules;
import com.stf.bj.app.game.sprites.AnimationManager;
import com.stf.bj.app.game.sprites.AnimationSettings;
import com.stf.bj.app.game.sprites.CardSprite;

public class BjStage extends Stage {
	private BjManager bjManager;
	AnimationManager spriteManager;
	boolean everyOtherClick;
	CardSprite lastSprite;
	boolean mouseDown;
	int lastRank = 0;
	int lastSuit = 0;
	Random r;

	public BjStage(ScreenViewport screenViewport) {
		super(screenViewport);
		createBlackjack();
	}

	private void createBlackjack() {
		r = new Random(System.currentTimeMillis());
		AppSettings settings = AppSettings.getHochunkPratice();
		bjManager = new BjManager(settings);
		int playerSpot = r.nextInt(settings.getTableRules().getSpots());
		bjManager.addPlayer(playerSpot, PlayerType.HUMAN, null);
		for(int spot = 0; spot < settings.getTableRules().getSpots(); spot++) {
			if(spot == playerSpot) {
				continue;
			}
			bjManager.addPlayer(spot, PlayerType.REALISTIC_BOT, r);
		}
		bjManager.openTable();

		// Player manager is a at different level, should we really do it this way? TODO
		spriteManager = new AnimationManager(getBatch(), bjManager.getSpots(), settings);

		List<Ranks> ranks = new ArrayList<Ranks>();
		ranks.add(Ranks.ACE);
		ranks.add(Ranks.ACE);
		ranks.add(Ranks.TEN);
		ranks.add(Ranks.TEN);
		ranks.add(Ranks.ACE);
		ranks.add(Ranks.TEN);
		//bjManager.shadyShit(ranks);
	}
	
	public void act(float delta) {
		super.act(delta);
		bjManager.input(handleInput());
		bjManager.tick(spriteManager);
	}

	public void draw() {
		super.draw();

		Gdx.gl.glClearColor(.1f, .8f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
}
