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

import java.io.File;
import java.util.ResourceBundle;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.teotigraphix.caustic.application.ApplicationProvider;
import com.teotigraphix.caustic.controller.ApplicationController;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.model.ApplicationModel;
import com.teotigraphix.caustic.model.IApplicationModel;
import com.teotigraphix.caustic.model.IStageModel;
import com.teotigraphix.caustic.model.StageModel;
import com.teotigraphix.caustic.screen.IScreenManager;
import com.teotigraphix.caustic.screen.ScreenManager;
import com.teotigraphix.causticlive.CausticLiveApplication;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.model.PadModel;
import com.teotigraphix.causticlive.model.SoundModel;
import com.teotigraphix.caustk.application.CaustkConfigurationBase;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        // Core 
        bind(IApplicationModel.class).to(ApplicationModel.class).in(Singleton.class);
        bind(IApplicationController.class).to(ApplicationController.class).in(Singleton.class);

        bind(IScreenManager.class).to(ScreenManager.class).in(Singleton.class);

        // JavaFX
        bind(IStageModel.class).to(StageModel.class).in(Singleton.class);

        // Binds our resource bundle that contains localized Strings
        bind(ResourceBundle.class).annotatedWith(Names.named("resources")).toInstance(
                ResourceBundle.getBundle(CausticLiveApplication.class.getName()));

        // Application
        bind(ICaustkConfiguration.class).to(ApplicationConfiguration.class).in(Singleton.class);
        bind(ICaustkApplicationProvider.class).to(ApplicationProvider.class).in(Singleton.class);

        bind(IPadModel.class).to(PadModel.class).in(Singleton.class);
        bind(ISoundModel.class).to(SoundModel.class).in(Singleton.class);
    }

    public static class ApplicationConfiguration extends CaustkConfigurationBase {

        @Override
        public String getApplicationId() {
            return "causticlive";
        }

        @Override
        public void setCausticStorage(File value) {
            super.setCausticStorage(value);
            RuntimeUtils.STORAGE_ROOT = value.getAbsolutePath();
        }

        public ApplicationConfiguration() {
        }

    }

}
