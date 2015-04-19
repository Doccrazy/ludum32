package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.actor.BulletActor;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.world.GameWorld;

public class StartAbility implements Ability {
	private GameWorld world;
	private Color color;
	private Texture texturePrimary;
	private Texture textureAlternate;

	public StartAbility(GameWorld world, Color color) {
		this.world = world;
		this.color = color;
		texturePrimary = Resource.GFX.startWeapon;
		textureAlternate = Resource.GFX.startWeaponX;
	}

	@Override
	public void trigger(Vector2 position, FireMode mode) {
		if (mode == FireMode.PRIMARY) {
			Vector2 d = position.cpy().sub(world.getPlayer().getBody().getPosition());
			d.nor().scl(5f);
			world.addActor(new BulletActor(world, new Vector2(world.getPlayer().getBody().getPosition().x, world.getPlayer().getBody().getPosition().y + PlayerActor.RADIUS/2) , d, color));
		}


	}

	@Override
	public Texture getTexture(FireMode mode) {
		if(mode == FireMode.PRIMARY)
		return texturePrimary;
		else
			return textureAlternate;
	}

	@Override
	public Animation getWeaponAnimation(boolean fire) {
		return Resource.GFX.weaponStart[fire ? 1 : 0];
	}
}
