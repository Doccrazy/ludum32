package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.game.abilities.SpikeAbility;
import de.obvious.ld32.game.actor.action.SpikyAiAction;
import de.obvious.ld32.game.world.GameWorld;

public class SpikyActor extends EnemyActor {
    private static final int SPAWN_COUNT = 10;
    private boolean attacking;


    public SpikyActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
        super(world, spawn, spawnIsLeftBottom);
        ability = new SpikeAbility(world);
        radius = 0.75f;
        bloodColor = Color.valueOf("3114AE");
    }

    @Override
    protected void init() {
        super.init();
        addAction(new SpikyAiAction());
    }

    @Override
    void drawBody(Batch batch) {
        Animation anim = attacking && !killed ? Resource.GFX.enemySpikyAttack : Resource.GFX.enemySpiky[animationDir().ordinal()];
        TextureRegion frame = anim.getKeyFrame(killed ? Math.min(stateTime, anim.getAnimationDuration()) : (isMoving() || attacking ? stateTime : 0), !killed);
        float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
        batch.draw(frame, getX() + getOriginX() - originX, getY(),
                frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE);
    }

    public void attack() {
        attacking = true;
        body.setLinearVelocity(0, 0);
        stateTime = 0;
        task.in(1f, (Void) -> {
            if (((GameWorld)world).getPlayer().isDead() || killed) {
                return;
            }
            spawnSpikes();
        });
        task.in(Resource.GFX.enemySpikyAttack.getAnimationDuration(), (Void) -> {
            attacking = false;
        });
    }

    private void spawnSpikes() {
        Vector2 d = new Vector2(1, 0);

        for (int i = 0; i < SPAWN_COUNT; i++) {
            world.addActor(new SpikyAttackActor(world, body.getPosition().cpy().add(d.cpy().scl(0.9f)), d.cpy()));
            d.rotate(360f/SPAWN_COUNT);
        }
    }

    public boolean isAttacking() {
        return attacking;
    }
}
