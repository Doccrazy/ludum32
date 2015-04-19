package de.obvious.ld32.game.actor.action;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.scenes.scene2d.Action;

import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.ai.AiUtils;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.WorldActor;

public enum MeleeState implements State<AiAction> {
    IDLE() {
        @Override
        public void update(AiAction entity) {
            if (AiUtils.distance(entity.getActor(), AiUtils.getPlayer(entity.getActor())) < WANDER_DIST) {
                entity.getStateMachine().changeState(WANDER);
            }
        }
    },
    WANDER() {
        @Override
        public void enter(AiAction entity) {
            entity.enableWander(true);
        }

        @Override
        public void exit(AiAction entity) {
            entity.enableWander(false);
        }

        @Override
        public void update(AiAction entity) {
            float d = AiUtils.distance(entity.getActor(), AiUtils.getPlayer(entity.getActor()));
            if (d < PURSUE_DIST) {
                entity.getStateMachine().changeState(PURSUE);
            } else if (d > WANDER_DIST) {
                entity.getStateMachine().changeState(IDLE);
            }
        }
    },
    PURSUE() {
        @Override
        public void enter(AiAction entity) {
            entity.enablePursue(true);
        }

        @Override
        public void exit(AiAction entity) {
            entity.enablePursue(false);
        }

        @Override
        public void update(AiAction entity) {
            float d = AiUtils.distance(entity.getActor(), AiUtils.getPlayer(entity.getActor()));
            if (d > PURSUE_DIST*1.25f) {
                entity.getStateMachine().changeState(WANDER);
            } else if (d < ATTACK_DIST) {
                entity.getStateMachine().changeState(ATTACK);
            }
        }
    },
    ATTACK() {
        @Override
        public void enter(AiAction entity) {
            entity.getActor().addAction(new AttackAction());
            entity.enablePursue(true);
        }

        @Override
        public void exit(AiAction entity) {
            for (Action a : entity.getActor().getActions()) {
                if (a instanceof AttackAction) {
                    entity.getActor().removeAction(a);
                    break;
                }
            }
            entity.enablePursue(false);
        }

        @Override
        public void update(AiAction entity) {
            if (AiUtils.distance(entity.getActor(), AiUtils.getPlayer(entity.getActor())) > ATTACK_DIST*1.25f) {
                entity.getStateMachine().changeState(PURSUE);
            }
        }
    };

    private static float WANDER_DIST = 15f;
    private static float PURSUE_DIST = 5f;
    private static float ATTACK_DIST = 2f;

    @Override
    public void enter(AiAction entity) {
    }

    @Override
    public void exit(AiAction entity) {
    }

    @Override
    public boolean onMessage(AiAction entity, Telegram telegram) {
        return false;
    }
}

class AttackAction extends Action {
    private float time;

    @Override
    public boolean act(float delta) {
        time += delta;
        while (time > 1f) {
            hit();
            time -= 1f;
        }
        return getPlayer().isDead();
    }

    private void hit() {
        getPlayer().damage(50f, DamageType.MELEE);
    }

    private PlayerActor getPlayer() {
        return ((GameWorld)((WorldActor)getActor()).getWorld()).getPlayer();
    }

}