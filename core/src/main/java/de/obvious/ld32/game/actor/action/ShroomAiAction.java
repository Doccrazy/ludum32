package de.obvious.ld32.game.actor.action;

import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.actor.ShroomActor;
import de.obvious.ld32.game.actor.ShroomLingActor;


public class ShroomAiAction extends BaseAiAction {
    private static final float ATTACK_DELAY = 1f;
    private static final float ATTACK_DIST = 1.5f;

    private float lastAttack = -ATTACK_DELAY;
    private float speed = 0.5f;

    public ShroomAiAction() {
        task.every(0.5f, (Void) -> {
            if (distToPlayer() < 4 && Math.random() < 0.5f && !((ShroomActor)getActor()).isStunned()) {
                spawnShroomLing();
            }
        });
    }

    @Override
    protected boolean doAct(float delta) {
        if (distToPlayer() < GameRules.AGGRO_RANGE && !((ShroomActor)getActor()).isStunned()) {
            pathToPlayer();
            followPlayer(4f, speed);
        }
        return false;
    }

    private void spawnShroomLing() {
        Vector2 spawn = getActor().getBody().getPosition().cpy();
        Vector2 toPlayer = vecToPlayer().nor();
        toPlayer.rotateRad((float) (Math.random() * Math.PI));
        getWorld().addActor(new ShroomLingActor(getWorld(), spawn.add(toPlayer.scl((float) (1 + Math.random()))), false));
    }

}
