package com.mygdx.wanderingpaw;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.wanderingpaw.main.Game;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(Game.V_WIDTH * Game.SCALE, Game.V_HEIGHT * Game.SCALE);
		config.setResizable(false);
		config.setForegroundFPS(60);
		config.setTitle("Wandering Paw");
		new Lwjgl3Application(new Game(), config);
	}
}
