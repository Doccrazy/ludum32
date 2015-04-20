package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.DamageType;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

class SpikyAttackActor extends ShapeActor implements CollisionListener {

    private Vector2 velocity;

    public SpikyAttackActor(Box2dWorld world, Vector2 spawn, Vector2 velocity) {
        super(world, spawn, false);
        this.velocity = velocity;
        task.in(5f, (Void) -> kill());
    }

    @Override
    public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
        if (other.getUserData() instanceof PlayerActor) {
            ((PlayerActor) other.getUserData()).damage(75, DamageType.NORMAL);
        }

        if (!other.isBullet())
            kill();

        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(Resource.GFX.spikeLittleProjectile.getKeyFrame(stateTime, true), getX(), getY(), getOriginX(), getOriginY(), 24 / 50f, 5 / 50f, 1, 1, velocity.angle());
    }

    @Override
    public void endContact(Body other) {
    }

    @Override
    public void hit(float force) {
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forDynamic(spawn).asBullet().velocity(velocity.cpy().scl(5)).fixShape(ShapeBuilder.circle(0.1f)).fixSensor();
    }

}
