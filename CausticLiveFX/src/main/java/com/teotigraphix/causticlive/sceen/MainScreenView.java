
package com.teotigraphix.causticlive.sceen;

import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.screen.DesktopScreenView;
import com.teotigraphix.caustic.screen.IScreenView;
import com.teotigraphix.causticlive.config.ApplicationConstants;
import com.teotigraphix.causticlive.mediator.main.BankMediator;
import com.teotigraphix.causticlive.mediator.main.FunctionGroupMediator;
import com.teotigraphix.causticlive.mediator.main.PadMediator2;
import com.teotigraphix.causticlive.mediator.main.ToolBarMediator;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;

@Singleton
public class MainScreenView extends DesktopScreenView implements IScreenView {

    @Inject
    ToolBarMediator toolBarMediator;

    @Inject
    FunctionGroupMediator functionGroupMediator;

    @Inject
    PadMediator2 padMediator;

    @Inject
    BankMediator bankMediator;

    @Override
    protected String getResource() {
        return ApplicationConstants.SCREEN_MAIN_VIEW;
    }

    @Inject
    public MainScreenView(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    public void create(Pane root) {
        toolBarMediator.create(root);
        functionGroupMediator.create(root);
        padMediator.create(root);
        bankMediator.create(root);
    }

    @Override
    public void preinitialize() {
        super.preinitialize();
        toolBarMediator.preinitialize();
        functionGroupMediator.preinitialize();
        padMediator.preinitialize();
        bankMediator.preinitialize();
    }

}
