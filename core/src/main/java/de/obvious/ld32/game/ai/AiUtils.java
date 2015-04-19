package de.obvious.ld32.game.ai;

import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.Box2dActor;
import de.obvious.shared.game.actor.WorldActor;

public class AiUtils {
    public static PlayerActor getPlayer(WorldActor actor) {
        return ((GameWorld)actor.getWorld()).getPlayer();
    }

    public static float distance(Box2dActor a1, Box2dActor a2) {
        Vector2 tmp = a2.getBody().getPosition().cpy();
        tmp.sub(a1.getBody().getPosition());
        return tmp.len();
    }
}
