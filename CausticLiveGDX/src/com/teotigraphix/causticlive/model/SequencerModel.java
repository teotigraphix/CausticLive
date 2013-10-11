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

package com.teotigraphix.causticlive.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.state.SequencerModelState;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueDataChannel;
import com.teotigraphix.caustk.sequencer.track.Phrase;
import com.teotigraphix.caustk.sequencer.track.Track;
import com.teotigraphix.libgdx.model.CaustkModelBase;
import com.teotigraphix.libgdx.model.IApplicationModel;

@Singleton
public class SequencerModel extends CaustkModelBase implements ISequencerModel {

    @Inject
    IApplicationModel applicationModel;

    private CausticLiveApplicationState state;

    protected final SequencerModelState getSequencerModelState() {
        return state.getSequencerModelState();
    }

    protected final IQueueSequencer getQueueSequencer() {
        return getSequencerModelState().getQueueSequencer();
    }

    @Override
    public void onRegister() {
        super.onRegister();
        state = applicationModel.getState();
    }

    //--------------------------------------------------------------------------
    // ISequencerModel API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedBank
    //----------------------------------

    @Override
    public int getSelectedBank() {
        return getSequencerModelState().getSelectedBank();
    }

    @Override
    public void setSelectedBank(int value) {
        boolean changed = getSequencerModelState().setSelectedBank(value);
        if (changed)
            trigger(new OnSequencerModelPropertyChange(PropertyChangeKind.Bank));
    }

    private final String[] items = {
            "A", "B", "C", "D"
    };

    @Override
    public final String[] getBankNames() {
        return items;
    }

    //----------------------------------
    // activeData
    //----------------------------------

    @Override
    public QueueData getActiveData() {
        return getSequencerModelState().getActiveData();
    }

    @Override
    public void setActiveData(QueueData value) {
        getSequencerModelState().setActiveData(value);
        trigger(new OnSequencerModelPropertyChange(PropertyChangeKind.ActiveData));
    }

    //----------------------------------
    // recordMode
    //----------------------------------

    @Override
    public boolean isRecordMode() {
        return getQueueSequencer().isRecordMode();
    }

    @Override
    public void setRecordMode(boolean value) {
        getQueueSequencer().setRecordMode(value);
    }

    public SequencerModel() {
    }

    @Override
    public Collection<QueueData> getViewData(int bankIndex) {
        Map<Integer, QueueData> view = getQueueSequencer().getView(bankIndex);
        // this collection may not hold all 16 datas since we lazy load
        // will contain 16 items either null or QueueData
        Collection<QueueData> collection = new ArrayList<QueueData>(view.values());
        return collection;
    }

    @Override
    public QueueData getQueueData(int bankIndex, int patternIndex) {
        return getQueueSequencer().getQueueData(bankIndex, patternIndex);
    }

    @Override
    public QueueDataChannel getChannel(int bankIndex, int patternIndex, int toneIndex) {
        return getQueueSequencer().getChannel(bankIndex, patternIndex, toneIndex);
    }

    @Override
    public void play() {
        try {
            getQueueSequencer().play();
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        getQueueSequencer().stop();
    }

    @Override
    public boolean touch(QueueData data) {
        return getQueueSequencer().touch(data);
    }

    @Override
    public void assignPhrase(QueueData data, Track track, LibraryPhrase libraryPhrase) {
        QueueDataChannel channel = data.getChannel(track.getIndex());
        Phrase phrase = track.getPhrase(channel.getBankIndex(), channel.getPatternIndex());

        int lastBank = track.getCurrentBank();
        int lastPattern = track.getCurrentPattern();

        phrase.getTrack().setCurrentBankPattern(channel.getBankIndex(), channel.getPatternIndex());

        phrase.setPhraseId(libraryPhrase.getId());
        phrase.setLength(libraryPhrase.getLength());
        phrase.setNoteData(libraryPhrase.getNoteData());

        channel.assignPhrase(phrase);

        phrase.getTrack().setCurrentBankPattern(lastBank, lastPattern);
    }

}
