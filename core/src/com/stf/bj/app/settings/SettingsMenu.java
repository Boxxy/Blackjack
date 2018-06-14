package com.stf.bj.app.settings;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stf.bj.app.StfBlackjack;
import com.stf.bj.app.StfBlackjack.Screens;
import com.stf.bj.app.settings.settings.Decks;
import com.stf.bj.app.settings.settings.HumanSpot;
import com.stf.bj.app.settings.settings.NewAppSettings;
import com.stf.bj.app.settings.settings.Setting;
import com.stf.bj.app.utils.TextureActor;

public class SettingsMenu extends Stage {

	private TextButton backButton;
	private Skin skin;
	private final StfBlackjack app;
	private Table table;
	private NewAppSettings settings;

	public SettingsMenu(StfBlackjack app) {
		super(app.getSvp());
		this.skin = app.getSkin();
		this.app = app;
		settings = app.getSettings();

		table = new Table();
		table.setFillParent(true);
		addActor(table);

		backButton = new TextButton("Back", skin, "big");
		table.add(backButton).left();
		table.top().left();

		addSetting(settings.getHumanSpotSetting());

		addSetting(settings.getDeckSetting());

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back();
			}

		});
	}

	private void addSetting(Setting setting) {
		table.row();
		table.add(setting.generateTable(skin, "white", "small-toggle-green"));
	}

	public void draw() {
		Color c = skin.getColor("background");
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.draw();
	}

	private void back() {
		settings.updateAndSave();
		app.getPreferences().flush();
		app.switchToScreen(Screens.MAIN_MENU);
	}
}
