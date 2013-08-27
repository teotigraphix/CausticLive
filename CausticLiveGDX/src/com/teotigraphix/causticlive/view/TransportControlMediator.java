
package com.teotigraphix.causticlive.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ToggleButton;

public class TransportControlMediator extends MediatorBase {

    @Inject
    ISoundModel soundModel;

    private ToggleButton playButton;

    public TransportControlMediator() {
    }

    @Override
    public void create(IScreen screen) {

        playButton = new ToggleButton("Play", screen.getSkin());
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (playButton.isChecked()) {
                    try {
                        soundModel.play();
                    } catch (CausticException e) {
                        e.printStackTrace();
                    }
                } else {
                    soundModel.stop();
                }
            }
        });
        screen.getStage().addActor(playButton);
        playButton.setPosition(66f, 90f);
        playButton.setSize(83f, 50f);
    }

    @Override
    public void onRegister() {

    }

}
