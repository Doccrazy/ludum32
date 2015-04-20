package de.obvious.ld32.game.actor.action;

import de.obvious.ld32.game.actor.SpikyActor;

public class SpikyAiAction extends BaseAiAction {
    private static final float FOLLOW_DIST = 10f;

    private static final float ATTACK_DELAY = 5f;
    private static final float ATTACK_DIST = 4f;

    private float lastAttack = -ATTACK_DELAY;
    private float speed = 1.5f;

    @Override
    protected boolean doAct(float delta) {
        if (distToPlayer() < FOLLOW_DIST && !((SpikyActor)getActor()).isAttacking()) {
            pathToPlayer();
            followPlayer(1.5f, speed);
        }
        if (distToPlayer() < ATTACK_DIST && stateTime - lastAttack > ATTACK_DELAY) {
            ((SpikyActor)getActor()).attack();
            lastAttack = stateTime;
        }
        return false;
    }

}

