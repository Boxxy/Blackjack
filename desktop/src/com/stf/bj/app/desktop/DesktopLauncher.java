package com.stf.bj.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.stf.bj.app.BjApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "BJ Test";
	    config.width = 1280;
	    config.height = 720;
	    config.resizable = false;
	    //config.foregroundFPS = 0;
	    //config.backgroundFPS = 0;
	    //config.vSyncEnabled = false;
		new LwjglApplication(new BjApp(), config);
	}
}
