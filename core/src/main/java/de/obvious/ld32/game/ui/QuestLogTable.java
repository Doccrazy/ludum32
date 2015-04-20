package de.obvious.ld32.game.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.game.misc.QuestEvent;
import de.obvious.ld32.game.misc.QuestEvent.Status;

public class QuestLogTable extends Table {
    private UiRoot root;
    private SortedMap<String, QuestEvent> quests = new TreeMap<>();
    private Map<QuestEvent, String> texts = new HashMap<QuestEvent, String>() {{
        put(new QuestEvent("Fuel", QuestEvent.Status.START), "Find fuel to power the engines");
        put(new QuestEvent("Fuel", QuestEvent.Status.ITEM), "Return the fuel canister to your ship");
        put(new QuestEvent("Med", QuestEvent.Status.START), "Find medicine for your wounded crewmember");
        put(new QuestEvent("Med", QuestEvent.Status.ITEM), "Take the medicine to your shipmate");
    }};

    public QuestLogTable(UiRoot root) {
        this.root = root;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        root.getWorld().pollEvents(QuestEvent.class, (QuestEvent event) -> {
            if (!quests.containsKey(event.getQuest()) || quests.get(event.getQuest()).getStatus().ordinal() < event.getStatus().ordinal())
            quests.put(event.getQuest(), event);
            refreshQuests();
        });
    }

    private void refreshQuests() {
        clearChildren();
        for (QuestEvent q : quests.values()) {
            if (q.getStatus() == QuestEvent.Status.END) {
                continue;
            }
            row();
            add(new Image(q.getStatus() == Status.START ? Resource.GFX.quest : Resource.GFX.questDone)).pad(2);
            add(new Label(texts.get(q), new LabelStyle(Resource.FONT.retroSmall, q.getStatus() == Status.START ? Color.YELLOW : Color.GREEN))).left().top();
        }
    }
}
