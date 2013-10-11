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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.CausticLiveApplicationState;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.caustk.controller.IRack;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.libgdx.application.ApplicationMediatorBase;
import com.teotigraphix.libgdx.application.IApplicationMediator;
import com.teotigraphix.libgdx.model.ApplicationModelState;

@Singleton
public class ApplicationMediator extends ApplicationMediatorBase implements IApplicationMediator {

    @Inject
    ILibraryModel libraryModel;

    //    @Inject
    //    ISoundModel soundModel;

    @Inject
    ISequencerModel sequencerModel;

    //
    //    @Inject
    //    IToneModel toneModel;

    public ApplicationMediator() {
        stateType = CausticLiveApplicationState.class;
        deleteCausticFile = false;
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
    protected void onLoad() {
        super.onLoad();

        // get any references from the deseralized rack

        // setup app specific commands

        // register Models
        applicationModel.registerModel(sequencerModel);
        applicationModel.registerModel(libraryModel);
    }

    @Override
    protected void onRun() {
        super.onRun();

        if (isFirstRun()) {
            File file = RuntimeUtils.getCausticSongFile("C2DEMO");
            try {
                libraryModel.importSong(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CausticException e) {
                e.printStackTrace();
            }
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
}
