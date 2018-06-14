package com.stf.bj.app.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stf.bj.app.StfBlackjack;
import com.stf.bj.app.StfBlackjack.Screens;
import com.stf.bj.app.game.animation.AnimationManager;
import com.stf.bj.app.game.animation.CardSprite;
import com.stf.bj.app.game.bj.BjManager;
import com.stf.bj.app.game.players.PlayerType;
import com.stf.bj.app.game.server.Ranks;
import com.stf.bj.app.settings.AppSettings;
import com.stf.bj.app.settings.settings.HumanSpot.HumanSpotSetting;
import com.stf.bj.app.utils.TextureActor;

public class GameStage extends Stage {
	private BjManager bjManager;
	AnimationManager spriteManager;
	boolean everyOtherClick;
	CardSprite lastSprite;
	boolean mouseDown;
	int lastRank = 0;
	int lastSuit = 0;
	Random r;
	TextButton backButton;
	private final StfBlackjack app;

	public GameStage(StfBlackjack app) {
		super(app.getSvp());
		this.app = app;
		Skin skin = app.getSkin();
		addActor(new TextureActor("gameBackground.png"));

		Table table = new Table();
		table.setFillParent(true);
		addActor(table);

		backButton = new TextButton("Back", skin, "big");
		table.add(backButton);
		table.top().left();

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back();
			}

		});

		createBlackjack(skin);
	}

	private void back() {
		app.switchToScreen(Screens.MAIN_MENU);
	}

	private void createBlackjack(Skin skin) {
		r = new Random(System.currentTimeMillis());
		AppSettings settings = AppSettings.getHochunkPratice();
		bjManager = new BjManager(settings);

		int playerSpot = getPlayerSpotFromSettings(r, app.getSettings().getHumanSpotValue(),
				settings.getTableRules().getSpots());
		if (playerSpot >= 0) {
			bjManager.addPlayer(playerSpot, PlayerType.HUMAN, null);
		}
		for (int spot = 0; spot < settings.getTableRules().getSpots(); spot++) {
			if (spot == playerSpot) {
				continue;
			}
			bjManager.addPlayer(spot, PlayerType.REALISTIC_BOT, r);
		}
		bjManager.openTable();

		// Player manager is a at different level, should we really do it this way? TODO
		spriteManager = new AnimationManager(getBatch(), bjManager.getSpots(), settings, skin);

		List<Ranks> ranks = new ArrayList<Ranks>();
		ranks.add(Ranks.ACE);
		ranks.add(Ranks.ACE);
		ranks.add(Ranks.TEN);
		ranks.add(Ranks.TEN);
		ranks.add(Ranks.ACE);
		ranks.add(Ranks.TEN);
		// bjManager.shadyShit(ranks);
	}

	private int getPlayerSpotFromSettings(Random r, HumanSpotSetting humanSpotSetting, int spots) {
		switch (humanSpotSetting) {
		case FIVE:
			return 4;
		case FOUR:
			return 3;
		case NONE:
			return -1;
		case ONE:
			return 0;
		case RANDOM:
			return r.nextInt(spots);
		case SIX:
			return 5;
		case THREE:
			return 2;
		case TWO:
			return 1;
		default:
			throw new IllegalStateException();
		}
	}

	public void act(float delta) {
		super.act(delta);
		bjManager.input(handleInput());
		bjManager.tick(spriteManager);
	}

	public void draw() {
		super.draw();
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
