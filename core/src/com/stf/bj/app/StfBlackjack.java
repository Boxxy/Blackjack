package com.stf.bj.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stf.bj.app.game.GameStage;
import com.stf.bj.app.mainMenu.MainMenu;

public class StfBlackjack extends Game{

	private Stage bjStage;
	private Stage mainMenuStage;
	private Stage stage;
	ScreenViewport svp;
	private Skin skin;
	
	public enum Screens{
		MAIN_MENU, GAME, SETTINGS;
	}
	
	@Override
	public void create() {
		svp = new ScreenViewport();
		skin = new Skin(Gdx.files.internal("sgx/skin/sgx-ui.json"));
		switchToScreen(Screens.MAIN_MENU);
	}

	@Override
	public void resize (int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
	public void switchToScreen(Screens mainMenu) {
		Stage stageToSet = null;
		switch(mainMenu) {
		case GAME:
			if(bjStage == null) {
				bjStage = new GameStage(svp, skin);
			}
			stageToSet = bjStage;
			break;
		case MAIN_MENU:
			if(mainMenuStage == null) {
				mainMenuStage = new MainMenu(this, svp, skin);
			}
			stageToSet = mainMenuStage;
			break;
		case SETTINGS:
			break;
		default:
			break;
		
		}
		switchToStage(stageToSet);
	}

	private void switchToStage(Stage stage) {
		this.stage = stage;
		Gdx.input.setInputProcessor(stage);
	}
}
