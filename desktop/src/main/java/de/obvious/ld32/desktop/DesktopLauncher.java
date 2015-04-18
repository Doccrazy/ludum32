package de.obvious.ld32.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.obvious.ld32.core.Main;
import de.obvious.ld32.data.Constants;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.SCREEN_WIDTH;
		config.height = Constants.SCREEN_HEIGHT;
		config.vSyncEnabled = true;
		config.title = Constants.GAME_TITLE + " (a Ludum Dare " + Constants.COMPO_ID + " game by Obvious Industries)";
		new LwjglApplication(new Main(), config);
	}
}
