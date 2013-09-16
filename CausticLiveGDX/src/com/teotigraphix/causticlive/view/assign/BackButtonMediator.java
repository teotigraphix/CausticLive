
package com.teotigraphix.causticlive.view.assign;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.ISequencerModel.OnSequencerModelPropertyChange;
import com.teotigraphix.causticlive.screen.ICausticLiveScreen;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.OldSelectButton;

public class BackButtonMediator extends CaustkMediator {

    @Inject
    IApplicationModel applicationModel;

    @Inject
    ISequencerModel sequencerModel;

    private Label titleLabel;

    public BackButtonMediator() {
    }

    @Override
    public void create(IScreen screen) {
        final Stage stage = screen.getStage();
        OldSelectButton button = new OldSelectButton("Back", screen.getSkin());
        button.setPosition(1090f, 705f);
        button.setSize(100f, 40f);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                applicationModel.setScreen(ICausticLiveScreen.MAIN_SCREEN);
            }
        });
        stage.addActor(button);

        titleLabel = new Label("Hello", screen.getSkin());
        titleLabel.setPosition(10f, 720f);
        stage.addActor(titleLabel);
    }

    @Override
    public void onAttach() {
        register(sequencerModel, OnSequencerModelPropertyChange.class,
                new EventObserver<OnSequencerModelPropertyChange>() {
                    @Override
                    public void trigger(OnSequencerModelPropertyChange object) {
                        refreshTitle();
                    }
                });
    }

    @Override
    public void onShow(IScreen screen) {
        refreshTitle();
    }

    protected void refreshTitle() {
        QueueData activeData = sequencerModel.getActiveData();
        titleLabel.setText(activeData.toString());
    }
}
