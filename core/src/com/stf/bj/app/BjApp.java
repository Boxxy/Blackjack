package com.stf.bj.app;

import java.util.ArrayList;
import java.util.List;

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
import com.stf.bj.app.sprites.SpriteManager;
import com.stf.bj.app.table.Ranks;
import com.stf.bj.app.table.TableRules;

public class BjApp extends ApplicationAdapter {
	SpriteBatch batch;
	private OrthographicCamera camera;
	private BjManager bjManager;
	SpriteManager spriteManager;
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
		bjManager = new BjManager(TableRules.STANDARD_MYSTIC_RULES());
		bjManager.addPlayer(0, PlayerType.BASIC_BOT);
		bjManager.addPlayer(1, PlayerType.BASIC_COUNTING_BOT);
		bjManager.addPlayer(2, PlayerType.BASIC_COUNTING_BOT);
		bjManager.addPlayer(3, PlayerType.HUMAN);
		bjManager.addPlayer(4, PlayerType.BASIC_INDEX_COUNTING_BOT);
		bjManager.addPlayer(5, PlayerType.BASIC_INDEX_COUNTING_BOT);
		bjManager.openTable();
		spriteManager = new SpriteManager(batch, bjManager.getSpots(), AnimationSettings.getNew()); //Player manager is a at different level, should we really do it this way? TODO
		
		List<Ranks> r = new ArrayList<Ranks>();
		r.add(Ranks.ACE);
		r.add(Ranks.ACE);
		r.add(Ranks.TEN);
		r.add(Ranks.TEN);
		r.add(Ranks.ACE);
		r.add(Ranks.TEN);
		//bjManager.shadyShit(r);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.1f, .8f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		bjManager.input(handleInput());
		
		if (!spriteManager.busy()) { //TODO i don't like this set up for processing. Graphics, input, and game logic should be less coupled.
			if (!bjManager.processEvents(spriteManager)) {
				bjManager.tick(spriteManager);
			}
		}
		spriteManager.render(bjManager.getPlayerManager());
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
		return -1;
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
