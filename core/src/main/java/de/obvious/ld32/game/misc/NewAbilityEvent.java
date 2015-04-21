package de.obvious.ld32.game.misc;

import de.obvious.ld32.game.abilities.Ability;
import de.obvious.shared.game.event.Event;

public class NewAbilityEvent extends Event {
    private Ability ability;

    public NewAbilityEvent(Ability ability) {
        super(0, 0);
        this.ability = ability;
    }


    public Ability getAbility() {
        return ability;
    }
}
