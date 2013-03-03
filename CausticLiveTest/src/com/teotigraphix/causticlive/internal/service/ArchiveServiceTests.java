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

package com.teotigraphix.causticlive.internal.service;

import roboguice.RoboGuice;
import android.test.ActivityInstrumentationTestCase2;

import com.teotigraphix.causticlive.BrowserActivity;
import com.teotigraphix.causticlive.service.IArchiveService;

public class ArchiveServiceTests extends ActivityInstrumentationTestCase2<BrowserActivity> {

    private BrowserActivity activity;

    private IArchiveService archiveService;

    public ArchiveServiceTests() {
        super(BrowserActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();
        archiveService = RoboGuice.getInjector(activity).getInstance(IArchiveService.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParse() {
        // Singletons need Provider<Context>
    }

}
