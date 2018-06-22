package com.stf.bj.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stf.bj.app.game.GameStage;
import com.stf.bj.app.mainMenu.MainMenu;
import com.stf.bj.app.settings.AppSettings;
import com.stf.bj.app.settings.SettingsMenu;

public class StfBlackjack extends Game {

	private Stage bjStage, mainMenuStage, settingsStage, stage;
	ScreenViewport svp;
	private Skin skin;
	private Preferences preferences;
	private AppSettings settings;

	public enum Screens {
		MAIN_MENU, GAME, SETTINGS;
	}

	@Override
	public void create() {
		svp = new ScreenViewport();
		skin = new Skin(Gdx.files.internal("sgx/skin/sgx-ui.json"));
		preferences = Gdx.app.getPreferences("defaultPreferences");
		settings = new AppSettings(preferences);
		switchToScreen(Screens.MAIN_MENU);
	}

	@Override
	public void resize(int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render() {
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
		switch (mainMenu) {
		case GAME:
			bjStage = new GameStage(this);
			stageToSet = bjStage;
			break;
		case MAIN_MENU:
			if (mainMenuStage == null) {
				mainMenuStage = new MainMenu(this);
			}
			stageToSet = mainMenuStage;
			break;
		case SETTINGS:
			if (settingsStage == null) {
				settingsStage = new SettingsMenu(this);
			}
			stageToSet = settingsStage;
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
	
	public Skin getSkin() {
		return skin;
	}
	
	public ScreenViewport getSvp() {
		return svp;
	}
	
	public Preferences getPreferences() {
		return preferences;
	}
	
	public AppSettings getSettings(){
		return settings;
	}
}
