package com.stf.bj.app.mainMenu;

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
import com.stf.bj.app.utils.TextureActor;

public class MainMenu extends Stage {

	private TextButton startGameButton, settingsButton, strategyButton, statisticsButton;

	public MainMenu(final StfBlackjack app) {
		super(app.getSvp());
		Skin skin = app.getSkin();
		addActor(new TextureActor("menuBackground.png"));

		Table table = new Table();
		table.setFillParent(true);
		addActor(table);

		Label title = new Label("Suit and Tie Financial", skin, "title");
		Label subTitle = new Label("Blackjack Trainer", skin, "title");
		title.setScale(5);
		table.add(title).pad(80f, 80f,0f, 80f);
		table.row();
		table.add(subTitle);
		table.top().left();
		
		table.row();
		startGameButton = new TextButton("Start Game", skin, "big");
		table.add(startGameButton).center().pad(60f, 200f, 10f, 200f).fill();
		
		table.row();
		settingsButton = new TextButton("Settings", skin, "big");
		table.add(settingsButton).center().pad(10f, 200f, 10f, 200f).fill();
		
		table.row();
		strategyButton = new TextButton("Strategy", skin, "big");
		table.add(strategyButton).center().pad(10f, 200f, 10f, 200f).fill();
		
		table.row();
		statisticsButton = new TextButton("Statistics", skin, "big");
		table.add(statisticsButton).center().pad(10f, 200f, 10f, 200f).fill();

		startGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				app.switchToScreen(Screens.GAME);
			}

		});

		settingsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				app.switchToScreen(Screens.SETTINGS);
			}

		});

		strategyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				app.switchToScreen(Screens.STRATEGY);
			}

		});
	}
}
