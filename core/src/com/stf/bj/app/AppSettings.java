package com.stf.bj.app;

import com.stf.bj.app.sprites.AnimationSettings;
import com.stf.bj.app.sprites.TimingSettings;
import com.stf.bj.app.table.TableRules;

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
		return new AppSettings(TableRules.mystic4Surrender(), AnimationSettings.getNew(), TimingSettings.getFast());
	}
	
	public static AppSettings getClassic() {
		return new AppSettings(TableRules.hochunk(), AnimationSettings.getClassic(), TimingSettings.getSlow());
	}
	
	public static AppSettings getRandom() {
		return new AppSettings(TableRules.getRandom(), AnimationSettings.getRandom(), TimingSettings.getRandom());
	}
	
	
}
