
package com.teotigraphix.causticlive.view.assign;

import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.IToneModel;
import com.teotigraphix.causticlive.view.components.assign.MachineButtonBar;
import com.teotigraphix.causticlive.view.components.assign.MachineButtonBar.OnMachineButtonBarListener;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;

public class MachineButtonBarMediator extends MediatorBase {

    MachineButtonBar view;

    @Inject
    ISequencerModel sequencerModel;

    @Inject
    IToneModel toneModel;

    public MachineButtonBarMediator() {
    }

    @Override
    public void create(IScreen screen) {
        view = new MachineButtonBar(screen.getSkin());
        view.setPosition(5f, 5f);
        view.setSize(screen.getStage().getWidth() - 10f, 75f);
        view.setOnMachineButtonBarListener(new OnMachineButtonBarListener() {
            @Override
            public void onMachineChange(int index) {
                QueueData activeData = sequencerModel.getActiveData();
                toneModel.assignTone(index, activeData);
            }
        });
        screen.getStage().addActor(view);
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow(IScreen screen) {
        view.select(sequencerModel.getActiveData().getViewChannel());
    }

}
