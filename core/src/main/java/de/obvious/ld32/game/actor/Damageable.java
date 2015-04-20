package de.obvious.ld32.game.actor;

import de.obvious.ld32.data.DamageType;

public interface Damageable {
    void damage(float amount, DamageType type);
}
