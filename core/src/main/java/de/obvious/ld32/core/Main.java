package de.obvious.ld32.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;

import de.obvious.ld32.game.GameScreen;

public class Main extends Game {
	private GameScreen gameScreen;
	private FPSLogger fps;

	@Override
	public void create () {
		Resource.init();

		gameScreen = new GameScreen();
		setScreen(gameScreen);

		fps = new FPSLogger();
	}

	@Override
	public void render () {
		super.render();
		//fps.log();
	}
}
