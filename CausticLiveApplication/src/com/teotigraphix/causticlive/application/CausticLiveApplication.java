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

package com.teotigraphix.causticlive.application;

import roboguice.RoboGuice;
import android.app.Application;

import com.google.inject.Inject;
import com.google.inject.Module;
import com.teotigraphix.caustic.activity.IApplicationConfiguration;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.causticlive.config.CausticLiveModule;

public class CausticLiveApplication extends Application {

    @Inject
    IWorkspace workspace;

    @Inject
    IApplicationConfiguration configuration;

    @Override
    public void onCreate() {
        super.onCreate();

        createModule();

        //RoboGuice.injectMembers(getApplicationContext(), this);
        //        try {
        //            workspace.startAndRun();
        //        } catch (CausticException e) {
        //            Log.e("CausticLiveApplication", "workspace.startAndRun()", e);
        //        }

    }

    protected void createModule() {
        Module module = createApplicationModule();
        if (module != null) {
            loadApplicationModule(this, module);
        }
    }

    private void loadApplicationModule(Application application, Module module) {
        RoboGuice.setBaseApplicationInjector(application, RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(application), module);
    }

    protected Module createApplicationModule() {
        return new CausticLiveModule();
    }
}
