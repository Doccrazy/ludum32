package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.shared.game.actor.WorldActor;
import de.obvious.shared.game.world.Box2dWorld;

public class BloodActor extends WorldActor {
    private Vector2 spawn;
    private Vector2 dir;
    private int effectIdx;
    private float percent;
    private Color color;

    public BloodActor(Box2dWorld world, Vector2 spawn, Color color, float damage) {
        super(world);
        this.spawn = spawn.cpy();
        this.color = color;
        this.dir = new Vector2(1, 0).rotateRad((float) (Math.random() * Math.PI * 2));
        effectIdx = MathUtils.random(Resource.GFX.blood.length-1);
        setRotation(MathUtils.random(360f));
        setScale(Math.min(1f, 0.25f + damage / 25));
    }

    @Override
    protected void doAct(float delta) {
        percent = Interpolation.exp10Out.apply(Math.min(stateTime, 1));
        setPosition(spawn.x + dir.x * percent, spawn.y + dir.y * percent);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Sprite frame = Resource.GFX.blood[effectIdx];
        batch.setColor(color);
        batch.draw(frame, getX(), getY(), (frame.getWidth() * Constants.PIXEL_SCALE)/2f, (frame.getHeight() * Constants.PIXEL_SCALE)/2f,
                frame.getWidth() * Constants.PIXEL_SCALE, frame.getHeight() * Constants.PIXEL_SCALE,
                getScaleX() * percent, getScaleY() * percent, getRotation());
        batch.setColor(Color.WHITE);
    }

}
