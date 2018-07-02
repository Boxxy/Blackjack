package com.stf.bj.app.strategy;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WagerButton extends TextButton {

	double currentWager;

	public WagerButton(Skin skin, String styleName) {
		this(skin, styleName, 5);

	}

	public WagerButton(Skin skin, String styleName, int currentWager) {
		super(String.valueOf(currentWager), skin, styleName);

		this.addListener(new ClickListener(Buttons.LEFT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				onClick(true);
			}
		});
		this.addListener(new ClickListener(Buttons.RIGHT) {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				onClick(false);
			}
		});

		setWager(currentWager);

	}

	public void onClick(boolean left) {
		double newWager= 0;
		if (left) {
			newWager = currentWager + 5;
			if (currentWager < 50) {
				newWager = currentWager + 5;
			} else {
				newWager = currentWager + 25;
			}
		} else {
			if (currentWager < 5) {
				return;
			} else if (currentWager < 55) {
				newWager = currentWager - 5;
			} else {
				newWager = currentWager - 25;
			}
		}

		setWager(newWager);
	}

	public void setWager(double newWager) {
		currentWager = newWager;
		setText(String.valueOf(currentWager));
	}

}
