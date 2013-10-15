
package com.teotigraphix.causticlive.view.assign;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;

public class ToneSelectBoxMediator extends ScreenMediator {

    @Inject
    ISequencerModel sequencerModel;

    @Inject
    ISoundModel soundModel;

    @Inject
    ILibraryModel libraryModel;

    private SelectBox view;

    public ToneSelectBoxMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        view = new SelectBox(soundModel.getToneNames(false), screen.getSkin(), "default");
        view.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int index = view.getSelectionIndex();
                QueueData activeData = sequencerModel.getActiveData();
                if (activeData.getViewChannelIndex() == index)
                    return;
                libraryModel.assignTone(index, activeData);
            }
        });
        screen.getStage().addActor(view);

        updateSelection();
    }

    @Override
    public void onShow(IScreen screen) {
        updateSelection();
    }

    private void updateSelection() {
        int index = sequencerModel.getActiveData().getViewChannelIndex();
        if (index == -1) {
            index = 0;
            libraryModel.assignTone(index, sequencerModel.getActiveData());
        }
        view.setSelection(index);
    }

}
