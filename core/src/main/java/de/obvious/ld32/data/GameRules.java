package de.obvious.ld32.data;

import com.badlogic.gdx.math.Vector2;

public class GameRules {
    public static final Vector2 GRAVITY = new Vector2(0, 0);

    public static final float LEVEL_WIDTH = 25.6f;
    public static final float LEVEL_HEIGHT = 14.4f;
    public static final float WALL_WIDTH = 0.3f;

    public static final float PLAYER_HEALTH = 200;
    public static final float PLAYER_HEALTH_WOUNDED = 100;
    public static final float REGEN_DELAY = 1.5f;
    public static final float REGEN_PER_SEC = 100f;
    public static final float STAGGER_TIME = 0.75f;

    public static final int ROUNDS_TO_WIN = 3;

    public static final float PLAYER_VELOCITY = 5f;  //m/s
    public static final float PLAYER_HALF_FOV = 40f;  //deg
    public static final float CORPSE_DESPAWN = 60;

    public static final float AGGRO_RANGE = 10f;

    public static final float SHROOM_HEAL_PS = 15f;
    public static final float SHROOMLING_BLINK_TIME = 1f;
    public static final float SHROOMLING_DPS = 50f;
    public static final float SHROOMLING_DMG_TIME = 2f;

    public static final float[] COOLDOWN_START = new float[]{0.33f, Float.MAX_VALUE};
    public static final float[] COOLDOWN_SHROOM = new float[]{2f, 10f};
    public static final float[] COOLDOWN_INSECT = new float[]{1f, Float.MAX_VALUE};
    public static final float[] COOLDOWN_ROOT = new float[]{1f, 5f};
    public static final float[] COOLDOWN_SPIKE = new float[]{3f, Float.MAX_VALUE};
}
