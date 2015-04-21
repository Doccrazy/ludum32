package de.obvious.ld32.game.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.obvious.ld32.core.Resource;
import de.obvious.ld32.data.QuestStatus;
import de.obvious.ld32.data.QuestType;
import de.obvious.ld32.game.misc.UpdateQuestEvent;

public class QuestLogTable extends Table {
    private UiRoot root;
    private Map<QuestTextDef, String> texts = new HashMap<QuestTextDef, String>() {{
        put(new QuestTextDef(QuestType.FUEL, QuestStatus.START), "Find fuel to power the engines");
        put(new QuestTextDef(QuestType.FUEL, QuestStatus.ITEM), "Return the fuel canister to the machine room");
        put(new QuestTextDef(QuestType.MED, QuestStatus.START), "Find medicine for your wounded crewmember");
        put(new QuestTextDef(QuestType.MED, QuestStatus.ITEM), "Take the medicine to your shipmate");
        put(new QuestTextDef(QuestType.KRISTALL, QuestStatus.START), "Find the energy crystal");
        put(new QuestTextDef(QuestType.KRISTALL, QuestStatus.ITEM), "Return the energy crystal to your ship");
    }};

    public QuestLogTable(UiRoot root) {
        this.root = root;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        root.getWorld().pollEvents(UpdateQuestEvent.class, (UpdateQuestEvent event) -> {
            refreshQuests();
        });
    }

    private void refreshQuests() {
        clearChildren();
        for (Map.Entry<QuestType, QuestStatus> entry : root.getWorld().getQuests().entrySet()) {
            if (entry.getValue() == QuestStatus.END) {
                continue;
            }
            row();
            add(new Image(entry.getValue() == QuestStatus.START ? Resource.GFX.quest : Resource.GFX.questDone)).pad(2);
            add(new Label(texts.get(new QuestTextDef(entry.getKey(), entry.getValue())), new LabelStyle(Resource.FONT.retroSmall, entry.getValue() == QuestStatus.START ? Color.YELLOW : Color.GREEN))).left().top();
        }
    }
}

class QuestTextDef {
    private QuestType type;
    private QuestStatus status;

    public QuestTextDef(QuestType type, QuestStatus status) {
        super();
        this.type = type;
        this.status = status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        QuestTextDef other = (QuestTextDef) obj;
        if (status != other.status)
            return false;
        if (type != other.type)
            return false;
        return true;
    }

}