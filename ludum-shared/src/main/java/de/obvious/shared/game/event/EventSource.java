package de.obvious.shared.game.event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventSource {
    private final List<Event> eventQueue = new ArrayList<>();

    public void postEvent(Event event) {
        eventQueue.add(event);
    }

    public <T extends Event> void pollEvents(Class<T> type, Consumer<? super T> consumer) {
        pollEvents(type, consumer, true);
    }

    public <T extends Event> void pollEvents(Class<T> type, Consumer<? super T> consumer, boolean remove) {
        eventQueue.removeIf(new Predicate<Event>() {
            @Override
            public boolean test(Event event) {
                if (type.isInstance(event)) {
                    consumer.accept((T)event);
                    return remove;
                }
                return false;
            }
        });
    }
}
