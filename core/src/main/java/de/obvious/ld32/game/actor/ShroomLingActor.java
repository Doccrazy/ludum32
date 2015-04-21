package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.DamageType;
import de.obvious.ld32.data.GameRules;
import de.obvious.ld32.game.actor.action.FriendlyShroomLingAiAction;
import de.obvious.ld32.game.actor.action.ShroomLingAiAction;
import de.obvious.ld32.game.world.GameWorld;

public class ShroomLingActor extends EnemyActor {
    private boolean blowing;
    private boolean friendly;

    public ShroomLingActor(GameWorld world, Vector2 spawn, boolean friendly) {
        super(world, spawn, false);
        this.friendly = friendly;
        addAction(friendly ? new FriendlyShroomLingAiAction() : new ShroomLingAiAction());
        radius = 0.25f;
        initialLives = 20;
        bloodColor = Color.PURPLE;
    }

    @Override
    void drawBody(Batch batch) {
        Animation anim = Resource.GFX.enemyShroom[animationDir().ordinal()];
        TextureRegion frame = anim.getKeyFrame(killed ? Math.min(stateTime, anim.getAnimationDuration()) : (isMoving() ? stateTime : 0), !killed);
        if (blowing && stateTime % 0.25f < 0.125f && !killed) {
            batch.setColor(1, 0, 0, 1);
        }
        batch.draw(frame, getX(), getY(), getWidth(), getHeight() * 1.5f );
        batch.setColor(1, 1, 1, 1);
    }

    public void blow() {
        body.setLinearVelocity(body.getLinearVelocity().limit(0.5f));
        blowing = true;
        stateTime = 0;
        task.in(GameRules.SHROOMLING_BLINK_TIME, (Void) -> {
            AoeDamageActor aoe = new AoeDamageActor(world, body.getPosition(), 1.75f, GameRules.SHROOMLING_DPS, GameRules.SHROOMLING_DMG_TIME, DamageType.DOT,
                    Resource.GFX.enemyShroomCloud,  new Color(0, 1, 0.25f, 0.75f));
            aoe.setFriendly(friendly);
            world.addActor(aoe);
            Resource.SOUND.littleShroomExplode.play();
            killme();
        });
    }

    public boolean isFriendly() {
        return friendly;
    }
}