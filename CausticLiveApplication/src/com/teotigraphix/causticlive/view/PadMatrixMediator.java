////////////////////////////////////////////////////////////////////////////////
// Copyright 2012 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.causticlive.view;

import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;
import android.util.Log;
import android.view.View;

import com.google.inject.Inject;
import com.teotigraphix.android.components.IPadMatrix.PadData;
import com.teotigraphix.android.components.PadMatrix;
import com.teotigraphix.android.components.support.PadButton;
import com.teotigraphix.android.components.support.PadButton.OnPressedListener;
import com.teotigraphix.android.components.support.PadButton.OnSelectListener;
import com.teotigraphix.android.service.ITouchService;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.song.PatternQueue.PatternQueueData;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.ITrack;
import com.teotigraphix.caustic.view.Mediator;
import com.teotigraphix.causticlive.R;
import com.teotigraphix.causticlive.model.IApplicationModel;
import com.teotigraphix.causticlive.model.IApplicationModel.OnPatternQueueChange;
import com.teotigraphix.causticlive.model.IApplicationModel.OnSongRestoreEvent;
import com.teotigraphix.causticlive.model.IApplicationModel.PatternQueueChangeKind;
import com.teotigraphix.causticlive.model.IUserPhraseModel;

@ContextSingleton
public class PadMatrixMediator extends Mediator {

    @InjectView(R.id.pad_matrix)
    PadMatrix view;

    @Inject
    ITouchService touchService;

    @Inject
    IApplicationModel model;

    @Inject
    IUserPhraseModel phraseModel;

    public void onSongRestoreHandler(@Observes OnSongRestoreEvent event) {
        // the OnRackStateChangedEvent gets fired from the app model
        // when a racksong is successfully restored and ready to play
        try {
            updateComponents();
        } catch (CausticException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onAttachMediator() {
        Log.d("PadMatrixMediator", "onAttachMediator()");
        // this has to startup last, models need to initialize
        touchService.addTouchListener(view);
        // removed becasue the queue gets reset when a new song is loaded
        // model.getQueue().addListener(this);

    }

    private void updateComponents() throws CausticException {

        // each Track is a machine that has predefined patterns
        // these patterns will be assigned by the user, but for now
        // I have to make up patterns that exist, which means there needs
        // to be some type of model that is a middle man that "holds"
        // the assignments from the user that I can mock

        phraseModel.clear();

        phraseModel.setGroup(0);
        phraseModel.putPhrase(0, 0, 0, 0);
        phraseModel.putPhrase(1, 0, 0, 1);
        phraseModel.putPhrase(2, 0, 0, 2);
        phraseModel.putPhrase(3, 0, 0, 3);

        phraseModel.putPhrase(4, 1, 0, 0);
        phraseModel.putPhrase(5, 1, 0, 1);
        phraseModel.putPhrase(6, 1, 0, 2);
        phraseModel.putPhrase(7, 1, 0, 3);

        phraseModel.putPhrase(8, 2, 0, 0);
        phraseModel.putPhrase(9, 2, 0, 1);
        phraseModel.putPhrase(10, 2, 0, 2);
        phraseModel.putPhrase(11, 2, 0, 3);

        phraseModel.putPhrase(12, 4, 0, 0);
        phraseModel.putPhrase(13, 4, 0, 1);
        phraseModel.putPhrase(14, 4, 0, 2);
        phraseModel.putPhrase(15, 4, 0, 3);

        int len = 6;
        for (int track = 0; track < len; track++) {
            initializeTrack(track);
        }

        len = view.getChildCount();
        for (int i = 0; i < len; i++) {
            initializeButton((PadButton)view.getChildAt(i));
        }
    }

    private void initializeButton(PadButton button) {
        PadData data = (PadData)button.getTag();

        IPhrase phrase = phraseModel.getPhrase(data.getIndex());
        if (phrase == null) {
            return;
        }

        //IMachine machine = (IMachine)phrase.getSequencer().getDevice();
        //ITrack track = model.getTrack(machine.getIndex());
        //PatternQueueData qdata = phraseModel.createData(track, phrase, button);

        button.setOnPressedListener(new OnPressedListener() {
            @Override
            public void onPressed(View view, boolean pressed) {
            }
        });
        button.setOnSelectListener(new OnSelectListener() {
            @Override
            public void onSelect(View view, boolean selected) {
                doClick((PadButton)view, selected);
            }
        });
    }

    private void doClick(PadButton button, boolean selected) {
        PadData data = (PadData)button.getTag();
        if (data == null)
            return;

        PatternQueueData qdata = (PatternQueueData)data.getData();
        if (selected) {
            model.getQueue().removeOthers(qdata);
            model.getQueue().touch(qdata);
        } else {
            model.getQueue().touch(qdata);
        }
    }

    private void initializeTrack(int index) {
        ITrack track = model.getTrack(index);
        track.getPart().getTone().getSequencer().setBankPattern(3, 15);
    }

    void onPatternQueueChange(@Observes OnPatternQueueChange event) {
        if (event.getKind() == PatternQueueChangeKind.ADDED) {

        } else if (event.getKind() == PatternQueueChangeKind.REMOVED) {
            final PadButton button = getButton(event.getData());
            if (button == null)
                return;
            button.post(new Runnable() {
                @Override
                public void run() {
                    button.setSelectedNoEvent(false);
                }
            });
        }
    }

    private PadButton getButton(PatternQueueData data) {
        final int len = view.getChildCount();
        for (int i = 0; i < len; i++) {
            PadButton button = (PadButton)view.getChildAt(i);
            PadData tag = (PadData)button.getTag();
            if (tag.getData() == data)
                return button;
        }
        return null;
    }
}
