package de.obvious.ld32.game.actor.action;

import com.badlogic.gdx.scenes.scene2d.Action;

public class RageAction extends Action {
    private int rage;
    private static int RAGE_THRESHOLD = 4;
    private static int RAGE_LIMIT = 7;
    private float cooldown;

    public boolean isEnraged() {
        return rage >= RAGE_THRESHOLD;
    }

    @Override
    public boolean act(float delta) {
        if (rage > 0) {
            cooldown += delta;
            while (cooldown > 1) {
                cooldown -= 1;
                rage--;
            }
        } else {
            cooldown = 0;
        }
        return false;
    }

    public void fight() {
        if (rage < RAGE_LIMIT) {
            rage++;
        }
    }
}
