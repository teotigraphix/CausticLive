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

import com.google.inject.ImplementedBy;
import com.teotigraphix.caustic.internal.song.PatternQueue;
import com.teotigraphix.caustic.internal.song.PatternQueue.PatternQueueData;
import com.teotigraphix.caustic.song.ITrack;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.causticlive.internal.model.ApplicationModel;

@ImplementedBy(ApplicationModel.class)
public interface IApplicationModel {

    IWorkspace getWorkspace();

    void loadSong(String absolutePath);

    PatternQueue getQueue();

    ITrack getTrack(int trackIndex);

    public static class OnSongRestoreEvent {
        public OnSongRestoreEvent() {
        }
    }

    public static class OnPatternQueueChange {
        private final PatternQueueData data;

        private final PatternQueueChangeKind kind;

        public PatternQueueData getData() {
            return data;
        }

        public PatternQueueChangeKind getKind() {
            return kind;
        }

        public OnPatternQueueChange(PatternQueueData data, PatternQueueChangeKind kind) {
            this.data = data;
            this.kind = kind;
        }
    }

    public enum PatternQueueChangeKind {
        ADDED, REMOVED;
    }
}
