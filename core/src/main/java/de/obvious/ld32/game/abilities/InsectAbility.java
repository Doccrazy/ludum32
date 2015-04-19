package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.WorldActor;

public class InsectAbility implements Ability {
	private GameWorld world;
	private WorldActor actor;
	private Texture texture;


	public InsectAbility(GameWorld world) {
		this.world = world;
		texture = Resource.GFX.insectWeapon;
	}

	@Override
	public void trigger(Vector2 position, FireMode mode) {
		if(mode == FireMode.PRIMARY){

		}
	}

	@Override
	public Texture getTexture(FireMode mode) {
		return texture;
	}

    @Override
    public Animation getWeaponAnimation(boolean fire) {
        return Resource.GFX.weaponInsect[fire ? 1 : 0];
    }
}
