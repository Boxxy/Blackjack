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
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stf.bj.app.StfBlackjack;
import com.stf.bj.app.StfBlackjack.Screens;
import com.stf.bj.app.settings.settings.BotPlayers;
import com.stf.bj.app.settings.settings.BotSpeed;
import com.stf.bj.app.settings.settings.CardSpeed;
import com.stf.bj.app.settings.settings.DealerSpeed;
import com.stf.bj.app.settings.settings.Decks;
import com.stf.bj.app.settings.settings.DoubleAfterSplit;
import com.stf.bj.app.settings.settings.HumanSpot;
import com.stf.bj.app.settings.settings.NumberOfSplits;
import com.stf.bj.app.settings.settings.NumberOfSpots;
import com.stf.bj.app.settings.settings.ResplitAces;
import com.stf.bj.app.settings.settings.Setting;
import com.stf.bj.app.settings.settings.Surrender;
import com.stf.bj.app.settings.settings.TableMax;
import com.stf.bj.app.settings.settings.TableMin;
import com.stf.bj.app.utils.TextureActor;

public class SettingsMenu extends Stage {

	private TextButton backButton;
	private Skin skin;
	private final StfBlackjack app;
	private Table table;
	private AppSettings settings;

	public SettingsMenu(StfBlackjack app) {
		super(app.getSvp());
		this.skin = app.getSkin();
		this.app = app;
		settings = app.getSettings();

		table = new Table();
		table.setFillParent(true);
		addActor(table);

		backButton = new TextButton("Back", skin, "big");

		backButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				back();
			}

		});

		table.add(backButton).left();
		table.top().left();

		table.row();
		addSetting(settings.getNumberOfSpotsSetting());
		settings.getNumberOfSpotsSetting().addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				numberOfSpotsChanged();
			}
		});
		addSetting(settings.getDeckSetting());

		table.row();
		addSetting(settings.getHumanSpotSetting());
		settings.getHumanSpotSetting().addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				numberOfSpotsChanged();
			}
		});
		addSetting(settings.getDealerSoft17Setting());

		table.row();

		addSetting(settings.getDoubleAfterSplitSetting());
		addSetting(settings.getNumberOfSplitsSetting());

		table.row();
		addSetting(settings.getResplitAcesSetting());
		addSetting(settings.getSurrenderSetting());

		table.row();
		addSetting(settings.getPayoutBlackjackSetting());
		addSetting(settings.getFlipDealerCardSetting());

		table.row();
		addSetting(settings.getEvenMoneyPaymentSetting());
		addSetting(settings.getDoubleCardOrientationSetting());

		table.row();
		addSetting(settings.getBotPlayersSetting());
		addSetting(settings.getBotSpeedSetting());

		table.row();
		addSetting(settings.getCardSpeedSetting());
		addSetting(settings.getDealerSpeedSetting());

		table.row();
		addSetting(settings.getTableMinSetting());
		addSetting(settings.getTableMaxSetting());

		table.row();
		addSetting(settings.getPenetrationSetting());
		addSetting(settings.getCoachSetting());

	}

	private void addSetting(Setting setting) {
		table.add(setting.generateTable(skin, "white", "small-toggle-green")).center().expandX();
	}

	public void draw() {
		Color c = skin.getColor("background");
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.draw();
	}

	private void back() {
		settings.updateAndSave();
		try {
			app.getPreferences().flush();
		} catch (GdxRuntimeException e) {

		}
		app.switchToScreen(Screens.MAIN_MENU);
	}

	private void numberOfSpotsChanged() {
		if (settings.getHumanSpotInt() >= settings.getNumberOfSpots()) {
			settings.getHumanSpotSetting().reset();
		}
	}
}
