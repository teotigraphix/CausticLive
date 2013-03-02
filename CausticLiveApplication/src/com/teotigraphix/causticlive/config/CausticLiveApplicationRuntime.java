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

package com.teotigraphix.causticlive.config;

import java.io.IOException;

import android.content.SharedPreferences;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IApplicationPreferences;
import com.teotigraphix.caustic.internal.actvity.DefaultApplicationRuntime;
import com.teotigraphix.caustic.song.IWorkspace;

public class CausticLiveApplicationRuntime extends DefaultApplicationRuntime {

    @Inject
    IApplicationPreferences applicationPreferences;

    public CausticLiveApplicationRuntime(IWorkspace workspace) {
        super(workspace);
    }

    @Override
    protected void copyFirstRun() throws IOException {
    }

    @Override
    public boolean install() throws IOException {
        return super.install();
    }

    @Override
    public void boot() throws IOException {
        SharedPreferences prefs = getWorkspace().getPreferences();
        applicationPreferences.bootPreferences(prefs);
    }

    @Override
    public void run() {
        // load all things from the Workspace preferences here at startup
        // - load the last project file but DO NOT restore the project

    }
}
