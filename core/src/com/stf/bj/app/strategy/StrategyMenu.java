package com.stf.bj.app.strategy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.stf.bj.app.StfBlackjack;
import com.stf.bj.app.StfBlackjack.Screens;
import com.stf.bj.app.strategy.FullStrategy.StrategyTypes;

public class StrategyMenu extends Stage {

	private TextButton backButton;
	private TextButton copyButton;
	private Skin skin;
	private final StfBlackjack app;
	private Table table;
	private ButtonGroup<Button> countButtonGroup;
	private int currentCount;

	private TacticButton[][] hardPlays = new TacticButton[StrategyTypes.HARD.getRows()][10];
	private TacticButton[][] softPlays = new TacticButton[StrategyTypes.SOFT.getRows()][10];
	private TacticButton[][] pairPlays = new TacticButton[StrategyTypes.PAIRS.getRows()][10];

	public StrategyMenu(StfBlackjack app) {
		super(app.getSvp());
		this.skin = app.getSkin();
		this.app = app;

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

		copyButton = new TextButton("Copy From", skin, "big");

		copyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				copy();
			}

		});

		table.add(backButton).left();
		table.top().left();

		Table countButtonTable = new Table();
		countButtonGroup = new ButtonGroup<Button>();

		for (int count = -9; count < 10; count++) {
			final TextButton countButton = new TextButton(String.valueOf(count), skin, "small-toggle-green");
			countButton.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					saveButtonsToCurrentCount();
					loadCount(countButton.getText().toString());
				}

			});
			countButtonGroup.add(countButton);
			countButtonTable.add(countButton).right();
		}

		table.add(countButtonTable);

		table.row();

		Table otherStrategies = new Table();
		otherStrategies.add(generateStrategyTable(StrategyTypes.SOFT));
		otherStrategies.row();
		otherStrategies.add(generateStrategyTable(StrategyTypes.PAIRS));

		table.add(generateStrategyTable(StrategyTypes.HARD));
		table.add(otherStrategies);

		Table otherButtons = new Table();
		otherButtons.add(copyButton);
		table.add(otherButtons);

		countButtonGroup.setMaxCheckCount(1);
		countButtonGroup.setMinCheckCount(1);
		countButtonGroup.setChecked("0");
		loadCount("0");
	}

	private Table generateStrategyTable(StrategyTypes type) {
		Table table = new Table();
		// table.add(new Label(type.getTitle(), skin, "white")).colspan(11);
		// table.row();
		table.add();
		for (int column = 0; column < 10; column++) {
			String display;
			if (column == 9) {
				display = "A";
			} else if (column == 8) {
				display = "T";
			} else {
				display = String.valueOf(column + 2);
			}
			table.add(new Label(display, skin, "white"));
		}
		table.row();
		for (int row = 0; row < type.getRows(); row++) {

			table.add(new Label(getRowLabel(type, row), skin, "white"));
			TacticButton aceButton = null;
			for (int column = 0; column < 10; column++) {
				TacticButton button = new TacticButton(skin, "small");
				switch (type) {
				case HARD:
					this.hardPlays[row][column] = button;
					break;
				case PAIRS:
					this.pairPlays[row][column] = button;
					break;
				case SOFT:
					this.softPlays[row][column] = button;
					break;
				default:
					throw new IllegalStateException();
				}
				if (column == 0) {
					aceButton = button;
				} else {
					table.add(button).uniform();
				}
			}
			table.add(aceButton).uniform();
			table.row();
		}

		return table;
	}

	private String getRowLabel(StrategyTypes type, int row) {
		switch (type) {
		case HARD:
			return String.valueOf(row + 3);
		case PAIRS:
			if (row == 0) {
				return "AA";
			} else if (row == 9) {
				return "TT";
			} else {
				return String.valueOf(row + 1) + String.valueOf(row + 1);
			}
		case SOFT:
			if (row == 0) {
				return "AA";
			} else if (row == 9) {
				return "AT";
			} else {
				return "A" + String.valueOf(row + 1);
			}
		default:
			throw new IllegalStateException();

		}
	}

	public void draw() {
		Color c = skin.getColor("background");
		Gdx.gl.glClearColor(c.r, c.g, c.b, c.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.draw();
	}

	private void back() {
		saveButtonsToCurrentCount();
		try {
			app.getPreferences().flush();
		} finally {
		}

		app.switchToScreen(Screens.MAIN_MENU);
	}

	private void loadCount(String s) {
		System.out.println("Loading strategy menu to " + s);
		countButtonGroup.setChecked(s);
		currentCount = Integer.valueOf(s);

		for (int row = 0; row < StrategyTypes.HARD.getRows(); row++) {
			for (int column = 0; column < 10; column++) {
				hardPlays[row][column]
						.setTactic(app.getStrategy().getTactic(currentCount, StrategyTypes.HARD, row, column));
			}
		}
		for (int row = 0; row < StrategyTypes.SOFT.getRows(); row++) {
			for (int column = 0; column < 10; column++) {
				softPlays[row][column]
						.setTactic(app.getStrategy().getTactic(currentCount, StrategyTypes.SOFT, row, column));
			}
		}
		for (int row = 0; row < StrategyTypes.PAIRS.getRows(); row++) {
			for (int column = 0; column < 10; column++) {
				pairPlays[row][column]
						.setTactic(app.getStrategy().getTactic(currentCount, StrategyTypes.PAIRS, row, column));
			}
		}

		if (currentCount == 0) {
			copyButton.setText("Copy to all counts");
		} else {
			copyButton.setText("Copy from " + countToCopyFrom());
		}

	}

	private int countToCopyFrom() {
		if (currentCount > 0) {
			return currentCount - 1;
		} else {
			return currentCount + 1;
		}
	}

	private void saveButtonsToCurrentCount() {
		System.out.println("Saving strategy menu from " + currentCount);

		for (int row = 0; row < StrategyTypes.HARD.getRows(); row++) {
			for (int column = 0; column < 10; column++) {
				app.getStrategy().setTactic(currentCount, StrategyTypes.HARD, row, column,
						hardPlays[row][column].toInt());
			}
		}
		for (int row = 0; row < StrategyTypes.SOFT.getRows(); row++) {
			for (int column = 0; column < 10; column++) {
				app.getStrategy().setTactic(currentCount, StrategyTypes.SOFT, row, column,
						softPlays[row][column].toInt());
			}
		}
		for (int row = 0; row < StrategyTypes.PAIRS.getRows(); row++) {
			for (int column = 0; column < 10; column++) {
				app.getStrategy().setTactic(currentCount, StrategyTypes.PAIRS, row, column,
						pairPlays[row][column].toInt());
			}
		}

		app.getStrategy().save(app.getPreferences(), currentCount);

	}

	private void copy() {

		saveButtonsToCurrentCount();
		if (currentCount == 0) {
			for (int countTo = -9; countTo < 10; countTo++) {
				app.getStrategy().copy(0, countTo);
				app.getStrategy().save(app.getPreferences(), countTo);
			}
		} else {
			app.getStrategy().copy(countToCopyFrom(), currentCount);
			loadCount(String.valueOf(currentCount));
		}
	}
}
