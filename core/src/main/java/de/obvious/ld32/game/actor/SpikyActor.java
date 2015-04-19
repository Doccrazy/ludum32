package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.game.world.GameWorld;

public class SpikyActor extends EnemyActor {

    public SpikyActor(GameWorld world, Vector2 spawn, boolean spawnIsLeftBottom) {
        super(world, spawn, spawnIsLeftBottom);
        radius = 0.75f;
    }

    @Override
    void drawBody(Batch batch) {
        Animation anim = Resource.GFX.enemySpiky[animationDir().ordinal()];
        TextureRegion frame = anim.getKeyFrame(killed ? Math.min(stateTime, anim.getAnimationDuration()) : (isMoving() ? stateTime : 0), !killed);
        float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
        batch.draw(frame, getX() + getOriginX() - originX, getY(),
                frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE);
    }

}
