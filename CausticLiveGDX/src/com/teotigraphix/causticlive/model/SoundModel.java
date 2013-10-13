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

import com.google.inject.Singleton;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.libgdx.model.CaustkModelBase;

@Singleton
public class SoundModel extends CaustkModelBase implements ISoundModel {

    public SoundModel() {
    }

    @Override
    public String[] getToneNames() {
        final ISoundSource soundSource = getController().getRack().getSoundSource();
        final String[] result = new String[14];

        for (int i = 0; i < 14; i++) {
            String prefix = (i + 1) + " - ";
            final Tone tone = soundSource.getTone(i);
            String name = "Unassigned";
            if (tone != null) {
                name = tone.getName();
            }
            result[i] = prefix + name;
        }
        return result;
    }

}
