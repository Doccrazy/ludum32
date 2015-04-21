package de.obvious.ld32.game.misc;

import de.obvious.ld32.data.QuestType;
import de.obvious.shared.game.event.Event;

public class UpdateQuestEvent extends Event {
    private QuestType type;

    public UpdateQuestEvent(QuestType type) {
        super(0, 0);
        this.type = type;
    }

    public QuestType getType() {
        return type;
    }

}
