package de.obvious.ld32.game.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.Constants;
import de.obvious.ld32.data.QuestStatus;
import de.obvious.ld32.data.QuestType;
import de.obvious.ld32.game.world.GameWorld;
import de.obvious.shared.game.actor.WorldActor;
import de.obvious.shared.game.world.Box2dWorld;

public class QuestItemActor extends WorldActor {
    private QuestType quest;

    public QuestItemActor(Box2dWorld world, Vector2 spawn, QuestType quest) {
        super(world);
        this.quest = quest;
        setPosition(spawn.x, spawn.y);
    }

    @Override
    protected void doAct(float delta) {
        if (((GameWorld)world).getQuests().get(quest) == QuestStatus.ITEM) {
            kill();
            	Resource.SOUND.itemPickup.play();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Sprite sprite = Resource.GFX.questItem.get(quest);
        batch.draw(sprite, getX(), getY(), 0, 0, sprite.getWidth() * Constants.PIXEL_SCALE, sprite.getHeight() * Constants.PIXEL_SCALE, getScaleX(), getScaleY(), 0);
    }
}
