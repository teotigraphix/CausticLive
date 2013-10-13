
package com.teotigraphix.causticlive.view.main;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.view.main.components.PartMixer;
import com.teotigraphix.causticlive.view.main.components.PartMixer.OnPartMixerListener;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.sound.mixer.SoundMixerChannel;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Knob;
import com.teotigraphix.libgdx.ui.Led;
import com.teotigraphix.libgdx.ui.TextSlider;

public class ToolBarMediator extends ScreenMediator {

    @Inject
    IDialogManager dialogManager;

    private Led redLed;

    private Led greenLed;

    private TextButton mixerButton;

    private Skin skin;

    private PartMixer partMixer;

    private IScreen screen;

    private Dialog mixerDialog;

    private boolean updating;

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
        //        mixerButton.addListener(new ActorGestureListener() {
        //            @Override
        //            public void tap(InputEvent event, float x, float y, int count, int button) {
        //                createOrShowMixer();
        //            }
        //        });
        mixerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleMixer(mixerButton.isChecked());
            }
        });
        table.add(mixerButton);
    }

    protected void toggleMixer(boolean checked) {
        if (checked) {
            createOrShowMixer();
            mixerDialog.show(screen.getStage());
        } else {
            mixerDialog.hide();
        }
    }

    protected void createOrShowMixer() {
        if (partMixer != null)
            return;

        partMixer = new PartMixer(skin);
        partMixer.setOnPartMixerListener(new OnPartMixerListener() {
            @Override
            public void onSliderChange(int buttonIndex, int sliderIndex, float sliderValue) {
                if (updating)
                    return;
                SoundMixerChannel channel = getController().getRack().getSoundMixer()
                        .getChannel(sliderIndex);
                switch (buttonIndex) {
                    case 0:
                        channel.setVolume(sliderValue);
                        break;

                    case 1:
                        channel.setReverbSend(sliderValue);
                        break;

                    case 2:
                        channel.setDelaySend(sliderValue);
                        break;

                    case 3:
                        channel.setStereoWidth(sliderValue);
                        break;
                }
            }

            @Override
            public void onButtonChange(int index) {
                updateSliders(index);
            }

            @Override
            public void onKnobChange(int buttonIndex, int sliderIndex, float value) {
                if (updating)
                    return;
                SoundMixerChannel channel = getController().getRack().getSoundMixer()
                        .getChannel(sliderIndex);
                channel.setPan(value);
            }

            @Override
            public void onMuteChange(int buttonIndex, int sliderIndex, boolean selected) {
                if (updating)
                    return;
                SoundMixerChannel channel = getController().getRack().getSoundMixer()
                        .getChannel(sliderIndex);
                channel.setMute(selected);
            }

            @Override
            public void onSoloChange(int buttonIndex, int sliderIndex, boolean selected) {
                // TODO Auto-generated method stub

            }
        });

        mixerDialog = dialogManager.createDialog(screen, "Part Mixer", partMixer);
        partMixer.validate();
        mixerDialog.getContentTable().add(partMixer).size(partMixer.getPrefWidth());
        updateSliders(0);
    }

    protected void updateSliders(int index) {
        updating = true;
        for (int i = 0; i < 6; i++) {
            SoundMixerChannel channel = getController().getRack().getSoundMixer().getChannel(i);
            TextSlider slider = partMixer.getSliderAt(i);
            Knob panKnob = partMixer.getKnobAt(i);
            panKnob.setValue(channel.getPan());

            if (channel != null) {
                switch (index) {
                    case 0:
                        getController().getLogger().log("ToolBarMediator", "Volume");
                        slider.setRange(0f, 2f);
                        slider.setValue(channel.getVolume());
                        break;
                    case 1:
                        getController().getLogger().log("ToolBarMediator", "Reverb");
                        slider.setRange(0f, 1f);
                        slider.setValue(channel.getReverbSend());
                        break;
                    case 2:
                        getController().getLogger().log("ToolBarMediator", "Delay");
                        slider.setRange(0f, 1f);
                        slider.setValue(channel.getDelaySend());
                        break;
                    case 3:
                        getController().getLogger().log("ToolBarMediator", "Width");
                        slider.setRange(0f, 1f);
                        slider.setValue(channel.getStereoWidth());
                        break;
                }
            }
        }
        updating = false;
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
