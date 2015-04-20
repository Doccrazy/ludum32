package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.game.abilities.RootAbility;
import de.obvious.ld32.game.actor.action.RootAiAction;
import de.obvious.ld32.game.world.GameWorld;

public class RootActor extends EnemyActor {
    private boolean attacking;

    public RootActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
        super(world, spawn, spawnIsLeftBottom);
        ability = new RootAbility(world);
        addAction(new RootAiAction());
    }

    @Override
    void drawBody(Batch batch) {
        Animation anim = attacking && !killed ? Resource.GFX.enemyRootAttack : Resource.GFX.enemyRoot[animationDir().ordinal()];
        TextureRegion frame = anim.getKeyFrame(killed ? Math.min(stateTime, anim.getAnimationDuration()) : (isMoving() || attacking ? stateTime : 0), !killed);
        float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
        batch.draw(frame, getX() + getOriginX() - originX, getY(),
                frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE);
    }

    @Override
    protected void doAct(float delta) {
        super.doAct(delta);
        if (attacking) {
            body.setLinearVelocity(0, 0);
        }
    }

    public void attack(Vector2 target) {
        if (attacking) {
            return;
        }
        attacking = true;
        stateTime = 0;
        task.in(1.25f, (Void) -> {
            if (((GameWorld)world).getPlayer().isDead() || killed) {
                return;
            }
            AoeDamageActor aoe = new AoeDamageActor(world, ((GameWorld)world).getPlayer().getBody().getPosition(),
                    0.5f, 50, 1f, DamageType.DOT, Resource.GFX.rootAoe, Color.RED);
            aoe.setWarningTime(0.4f);
            world.addActor(aoe);
        });
        task.in(Resource.GFX.enemyRootAttack.getAnimationDuration(), (Void) -> {
            attacking = false;
        });
    }

    public boolean isAttacking() {
        return attacking;
    }

}
