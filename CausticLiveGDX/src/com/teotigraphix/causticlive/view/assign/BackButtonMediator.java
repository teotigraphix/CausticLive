
package com.teotigraphix.causticlive.view.assign;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.ISequencerModel.OnSequencerModelPropertyChange;
import com.teotigraphix.causticlive.screen.ICausticLiveScreen;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.OverlayButton;

public class BackButtonMediator extends ScreenMediator {

    @Inject
    IApplicationModel applicationModel;

    @Inject
    ISequencerModel sequencerModel;

    private Label titleLabel;

    private TextButton nameButton;

    private TextField nameField;

    public BackButtonMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        final Stage stage = screen.getStage();
        OverlayButton button = new OverlayButton("Back", screen.getSkin());
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

        nameButton = new TextButton("Change Name", screen.getSkin());
        nameButton.setPosition(5f, 600f);
        nameButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                Gdx.input.getTextInput(new TextInputListener() {
                    @Override
                    public void input(String text) {
                        getController().getLogger().log("BackButtonMediator", text);
                    }

                    @Override
                    public void canceled() {
                        getController().getLogger().log("BackButtonMediator",
                                "Name change Canceled");
                    }
                }, "Enter a new name", sequencerModel.getActiveData().getViewChannel().getTone()
                        .getName());
            }
        });
        stage.addActor(nameButton);

        nameField = new TextField("Name", screen.getSkin());
        nameField.setTextFieldListener(new TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if (key == '\n')
                    textField.getOnscreenKeyboard().show(false);
                getController().getLogger().log("BackButtonMediator", nameField.getText());
            }
        });
        stage.addActor(nameField);
        nameField.setPosition(10f, 675f);
    }

    @Override
    public void onAttach(IScreen screen) {
        super.onAttach(screen);

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
