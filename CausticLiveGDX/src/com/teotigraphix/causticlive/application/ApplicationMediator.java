
package com.teotigraphix.causticlive.application;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.caustk.sequencer.ISongPlayer.OnSongPlayerBeatChange;
import com.teotigraphix.libgdx.application.IApplicationMediator;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;

@Singleton
public class ApplicationMediator extends MediatorBase implements IApplicationMediator {

    @Inject
    ILibraryModel libraryModel;

    @Inject
    ISoundModel soundModel;

    public ApplicationMediator() {
    }

    @Override
    protected void registerObservers() {

        getController().getDispatcher().register(OnSongPlayerBeatChange.class,
                new EventObserver<OnSongPlayerBeatChange>() {
                    @Override
                    public void trigger(OnSongPlayerBeatChange object) {
                        soundModel.setCurrentBeat(object.getBeat());
                    }
                });
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
