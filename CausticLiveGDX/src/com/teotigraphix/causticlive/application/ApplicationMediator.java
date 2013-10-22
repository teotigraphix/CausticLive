////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.causticlive.application;

import java.io.File;
import java.io.IOException;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.Gdx;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.CausticLiveApplicationState;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.libgdx.application.ApplicationMediatorBase;
import com.teotigraphix.libgdx.application.IApplicationMediator;
import com.teotigraphix.libgdx.controller.IApplicationController;
import com.teotigraphix.libgdx.model.ApplicationModelState;

@Singleton
public class ApplicationMediator extends ApplicationMediatorBase implements IApplicationMediator {

    @Inject
    ILibraryModel libraryModel;

    @Inject
    ISequencerModel sequencerModel;

    @Inject
    IApplicationController applicationController;

    public ApplicationMediator() {
        stateType = CausticLiveApplicationState.class;
        deleteCausticFile = false;
    }

    @Override
    public void onRegister() {
        // register Models
        applicationModel.registerModel(sequencerModel);
        applicationModel.registerModel(libraryModel);

        super.onRegister();

        register(OnApplicationMediatorNewProject.class,
                new EventObserver<OnApplicationMediatorNewProject>() {
                    @Override
                    public void trigger(OnApplicationMediatorNewProject object) {
                        // create a new project, this is probably going to be multi step
                        // with dialogs
                        getController().getLogger()
                                .log("ApplicationMediator", "Create new project");
                        createNewProject();
                    }
                });
    }

    protected void createNewProject() {
        // What has to happen?
        // - Rack needs to be cleared, can't just kill the instance because of all
        // the listeners added at startup

        // CausticLiveApplicationState needs to be recreated from scratch and saved once the
        // project is created

        try {
            final Project project = applicationController.createProject("Foo1");
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    getController().getRack().unregisterObservers();
                    applicationModel.setProject(project);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        // ProjectManager save dialog

        // project manager new project name dialog

        // project manager creates a new project, maybe sending
        // out a message that all mediators onShow() gets called will
        // update the UI accordingly
    }

    @Override
    protected void firstRun(ApplicationModelState state) {
        super.firstRun(state);

        final IRack rack = getController().getRack();

        try {
            rack.getTrackSequencer().createSong(new File("UntitledSong.ctks"));
            rack.getTrackSequencer().setCurrentTrack(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRun() {
        super.onRun();

        if (isFirstRun()) {
            libraryModel.importDemoSong();
        } else {

        }

        // load the last scene 
        try {
            libraryModel.restoreState();
        } catch (CausticException e) {
            e.printStackTrace();
        }

        SequencerMessage.SONG_END_MODE.send(getController(), 0);
    }

    public static class OnApplicationMediatorNewProject {
    }
}
