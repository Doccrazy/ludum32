package de.obvious.ld32.game.actor.action;

import de.obvious.ld32.game.actor.ShroomLingActor;


public class ShroomLingAiAction extends BaseAiAction {
    private static final float ATTACK_DELAY = 1f;
    private static final float ATTACK_DIST = 1.5f;

    private float lastAttack = -ATTACK_DELAY;
    private float speed = 0.5f;

    @Override
    protected boolean doAct(float delta) {
        if (distToPlayer() < 5) {
            getActor().getBody().setLinearVelocity(vecToPlayer());
        }
        if (distToPlayer() < 1.75f) {
            ((ShroomLingActor)getActor()).blow();
            return true;
        }
        return false;
    }

}
