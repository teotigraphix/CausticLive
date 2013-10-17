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

import com.teotigraphix.causticlive.model.state.SequencerModelState;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.libgdx.model.RackApplicationState;

public class CausticLiveApplicationState extends RackApplicationState {

    private static final long serialVersionUID = -8033613757836089213L;

    private SequencerModelState sequencerModelState;

    public final SequencerModelState getSequencerModelState() {
        return sequencerModelState;
    }

    public CausticLiveApplicationState(ICaustkController controller) {
        super(controller);

        sequencerModelState = new SequencerModelState(controller);
    }

    @Override
    public void registerObservers() {
        super.registerObservers();
        sequencerModelState.registerObservers();
    }
}
