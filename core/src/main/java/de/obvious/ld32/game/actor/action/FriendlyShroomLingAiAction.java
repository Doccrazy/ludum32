package de.obvious.ld32.game.actor.action;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.obvious.ld32.game.actor.EnemyActor;
import de.obvious.ld32.game.actor.ShroomLingActor;



public class FriendlyShroomLingAiAction extends BaseAiAction {
    private static final float ATTACK_DELAY = 1f;
    private static final float ATTACK_DIST = 1.5f;

    private float lastAttack = -ATTACK_DELAY;
    private float speed = 0.5f;

    @Override
    protected boolean doAct(float delta) {
        Vector2 toEnemy = null;
        for (Actor a : getWorld().stage.getActors()) {
            if (a instanceof EnemyActor && !((EnemyActor) a).isKilled() &&
                    (!(a instanceof ShroomLingActor) || !((ShroomLingActor)a).isFriendly())) {
                Vector2 found = ((EnemyActor)a).getBody().getPosition().cpy().sub(getActor().getBody().getPosition());
                if ((toEnemy == null || found.len2() < toEnemy.len2()) && found.len2() < 16) {
                    toEnemy = found;
                }
            }
        }
        if (toEnemy != null) {
            getActor().getBody().setLinearVelocity(toEnemy);
            if (toEnemy.len() < 1.5f) {
                ((ShroomLingActor)getActor()).blow();
                return true;
            }
        }
        return false;
    }

}
