
package com.teotigraphix.causticlive.sceen;

import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.screen.DesktopScreenView;
import com.teotigraphix.causticlive.config.ApplicationConstants;
import com.teotigraphix.causticlive.mediator.machine.MachineScreenMediator;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;

@Singleton
public class MachineScreenView extends DesktopScreenView {

    @Inject
    MachineScreenMediator machineScreenMediator;

    @Override
    protected String getResource() {
        return ApplicationConstants.SCREEN_MACHINE_VIEW;
    }

    @Inject
    public MachineScreenView(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    public void create(Pane root) {
        root.setVisible(false);
        machineScreenMediator.create(root);
    }

    @Override
    public void preinitialize() {
        super.preinitialize();
        machineScreenMediator.preinitialize();
    }
}
