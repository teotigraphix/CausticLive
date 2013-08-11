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

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.teotigraphix.caustic.config.JavaFXRuntimeModule;
import com.teotigraphix.causticlive.CausticLiveApplication;
import com.teotigraphix.causticlive.model.ChannelModel;
import com.teotigraphix.causticlive.model.IChannelModel;
import com.teotigraphix.causticlive.model.IPadMapModel;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.model.PadMapModel;
import com.teotigraphix.causticlive.model.PadModel;
import com.teotigraphix.causticlive.model.SoundModel;
import com.teotigraphix.caustk.application.CaustkConfigurationBase;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class ApplicationModule extends JavaFXRuntimeModule {

    @Override
    protected void configureApplicationRequirements() {
        // Config
        bind(ICaustkConfiguration.class).to(ApplicationConfiguration.class).in(Singleton.class);

        // Binds our resource bundle that contains localized Strings
        bind(ResourceBundle.class).annotatedWith(Names.named("resources")).toInstance(
                ResourceBundle.getBundle(CausticLiveApplication.class.getName()));

        // Application
        bind(IPadModel.class).to(PadModel.class).in(Singleton.class);
        bind(IPadMapModel.class).to(PadMapModel.class).in(Singleton.class);
        bind(ISoundModel.class).to(SoundModel.class).in(Singleton.class);
        bind(IChannelModel.class).to(ChannelModel.class).in(Singleton.class); 
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
