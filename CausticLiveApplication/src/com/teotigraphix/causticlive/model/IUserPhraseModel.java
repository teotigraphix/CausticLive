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

package com.teotigraphix.causticlive.model;

import com.google.inject.ImplementedBy;
import com.teotigraphix.android.components.support.PadButton;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.song.PatternQueue.PatternQueueData;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.ITrack;
import com.teotigraphix.causticlive.internal.model.UserPhraseModel;

@ImplementedBy(UserPhraseModel.class)
public interface IUserPhraseModel {

    /**
     * The current phrase group that is being acted on.
     */
    int getGroup();

    /**
     * Sets the current phrase group.
     * 
     * @param index The group index (0..3).
     */
    void setGroup(int value);

    /**
     * Returns the phrase registered to index in the selected group.
     * 
     * @param index
     * @return
     */
    IPhrase getPhrase(int index);

    /**
     * Clears and reinitializes the group and phrase maps.
     */
    void clear();

    /**
     * Puts a phrase marker at the current track, bank and pattern.
     * 
     * @param index The index of the pad to assign.
     * @param track
     * @param bank
     * @param pattern
     * @throws CausticException
     */
    void putPhrase(int index, int track, int bank, int pattern) throws CausticException;

    PatternQueueData createData(ITrack track, IPhrase phrase, PadButton button);
}
