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

import java.io.File;
import java.io.IOException;

import roboguice.RoboGuice;
import android.test.ActivityInstrumentationTestCase2;

import com.teotigraphix.caustic.activity.IApplicationConfiguration;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.service.IFileService;
import com.teotigraphix.causticlive.BrowserActivity;
import com.teotigraphix.causticlive.service.IArchiveService;
import com.teotigraphix.common.utils.RuntimeUtils;

public class ArchiveServiceTests extends ActivityInstrumentationTestCase2<BrowserActivity> {

    private BrowserActivity activity;

    private IArchiveService archiveService;

    private IFileService fileService;

    public ArchiveServiceTests() {
        super(BrowserActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        IApplicationConfiguration.Test.TEST_MODE = true;
        activity = getActivity();
        archiveService = RoboGuice.getInjector(activity).getInstance(IArchiveService.class);
        fileService = RoboGuice.getInjector(activity).getInstance(IFileService.class);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        IApplicationConfiguration.Test.TEST_MODE = false;
    }

    public void testParse() throws CausticException, IOException {
        File source = RuntimeUtils.getCausticSongsDirectory();
        File target = fileService.getLibrariesDirectory();
        archiveService.parse(source, target);

    }

}
