package de.obvious.ld32.game.actor.action;

import de.obvious.ld32.game.actor.ShroomLingActor;


public class ShroomLingAiAction extends BaseAiAction {

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
