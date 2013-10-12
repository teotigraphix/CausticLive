
package com.teotigraphix.causticlive.view.main;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog;
import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog.OnAlertDialogListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.view.main.components.PartMixer;
import com.teotigraphix.causticlive.view.main.components.PartMixer.OnPartMixerListener;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.sound.mixer.SoundMixerChannel;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Led;

public class ToolBarMediator extends ScreenMediator {

    @Inject
    IDialogManager dialogManager;

    private Led redLed;

    private Led greenLed;

    private TextButton mixerButton;

    private Skin skin;

    private PartMixer partMixer;

    private IScreen screen;

    public ToolBarMediator() {
    }

    @Override
    public void onAttach(IScreen screen) {
        register(getController(), OnSystemSequencerBeatChange.class,
                new EventObserver<OnSystemSequencerBeatChange>() {
                    @Override
                    public void trigger(OnSystemSequencerBeatChange object) {
                        final float beat = object.getBeat();
                        if (beat % 4 == 0) {
                            greenLed.turnOn(0.05f); // 0.2 is smooth at 60 - 100
                        } else {
                            redLed.turnOn(0.05f); // 0.2 is smooth at 60 - 100
                        }
                    }
                });
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);
        this.screen = screen;
        skin = screen.getSkin();

        Table table = new Table();
        //table.debug();

        createBeatLed(table, screen.getSkin());
        createMixerButton(table, screen.getSkin());
        float prefHeight = table.getPrefHeight();

        int height = Gdx.graphics.getHeight();
        table.setPosition(0, height - prefHeight);
        table.setSize(Gdx.graphics.getWidth(), prefHeight);
        screen.getStage().addActor(table);
    }

    private void createMixerButton(Table table, Skin skin) {
        mixerButton = new TextButton("Mixer", skin);
        mixerButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                createOrShowMixer();
            }
        });
        table.add(mixerButton);
    }

    protected void createOrShowMixer() {
        partMixer = new PartMixer(skin);
        partMixer.setOnPartMixerListener(new OnPartMixerListener() {
            @Override
            public void onSliderChange(int buttonIndex, int sliderIndex, float sliderValue) {
                SoundMixerChannel channel = getController().getRack().getSoundMixer()
                        .getChannel(sliderIndex);
                switch (buttonIndex) {
                    case 0:
                        channel.setVolume(sliderValue);
                        break;

                    case 1:
                        channel.setPan(sliderValue);
                        break;

                    case 2:
                        channel.setReverbSend(sliderValue);
                        break;

                    case 3:
                        channel.setDelaySend(sliderValue);
                        break;

                    case 4:
                        channel.setStereoWidth(sliderValue);
                        break;
                }
            }

            @Override
            public void onButtonChange(int index) {
                updateSliders(index);
            }
        });

        AlertDialog dialog = dialogManager.createDialog(screen, "Part Mixer", partMixer);
        dialog.setOnAlertDialogListener(new OnAlertDialogListener() {
            @Override
            public void onOk() {

            }

            @Override
            public void onCancel() {

            }
        });
        //dialog.size(500f);
        dialog.show(screen.getStage());

        updateSliders(0);
    }

    protected void updateSliders(int index) {
        for (int i = 0; i < 6; i++) {
            SoundMixerChannel channel = getController().getRack().getSoundMixer().getChannel(i);
            Slider slider = partMixer.getSliderAt(i);
            if (channel != null) {
                switch (index) {
                    case 0:
                        slider.setRange(0f, 2f);
                        slider.setValue(channel.getVolume());
                        break;
                    case 1:
                        slider.setRange(-1f, 1f);
                        slider.setValue(channel.getPan());
                        break;
                    case 2:
                        slider.setRange(0f, 1f);
                        slider.setValue(channel.getReverbSend());
                        break;
                    case 3:
                        slider.setRange(0f, 1f);
                        slider.setValue(channel.getDelaySend());
                        break;
                    case 4:
                        slider.setRange(0f, 1f);
                        slider.setValue(channel.getStereoWidth());
                        break;
                }
            }
        }
    }

    private void createBeatLed(Table table, Skin skin) {
        Stack stack = new Stack();

        redLed = new Led(skin);
        redLed.setStyleName("led-red");

        greenLed = new Led(skin);
        greenLed.setStyleName("led-green");
        greenLed.setShowOnlyOn(true);

        stack.add(redLed);
        stack.add(greenLed);

        table.add(stack).size(30f, 30f);
    }

}
