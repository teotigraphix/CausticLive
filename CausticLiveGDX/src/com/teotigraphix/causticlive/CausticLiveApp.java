
package com.teotigraphix.causticlive;

import com.google.inject.Module;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.screen.AssignScreen;
import com.teotigraphix.causticlive.screen.ICausticLiveScreen;
import com.teotigraphix.causticlive.screen.MainScreen;
import com.teotigraphix.causticlive.screen.SplashScreen;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.libgdx.application.GDXGame;

@Singleton
public class CausticLiveApp extends GDXGame {

    public CausticLiveApp(ISoundGenerator generator, Module module) {
        super("CausticLive", generator, module);
        addScreen(ICausticLiveScreen.SPLASH_SCREEN, SplashScreen.class);
        addScreen(ICausticLiveScreen.MAIN_SCREEN, MainScreen.class);
        addScreen(ICausticLiveScreen.ASSIGN_SCREEN, AssignScreen.class);
    }

    @Override
    public void create() {
        super.create();
        setScreen(ICausticLiveScreen.SPLASH_SCREEN);
    }

}
