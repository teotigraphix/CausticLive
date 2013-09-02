
package com.teotigraphix.causticlive.config;

import com.google.inject.Singleton;
import com.teotigraphix.causticlive.application.ApplicationMediator;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.model.LibraryModel;
import com.teotigraphix.causticlive.model.SequencerModel;
import com.teotigraphix.causticlive.model.SoundModel;
import com.teotigraphix.caustk.controller.ICaustkConfiguration;
import com.teotigraphix.caustk.controller.core.CaustkConfigurationBase;
import com.teotigraphix.libgdx.application.IApplicationMediator;
import com.teotigraphix.libgdx.config.CausticRuntimeModule;
import com.teotigraphix.libgdx.dialog.DialogManager;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;
import com.teotigraphix.libgdx.scene2d.ScreenProvider;

public class CausticLiveModule extends CausticRuntimeModule {

    @Override
    protected void configurePlatformRequirements() {
        bind(IApplicationMediator.class).to(ApplicationMediator.class).in(Singleton.class);
    }

    @Override
    protected void configureApplicationRequirements() {
        // Config
        bind(ICaustkConfiguration.class).to(ApplicationConfiguration.class).in(Singleton.class);

        bind(IDialogManager.class).to(DialogManager.class).in(Singleton.class);
        bind(IScreenProvider.class).to(ScreenProvider.class).in(Singleton.class);

        bind(ISequencerModel.class).to(SequencerModel.class).in(Singleton.class);
        bind(ISoundModel.class).to(SoundModel.class).in(Singleton.class);
        bind(ILibraryModel.class).to(LibraryModel.class).in(Singleton.class);
    }

    public static class ApplicationConfiguration extends CaustkConfigurationBase {

        @Override
        protected void initialize() {
            setApplicationId("causticlive");
        }
    }
}
