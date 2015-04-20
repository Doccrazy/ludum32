package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.shared.game.actor.ShapeActor;
import de.obvious.shared.game.base.CollisionListener;
import de.obvious.shared.game.world.BodyBuilder;
import de.obvious.shared.game.world.Box2dWorld;
import de.obvious.shared.game.world.ShapeBuilder;

public class DoorActor extends ShapeActor implements CollisionListener {
    private boolean vertical;
    private float closeTime = Float.MAX_VALUE;
    private boolean open;
    private Body trigger;

    public DoorActor(Box2dWorld world, Vector2 spawn, boolean vertical) {
        super(world, spawn, true);
        this.vertical = vertical;
    }

    @Override
    protected void init() {
        super.init();
        trigger = BodyBuilder.forDynamic(new Vector2(body.getPosition().x, body.getPosition().y)).fixShape(ShapeBuilder.box(vertical ? 1f : 1.5f, vertical ? 1.5f : 1f))
               .fixSensor().build(world);
        trigger.setUserData(this);
        stateTime = 10;
    }

    @Override
    protected BodyBuilder createBody(Vector2 spawn) {
        return BodyBuilder.forStatic(spawn).fixShape(ShapeBuilder.box(vertical ? 0.5f : 1f, vertical ? 1f : 0.5f));
    }

    @Override
    protected void doAct(float delta) {
        super.doAct(delta);
        if (stateTime > closeTime && open) {
            close();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float dur = Resource.GFX.door[0].getAnimationDuration();
        float t = MathUtils.clamp(open ? stateTime : dur - stateTime, 0, dur);
        TextureRegion frame = Resource.GFX.door[vertical ? 1 : 0].getKeyFrame(t);
        batch.draw(frame, getX(), getY(), 0, 0, frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE, getScaleX(), getScaleY(), 0);
    }

    @Override
    public boolean beginContact(Body me, Body other, Vector2 normal, Vector2 contactPoint) {
        if (me.equals(trigger) && other.getUserData() instanceof PlayerActor) {
            open();
        }
        return false;
    }

    private void open() {
        closeTime = Float.MAX_VALUE;
        if (!open) {
            open = true;
            stateTime = 0;
            task.in(0, (Void) -> {
                body.setActive(false);
            });
        }
    }

    private void queueClose() {
        closeTime = stateTime + 0.5f;
    }

    private void close() {
        open = false;
        stateTime = 0;
        body.setActive(true);
    }

    @Override
    public void endContact(Body other) {
        if (other.getUserData() instanceof PlayerActor) {
            queueClose();
        }
    }

    @Override
    public void hit(float force) {
    }

    @Override
    protected void doRemove() {
        super.doRemove();
        world.box2dWorld.destroyBody(trigger);
        trigger = null;
    }
}
