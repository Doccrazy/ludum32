package de.obvious.ld32.game.misc;

import de.obvious.shared.game.event.Event;

public class QuestEvent extends Event implements Comparable<QuestEvent> {
    private String quest;
    private Status status;

    public QuestEvent(String quest, Status status) {
        super(0, 0);
        this.quest = quest;
        this.status = status;
    }

    public String getQuest() {
        return quest;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        START,
        ITEM,
        END
    }

    @Override
    public int compareTo(QuestEvent o) {
        int result = getStatus().compareTo(o.getStatus());
        if (result == 0) {
            result = getQuest().compareTo(o.getQuest());
        }
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((quest == null) ? 0 : quest.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        QuestEvent other = (QuestEvent) obj;
        if (quest == null) {
            if (other.quest != null)
                return false;
        } else if (!quest.equals(other.quest))
            return false;
        if (status != other.status)
            return false;
        return true;
    }

}
