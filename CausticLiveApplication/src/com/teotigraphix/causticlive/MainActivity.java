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

package com.teotigraphix.causticlive;

import roboguice.RoboGuice;
import roboguice.inject.ContentView;
import android.os.Bundle;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.teotigraphix.caustic.activity.CausticActivity;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.internal.song.Workspace;
import com.teotigraphix.causticlive.config.CausticLiveModule;
import com.teotigraphix.causticlive.internal.controller.ApplicationHandlers;

@ContentView(R.layout.activity_main)
public class MainActivity extends CausticActivity {

    @Inject
    IApplicationController applicationController;

    ApplicationHandlers applicationHandlers;

    @Override
    protected Module createApplicationModule() {
        return new CausticLiveModule();
    }

    @Override
    protected void createModule(Bundle state) {
        super.createModule(state);
        if (!Workspace.TEST_MODE) {
            applicationHandlers = new ApplicationHandlers();
            RoboGuice.injectMembers(this, applicationHandlers);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

    }
}
