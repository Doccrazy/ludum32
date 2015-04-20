package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.game.actor.PlayerActor;
import de.obvious.ld32.game.actor.action.DrawableAction;

public class HealAction extends Action implements DrawableAction {
    private PlayerActor player;
    private float stateTime;
    private float health;
    private float time;

    public HealAction(PlayerActor player, float health, float time) {
        this.player = player;
        this.health = health;
        this.time = time;
    }

    @Override
    public boolean act(float delta) {
        player.setHealth(player.getHealth() + health*(delta/time));
        stateTime += delta;
        return stateTime > time;
    }

    @Override
    public void draw(Batch batch) {
        Animation anim = Resource.GFX.enemyShroomHeal;
        TextureRegion frame = anim.getKeyFrame(stateTime, true);

        float originX = (frame.getRegionWidth() * Constants.PIXEL_SCALE) / 2f;
        batch.draw(frame, player.getX() + player.getOriginX() - originX, player.getY(), originX, player.getOriginY(),
                frame.getRegionWidth() * Constants.PIXEL_SCALE, frame.getRegionHeight() * Constants.PIXEL_SCALE,
                player.getScaleX(), player.getScaleY(), 0f);
    }
}
