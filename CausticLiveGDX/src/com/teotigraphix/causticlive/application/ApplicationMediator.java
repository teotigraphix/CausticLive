
package com.teotigraphix.causticlive.application;

import java.io.File;
import java.io.IOException;

import com.google.inject.Singleton;
import com.teotigraphix.libgdx.application.IApplicationMediator;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;

@Singleton
public class ApplicationMediator extends MediatorBase implements IApplicationMediator {

    public ApplicationMediator() {
    }

    @Override
    protected void onProjectCreate() {
        try {
            getController().getTrackSequencer().create(new File("songs/UntitledSong.ctks"));
            getController().getTrackSequencer().setCurrentTrack(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRegister() {

    }

    @Override
    public void dispose() {
    }

    @Override
    public void create(IScreen screen) {
    }

}
