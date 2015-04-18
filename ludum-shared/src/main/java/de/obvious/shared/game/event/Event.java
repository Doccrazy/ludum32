package de.obvious.shared.game.event;

public abstract class Event {
    private float x, y;

    public Event(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
