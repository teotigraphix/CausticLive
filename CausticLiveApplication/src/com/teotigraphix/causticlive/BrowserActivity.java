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
import roboguice.inject.ContentView;
import android.os.Bundle;

import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.IBrowserHandlers;

@ContentView(R.layout.activity_browser)
public class BrowserActivity extends RoboActivity {

    @Inject
    IBrowserHandlers browserHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //        if (!IApplicationConfig.Test.TEST_MODE) {
        //            BroswerActivityHandlers applicationHandlers = new BroswerActivityHandlers();
        //            RoboGuice.injectMembers(this, applicationHandlers);
        //        }
        super.onCreate(savedInstanceState);
    }

}
