package com.stf.bj.app.strategy;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TacticButton extends TextButton {

	Tactics currentTactic;

	public TacticButton(Skin skin, String styleName) {
		this(skin, styleName, Tactics.STAY);

	}

	public TacticButton(Skin skin, String styleName, Tactics currentTactic) {
		super(currentTactic.getDisplay(), skin, styleName);

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

		setTactic(currentTactic);

	}

	public void onClick(boolean left) {
		if (left) {
			setTactic(Tactics.getNext(currentTactic));
		} else {
			setTactic(Tactics.getPrevious(currentTactic));
		}
	}

	public void setTactic(Tactics tactic) {
		currentTactic = tactic;
		setText(currentTactic.getDisplay());
		switch (tactic) {
		case DOUBLE_HIT:
		case DOUBLE_STAY:
			setColor(Color.BLUE);
			break;
		case HIT:
			setColor(Color.GREEN);
			break;
		case SPLIT:

			setColor(Color.PURPLE);
			break;
		case STAY:
			setColor(Color.BROWN);
			break;
		case SURRENDER_HIT:
		case SURRENDER_SPLIT:
		case SURRENDER_STAY:
			setColor(Color.RED);
			break;
		default:
			break;

		}
	}

	public int toInt() {
		return currentTactic.getValue();
	}
}
