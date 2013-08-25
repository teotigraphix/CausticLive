
package com.teotigraphix.causticlive;

import com.teotigraphix.causticlive.screen.SplashScreen;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.libgdx.application.GDXGame;

public class CausticLiveApp extends GDXGame {

    public CausticLiveApp(ISoundGenerator generator) {
        super(generator);
    }

    @Override
    public void create() {
        setScreen(new SplashScreen());
    }
}
