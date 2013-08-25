
package com.teotigraphix.causticlive.config;

import java.io.File;

import com.google.inject.Singleton;
import com.teotigraphix.causticlive.application.ApplicationMediator;
import com.teotigraphix.caustk.application.CaustkConfigurationBase;
import com.teotigraphix.caustk.application.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.sound.ISoundGenerator;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.libgdx.application.IApplicationMediator;
import com.teotigraphix.libgdx.config.CausticRuntimeModule;

public class CausticLiveModule extends CausticRuntimeModule {

    @Override
    protected void configurePlatformRequirements() {
        bind(IApplicationMediator.class).to(ApplicationMediator.class).in(Singleton.class);
    }

    @Override
    protected void configureApplicationRequirements() {
        // Config
        bind(ICaustkConfiguration.class).to(ApplicationConfiguration.class).in(Singleton.class);

    }

    public static class ApplicationConfiguration extends CaustkConfigurationBase {

        private ISoundGenerator soundGenerator;

        @Override
        public void setSoundGenerator(ISoundGenerator soundGenerator) {
            this.soundGenerator = soundGenerator;
        }

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

        @Override
        public ISoundGenerator createSoundGenerator(ICaustkController controller) {
            return soundGenerator;
        }
    }
}
