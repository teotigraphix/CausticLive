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

package com.teotigraphix.causticlive.config;

import com.google.inject.Singleton;
import com.teotigraphix.caustic.activity.ICausticBackend;
import com.teotigraphix.caustic.activity.ICausticConfiguration;
import com.teotigraphix.caustic.internal.actvity.DefaultCausticBackend;
import com.teotigraphix.caustic.internal.song.ProjectData;
import com.teotigraphix.caustic.part.ISoundGenerator;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.caustic.song.IWorkspace;

@Singleton
public class CausticLiveConfiguration implements ICausticConfiguration {

    @Override
    public ICausticBackend createBackend() {
        return new CausticLiveBackend();
    }

    class CausticLiveBackend extends DefaultCausticBackend {

        @Override
        public int returnCausticCoreKey() {
            return 0x041C0899;
        }

        @Override
        public IRack createRack(IWorkspace workpace) {
            return super.createRack(workpace);
        }

        @Override
        public ISoundGenerator createSoundGenerator(IWorkspace workpace) {
            return super.createSoundGenerator(workpace);
        }

        @Override
        public ProjectData createProjectData() {
            return new CausticLiveProjectData();
        }
    }
}
