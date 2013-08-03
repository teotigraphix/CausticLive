
package com.teotigraphix.causticlive.sceen;

import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.screen.DesktopScreenView;
import com.teotigraphix.caustic.screen.IScreenView;
import com.teotigraphix.causticlive.config.ApplicationConstants;
import com.teotigraphix.causticlive.mediator.main.BankMediator;
import com.teotigraphix.causticlive.mediator.main.FunctionGroupMediator;
import com.teotigraphix.causticlive.mediator.main.PadMediator;
import com.teotigraphix.causticlive.mediator.main.ToolBarMediator;
import com.teotigraphix.causticlive.mediator.main.TransportMediator;

@Singleton
public class MainScreenView extends DesktopScreenView implements IScreenView {

    @Inject
    ToolBarMediator toolBarMediator;

    @Inject
    FunctionGroupMediator functionGroupMediator;

    @Inject
    PadMediator padMediator;

    @Inject
    BankMediator bankMediator;

    @Inject
    TransportMediator transportMediator;

    @Override
    protected String getResource() {
        return ApplicationConstants.SCREEN_MAIN_VIEW;
    }

    @Override
    public void create(Pane root) {
        toolBarMediator.create(root);
        functionGroupMediator.create(root);
        padMediator.create(root);
        bankMediator.create(root);
        transportMediator.create(root);
    }

}
