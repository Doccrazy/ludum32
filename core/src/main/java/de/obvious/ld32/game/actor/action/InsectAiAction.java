package de.obvious.ld32.game.actor.action;

import de.obvious.ld32.game.actor.InsectActor;

public class InsectAiAction extends BaseAiAction {
    private static final float FOLLOW_DIST = 10f;
    private static final float CHARGE_DELAY = 5f;
    private static final float CHARGE_DIST = 5f;
    private static final float CHARGE_DURATION = 0.25f;

    private static final float ATTACK_DELAY = 1f;
    private static final float ATTACK_DIST = 1.5f;

    private float lastCharge = -CHARGE_DELAY;
    private float lastAttack = -ATTACK_DELAY;
    private float speed = 2.0f;

    @Override
    protected boolean doAct(float delta) {
        if (distToPlayer() < FOLLOW_DIST) {
            pathToPlayer();
            if (playerLos() && distToPlayer() < CHARGE_DIST && stateTime - lastCharge > CHARGE_DELAY) {
                speed = 20.0f;
                lastCharge = stateTime;
                task.in(CHARGE_DURATION, (Void) -> speed = 2.0f);
            }
            followPlayer(1.2f, speed);
        }
        if (distToPlayer() < ATTACK_DIST && stateTime - lastAttack > ATTACK_DELAY) {
            ((InsectActor)getActor()).attack(ATTACK_DIST, 50f);
            lastAttack = stateTime;
        }
        return false;
    }

}
