
package com.teotigraphix.causticlive.view.assign;

import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.view.assign.components.MachineButtonBar;
import com.teotigraphix.causticlive.view.assign.components.MachineButtonBar.OnMachineButtonBarListener;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;

public class MachineButtonBarMediator extends ScreenMediator {

    MachineButtonBar view;

    @Inject
    ISequencerModel sequencerModel;

    @Inject
    ILibraryModel libraryModel;

    public MachineButtonBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        view = new MachineButtonBar(screen.getSkin());
        view.setPosition(5f, 5f);
        view.setSize(screen.getStage().getWidth() - 10f, 75f);
        view.setOnMachineButtonBarListener(new OnMachineButtonBarListener() {
            @Override
            public void onMachineChange(int index) {
                QueueData activeData = sequencerModel.getActiveData();
                if (activeData.getViewChannelIndex() == index)
                    return;
                libraryModel.assignTone(index, activeData);
            }
        });
        screen.getStage().addActor(view);
    }

    @Override
    public void onShow(IScreen screen) {
        int index = sequencerModel.getActiveData().getViewChannelIndex();
        if (index == -1) {
            sequencerModel.getActiveData().setViewChannelIndex(0);
            index = 0;
        }
        view.select(index);
    }

}
