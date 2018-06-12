package com.stf.bj.app.game;

import java.util.Random;

import com.stf.bj.app.game.server.TableRules;
import com.stf.bj.app.game.sprites.AnimationSettings;
import com.stf.bj.app.game.sprites.TimingSettings;

public class AppSettings {

	private final AnimationSettings animationSettings;
	private final TimingSettings timingSettings;
	private final TableRules tableRules;
	
	public AppSettings(TableRules tableRules, AnimationSettings animationSettings, TimingSettings timingSettings) {
		this.tableRules = tableRules;
		this.animationSettings = animationSettings;
		this.timingSettings = timingSettings; 
	}

	public AnimationSettings getAnimationSettings() {
		return animationSettings;
	}

	public TimingSettings getTimingSettings() {
		return timingSettings;
	}

	public TableRules getTableRules() {
		return tableRules;
	}
	
	public static AppSettings getNew() {
		return new AppSettings(TableRules.hochunk(), AnimationSettings.getNew(), TimingSettings.getFast());
	}
	
	public static AppSettings getHochunkPratice() {
		return new AppSettings(TableRules.hochunk(), AnimationSettings.getPratice(), TimingSettings.getPratice());
	}
	
	public static AppSettings getClassic() {
		return new AppSettings(TableRules.mystic6(), AnimationSettings.getClassic(), TimingSettings.getSlow());
	}
	
	public static AppSettings getRandom(Random r) {
		return new AppSettings(TableRules.getRandom(r), AnimationSettings.getRandom(r), TimingSettings.getRandom(r));
	}
	
	
}
