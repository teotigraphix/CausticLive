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

package com.teotigraphix.causticlive.internal.model;

import java.util.Map;
import java.util.TreeMap;

import roboguice.inject.ContextSingleton;

import com.google.inject.Inject;
import com.teotigraphix.android.components.IPadMatrix.PadData;
import com.teotigraphix.android.components.support.PadButton;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.song.PatternQueue.PatternQueueData;
import com.teotigraphix.caustic.sequencer.IPhrase;
import com.teotigraphix.caustic.song.ITrack;
import com.teotigraphix.causticlive.model.IApplicationModel;
import com.teotigraphix.causticlive.model.IUserPhraseModel;

/*

This model handles the phrase assignments to pads in the pad matrix
- the combination can be any Track/Phrase combination


    [1] [2] [3] [4]
[1]  0   1   2   3
[2]  4   5   6   7
[3]  8   9   10  11
[4]  12  13  14  15

index = (row * column) - 1

 

Map<0-15, 


*/

@ContextSingleton
public class UserPhraseModel implements IUserPhraseModel {

    private int mGroup = -1;

    Map<Integer, Map<Integer, IPhrase>> mGroupMap;

    private Map<Integer, IPhrase> mActivePhraseMap;

    @Inject
    IApplicationModel model;

    @Override
    public int getGroup() {
        return mGroup;
    }

    @Override
    public void setGroup(int value) {
        mGroup = value;
        mActivePhraseMap = mGroupMap.get(mGroup);
    }

    @Override
    public IPhrase getPhrase(int index) {
        return mActivePhraseMap.get(index);
    }

    public UserPhraseModel() {
        clear();
        setGroup(0);
    }

    @Override
    public void clear() {
        mGroupMap = new TreeMap<Integer, Map<Integer, IPhrase>>();
        for (int i = 0; i < 4; i++) {
            mGroupMap.put(i, new TreeMap<Integer, IPhrase>());
        }
    }

    @Override
    public void putPhrase(int index, int track, int bank, int pattern) throws CausticException {
        ITrack instance = model.getTrack(track);
        IPhrase phrase = instance.getPart().getTone().getSequencer().getPhraseAt(bank, pattern);
        mActivePhraseMap.put(index, phrase);
    }

    @Override
    public PatternQueueData createData(ITrack track, IPhrase phrase, PadButton button) {
        PatternQueueData result = new PatternQueueData(track, phrase, button);
        result.setLoop(true);
        PadData data = (PadData)button.getTag();
        data.setData(result);
        return result;
    }
}
