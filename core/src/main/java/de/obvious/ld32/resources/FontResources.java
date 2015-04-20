package de.obvious.ld32.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;

import de.obvious.shared.core.ResourcesBase;

public class FontResources extends ResourcesBase {
	public BitmapFont retro = bitmapFont("Font/font");
	public BitmapFont retroSmall = bitmapFont("Font/fontSmall");

	public String[] insults = Gdx.files.internal("insults.txt").readString().split("\r?\n");

	public String getRandomInsult() {
	    return insults[MathUtils.random(insults.length-1)];
	}
}
