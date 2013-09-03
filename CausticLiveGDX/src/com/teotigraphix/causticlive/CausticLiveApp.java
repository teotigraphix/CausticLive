
package com.teotigraphix.causticlive;

import com.google.inject.Singleton;
import com.teotigraphix.causticlive.screen.SplashScreen;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.libgdx.application.GDXGame;

@Singleton
public class CausticLiveApp extends GDXGame {

    public CausticLiveApp(ISoundGenerator generator) {
        super("CausticLive", generator);
    }

    @Override
    public void create() {
        super.create();
        setScreen(new SplashScreen());
    }

}
