
package com.teotigraphix.causticlive;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.teotigraphix.causticlive.config.CausticLiveModule;
import com.teotigraphix.caustk.core.internal.DesktopSoundGenerator;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Caustic Live";
        cfg.useGL20 = false;
        cfg.width = 800;
        cfg.height = 480;
        cfg.resizable = false;

        CausticLiveApp listener = new CausticLiveApp(new DesktopSoundGenerator(),
                new CausticLiveModule());
        new LwjglApplication(listener, cfg);
    }
}
