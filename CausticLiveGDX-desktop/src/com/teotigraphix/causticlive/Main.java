
package com.teotigraphix.causticlive;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.teotigraphix.causticlive.config.CausticLiveModule;
import com.teotigraphix.caustk.sound.core.DesktopSoundGenerator;

public class Main {
    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Caustic Live";
        cfg.useGL20 = false;
        cfg.width = 1200;
        cfg.height = 752;
        cfg.resizable = false;

        //        Settings settings = new Settings();
        //        settings.maxWidth = 2048;
        //        settings.maxHeight = 2048;
        //        TexturePacker2.process(settings, new File(
        //                "C:/Users/Work/Documents/git/CausticLive/CausticLiveGDX-android/assets/images")
        //                .getPath(), "../CausticLiveGDX-android/assets", "game");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CausticLiveApp listener = new CausticLiveApp(new DesktopSoundGenerator());
        new LwjglApplication(listener, cfg);
        listener.initialize(new CausticLiveModule());
    }
}
