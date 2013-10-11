
package com.teotigraphix.causticlive.view.main;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.OverlayButton;

public class TransportControlMediator extends ScreenMediator {

    @Inject
    ISequencerModel sequencerModel;

    private OverlayButton playButton;

    private OverlayButton recordButton;

    public TransportControlMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        playButton = new OverlayButton("Play", screen.getSkin());
        playButton.setToggle(true);
        playButton.setPosition(66f, 90f);
        playButton.setSize(83f, 50f);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (playButton.isChecked()) {
                    sequencerModel.play();
                } else {
                    sequencerModel.stop();
                }
            }
        });

        screen.getStage().addActor(playButton);

        recordButton = new OverlayButton("Record", screen.getSkin());
        recordButton.setPosition(170f, 90f);
        recordButton.setSize(83f, 50f);
        recordButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (recordButton.isChecked()) {
                    sequencerModel.setRecordMode(true);
                } else {
                    sequencerModel.setRecordMode(false);
                }
            }
        });

        //screen.getStage().addActor(recordButton);
    }
}
