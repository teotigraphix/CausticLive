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

package com.teotigraphix.causticlive;

import roboguice.activity.RoboActivity;
import roboguice.event.EventManager;
import android.os.Bundle;

import com.google.inject.Inject;
import com.teotigraphix.caustic.view.Mediator.OnAttachMediatorEvent;
import com.teotigraphix.causticlive.view.song.LoadSongMediator;

public class BrowserActivity extends RoboActivity {

    //@Inject
    //ISongLibraryModel songLibraryModel;

    @Inject
    LoadSongMediator loadSongMediator;

    @Inject
    EventManager eventManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        eventManager.fire(new OnAttachMediatorEvent());
    }

}
