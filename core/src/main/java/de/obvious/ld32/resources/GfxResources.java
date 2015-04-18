package de.obvious.ld32.resources;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.obvious.shared.core.ResourcesBase;

public class GfxResources extends ResourcesBase {
	public Sprite player = atlas.createSprite("player");

    public GfxResources() {
        super("game.atlas");
    }
}
