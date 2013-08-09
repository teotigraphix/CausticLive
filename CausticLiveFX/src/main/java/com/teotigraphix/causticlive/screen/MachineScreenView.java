
package com.teotigraphix.causticlive.screen;

import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.screen.DesktopScreenView;
import com.teotigraphix.causticlive.config.ApplicationConstants;
import com.teotigraphix.causticlive.mediator.machine.MachineScreenMediator;

@Singleton
public class MachineScreenView extends DesktopScreenView {

    @Inject
    MachineScreenMediator machineScreenMediator;

    @Override
    protected String getResource() {
        return ApplicationConstants.SCREEN_MACHINE_VIEW;
    }

    @Override
    public void create(Pane root) {
        root.setVisible(false);
        machineScreenMediator.create(root);
    }
}
