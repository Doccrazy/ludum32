package de.obvious.ld32.game.actor.action;

import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.actor.RootActor;

public class RootAiAction extends BaseAiAction {
    private static final float ATTACK_DELAY = 3f;
    private static final float ATTACK_DIST = 4f;

    private float lastAttack = -ATTACK_DELAY;
    private float speed = 1.5f;

    @Override
    protected boolean doAct(float delta) {
        if (distToPlayer() < GameRules.AGGRO_RANGE && !((RootActor)getActor()).isAttacking()) {
            pathToPlayer();
            followPlayer(2.5f, speed);
        }
        if (distToPlayer() < ATTACK_DIST && stateTime - lastAttack > ATTACK_DELAY) {
            ((RootActor)getActor()).attack(getPlayer().getBody().getPosition());
            lastAttack = stateTime;
        }
        return false;
    }

}
