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

import java.util.Collection;
import java.util.UUID;

import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueDataChannel;
import com.teotigraphix.caustk.sequencer.track.Track;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface ISequencerModel extends ICaustkModel {

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    PadState getPadState();

    /**
     * @param value
     * @see OnSequencerModelPropertyChange
     * @see PropertyChangeKind#PadState
     */
    void setPadState(PadState value);

    //----------------------------------
    // selectedBank
    //----------------------------------

    int getSelectedBank();

    /**
     * @param value
     * @see OnSequencerModelPropertyChange
     * @see PropertyChangeKind#Bank
     */
    void setSelectedBank(int value);

    String[] getBankNames();

    //----------------------------------
    // activeData
    //----------------------------------

    QueueData getActiveData();

    void setActiveData(QueueData queueData);

    //----------------------------------
    // recordMode
    //----------------------------------

    boolean isRecordMode();

    void setRecordMode(boolean value);

    //--------------------------------------------------------------------------
    // Method API
    //--------------------------------------------------------------------------

    Collection<QueueData> getViewData(int bankIndex);

    QueueData getQueueData(int bankIndex, int patternIndex);

    QueueDataChannel getChannel(int bankIndex, int patternIndex, int toneIndex);

    void play();

    void stop();

    boolean touch(QueueData data);

    void assignPhrase(QueueData data, Track track, LibraryPhrase libraryPhrase);

    void assignPhrase(QueueData data, Track track, UUID phraseId);

    void unassignPhrase(QueueData data, Track track);

    public enum PadState {
        Perform(0),

        Assign(1);

        private int value;

        public int getValue() {
            return value;
        }

        PadState(int value) {
            this.value = value;
        }

        public static PadState fromInt(int value) {
            for (PadState state : values()) {
                if (state.getValue() == value)
                    return state;
            }
            return null;
        }
    }

    public enum SequencerState {
        Play,

        Record;
    }

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    public enum PropertyChangeKind {
        PadState,

        Bank,

        ActiveData;
    }

    public static class OnSequencerModelPropertyChange {

        private final PropertyChangeKind kind;

        public final PropertyChangeKind getKind() {
            return kind;
        }

        public OnSequencerModelPropertyChange(PropertyChangeKind kind) {
            this.kind = kind;
        }
    }

}
