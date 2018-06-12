package com.stf.bj.app.mainMenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.stf.bj.app.StfBlackjack;
import com.stf.bj.app.StfBlackjack.Screens;

public class MainMenu extends Stage {

	private Table table;
	private Skin skin;
	private TextButton startGameButton;
	private BitmapFont font;

	public MainMenu(final StfBlackjack myGame, ScreenViewport svp) {
		super(svp);
		addActor(new TextureActor("menuBackground.png"));

		table = new Table();
		table.setFillParent(true);
		addActor(table);

		skin = new Skin();
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().scale(3f);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		skin.add("default", font);
		LabelStyle labelStyle = new LabelStyle();
		skin.add("default", labelStyle);
		labelStyle.font = skin.getFont("default");

		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		startGameButton = new TextButton("Start Game", skin);
		Label title = new Label("Suit and Tie Financial", skin);
		Label subTitle = new Label("Blackjack Trainer", skin);
		title.setScale(5);
		table.add(title);
		table.row();
		table.add(subTitle);
		table.row();
		table.add(startGameButton).left().pad(10f, 50f, 10f, 50f).fill();
		table.row();
		table.add(new TextButton("Settings", skin)).left().pad(10f, 50f, 10f, 50f).fill();
		table.row();
		table.add(new TextButton("Statistics", skin)).left().pad(10f, 50f, 10f, 50f).fill();
		table.left();

		startGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				myGame.switchToScreen(Screens.GAME);
			}

		});
	}
}
