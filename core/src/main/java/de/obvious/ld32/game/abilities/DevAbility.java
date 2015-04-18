package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.game.actor.BulletActor;
import de.obvious.ld32.game.world.GameWorld;

public class DevAbility implements Ability {
    private GameWorld world;
    private Color color;

    public DevAbility(GameWorld world, Color color) {
        this.world = world;
        this.color = color;
    }

    @Override
    public void trigger(Vector2 position, FireMode mode) {
        Vector2 d = position.cpy().sub(world.getPlayer().getBody().getPosition());
        d.nor().scl(5f);

        world.addActor(new BulletActor(world, world.getPlayer().getBody().getPosition(), d, color));
    }

}


