
package com.teotigraphix.causticlive.mediator.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;

public class TransportMediator extends DesktopMediatorBase {

    private ToggleButton playPauseButton;

    public TransportMediator() {
    }

    @Override
    public void create(Pane root) {
        playPauseButton = (ToggleButton)root.lookup("#playPauseButton");
        playPauseButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                onPlayPuseSelected();
            }
        });
    }

    protected void onPlayPuseSelected() {
        if (playPauseButton.isSelected()) {
            getController().getSystemSequencer().play(SequencerMode.PATTERN);
        } else {
            getController().getSystemSequencer().stop();
        }
    }

    @Override
    public void onRegister() {
    }

}
