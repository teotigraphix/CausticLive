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

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;
import com.teotigraphix.causticlive.model.state.SequencerModelState;
import com.teotigraphix.libgdx.model.ApplicationModelState;

public class CausticLiveApplicationState extends ApplicationModelState {

    @Tag(100)
    private SequencerModelState sequencerModelState;

    public final SequencerModelState getSequencerModelState() {
        return sequencerModelState;
    }

    public CausticLiveApplicationState() {
        super();
    }

    @Override
    public void create() {
        super.create();
        sequencerModelState = new SequencerModelState(getController());
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void registerObservers() {
        super.registerObservers();
        sequencerModelState.registerObservers();
    }
}
