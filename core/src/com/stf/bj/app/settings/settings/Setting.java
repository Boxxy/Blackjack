package com.stf.bj.app.settings.settings;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public abstract class Setting {

	private int index;
	private final int defaultIndex;
	private final List<String> optionNames;
	private final String name;
	private ButtonGroup<Button> group;
	private final Preferences preferences;
	

	public Setting(Preferences preferences, String name, int defaultValue) {
		this.name = name;
		this.defaultIndex = defaultValue;
		optionNames = new ArrayList<String>();
		this.preferences = preferences;
		loadFromPreferences();
	}

	public void updateAndSave() {
		index = group.getCheckedIndex();
		preferences.putInteger(name, index);
	}

	public void loadFromPreferences() {
		index = preferences.getInteger(name, defaultIndex);
	}

	public void addOption(String name, int value) {
		optionNames.add(name);
	}

	public int getValue() {
		return index;
	}

	//Should this be some kind of utility class method?
	public Table generateTable(Skin skin, String labelType, String buttonType) { 
		Table table = new Table();
		group = new ButtonGroup<Button>();
		table.add(new Label(name, skin, labelType)).colspan(optionNames.size());
		table.row();
		for (String s : optionNames) {
			Button b = new TextButton(s, skin, buttonType);
			group.add(b);
			table.add(b);
		}
		group.setMaxCheckCount(1);
		group.setMinCheckCount(1);

		group.setChecked(optionNames.get(index));

		return table;
	}

}
