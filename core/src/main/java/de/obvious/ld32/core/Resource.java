package de.obvious.ld32.core;

import de.obvious.ld32.resources.FontResources;
import de.obvious.ld32.resources.GfxResources;
import de.obvious.ld32.resources.MusicResources;
import de.obvious.ld32.resources.SoundResources;

public class Resource {
    public static GfxResources GFX;
    public static FontResources FONT;
    public static SoundResources SOUND;
    public static MusicResources MUSIC;

    private Resource() {
    }

    public static void init() {
        GFX = new GfxResources();
        FONT = new FontResources();
        SOUND = new SoundResources();
        MUSIC = new MusicResources();
    }
}

