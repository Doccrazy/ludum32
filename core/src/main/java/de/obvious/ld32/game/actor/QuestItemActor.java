package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.game.misc.QuestEvent;
import de.obvious.shared.game.actor.WorldActor;
import de.obvious.shared.game.world.Box2dWorld;

public class QuestItemActor extends WorldActor {
    private String quest;

    public QuestItemActor(Box2dWorld world, Vector2 spawn, String quest) {
        super(world);
        this.quest = quest;
        setPosition(spawn.x, spawn.y);
    }

    @Override
    protected void doAct(float delta) {
        world.pollEvents(QuestEvent.class, (QuestEvent event) -> {
            if (event.getQuest().equals(quest) && event.getStatus() == QuestEvent.Status.ITEM) {
                kill();
            }
        }, false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Sprite sprite = Resource.GFX.questItem.get(quest);
        batch.draw(sprite, getX(), getY(), 0, 0, sprite.getWidth() * Constants.PIXEL_SCALE, sprite.getHeight() * Constants.PIXEL_SCALE, getScaleX(), getScaleY(), 0);
    }
}
