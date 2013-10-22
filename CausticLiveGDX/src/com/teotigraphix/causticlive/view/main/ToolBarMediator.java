////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.causticlive.view.main;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.PopUp;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.screen.SkinRegistry;
import com.teotigraphix.causticlive.view.main.components.PartMixer;
import com.teotigraphix.causticlive.view.main.components.PartMixer.OnPartMixerListener;
import com.teotigraphix.caustk.rack.mixer.SoundMixerChannel;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Knob;
import com.teotigraphix.libgdx.ui.TextSlider;

public class ToolBarMediator extends ScreenMediator {

    @Inject
    ISequencerModel sequencerModel;

    @Inject
    IDialogManager dialogManager;

    private TextButton mixerButton;

    private Skin skin;

    private PartMixer partMixer;

    private IScreen screen;

    private PopUp mixerDialog;

    private boolean updating;

    private boolean isTopPlacement = true;

    public ToolBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);
        this.screen = screen;
        skin = screen.getSkin();

        Table table = new Table();
        table.setBackground(skin.getDrawable("toolbar_background"));
        //table.debug();

        //        createPlayButton(table, screen.getSkin());

        //        createBeatLed(table, screen.getSkin());
        createMixerButton(table, screen.getSkin());
        float prefHeight = 35f; //table.getPrefHeight();

        float height = SkinRegistry.getHeight();
        float width = SkinRegistry.getWidth();

        if (isTopPlacement) {
            table.setPosition(0f, height - prefHeight);
        } else {
            table.setPosition(0f, 0f);
        }

        table.setSize(width, prefHeight);
        screen.getStage().addActor(table);
    }

    private void createMixerButton(Table table, Skin skin) {
        mixerButton = new TextButton("Mixer", skin);
        mixerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleMixer(mixerButton.isChecked());
            }
        });
        table.add(mixerButton).size(70f, 30f);
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

        mixerDialog = dialogManager.createPopUp(screen, "Part Mixer", partMixer);
        mixerDialog.pad(5f);
        mixerDialog.padTop(25f);

        mixerDialog.getContentTable().add(partMixer).fill().expand();

        mixerDialog.setPosition(5f, 105f);
        mixerDialog.setExplicitSize(335f, 240f);
        mixerDialog.validate();

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

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);

        //        playButton.check(getController().getRack().getSystemSequencer().isPlaying());
    }

    @Override
    public void onDispose(IScreen screen) {
        super.onDispose(screen);

        //        playButton.setChecked(false);
    }
}
