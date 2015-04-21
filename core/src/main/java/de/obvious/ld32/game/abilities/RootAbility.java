package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.actor.AoeDamageActor;
import de.obvious.ld32.game.world.GameWorld;

public class RootAbility implements Ability {
	private GameWorld world;
	private Texture texture;
	private boolean isRooted = true;

	public RootAbility(GameWorld world) {
		this.world = world;
		texture = Resource.GFX.rootWeapon;
	}

	@Override
	public void trigger(Vector2 position, FireMode mode) {
		if (mode == FireMode.PRIMARY) {
            AoeDamageActor aoe = new AoeDamageActor(world, world.getPlayer().getCrosshair(),
                    0.5f, 50, 1f, DamageType.DOT, Resource.GFX.rootAoe, Color.RED);
            aoe.setFriendly(true);
			world.addActor(aoe);
			Resource.SOUND.rootUp.play();
		} else {
			if (isRooted) {
				world.getPlayer().allowMovement(!isRooted);
				world.getPlayer().setRooted(isRooted);
				isRooted = false;
			} else {
				world.getPlayer().allowMovement(!isRooted);
				world.getPlayer().setRooted(isRooted);
				isRooted = true;
			}
		}
	}

	@Override
	public Texture getTexture(FireMode mode) {
		return texture;
	}

	@Override
	public Animation getWeaponAnimation(boolean fire) {
		return Resource.GFX.weaponRoot[fire ? 1 : 0];
	}

	@Override
	public float getCooldown(FireMode mode) {
        return GameRules.COOLDOWN_ROOT[mode.ordinal()];
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void end() {
		if(Boolean.FALSE.equals(world.getPlayer().isRooted()) || world.getPlayer().isRooted() == null)
			return;
		world.getPlayer().allowMovement(!isRooted);
		world.getPlayer().setRooted(isRooted);
		isRooted = true;
	}

}
