package de.obvious.ld32.game.abilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

public interface Ability {

    void trigger(Vector2 position, FireMode mode);

    Texture getTexture();

    Animation getWeaponAnimation(boolean fire);

}
