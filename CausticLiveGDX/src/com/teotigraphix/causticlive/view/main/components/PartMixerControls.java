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

package com.teotigraphix.causticlive.view.main.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ToggleButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.libgdx.ui.ControlTable;
import com.teotigraphix.libgdx.ui.Knob;
import com.teotigraphix.libgdx.ui.TextSlider;

public class PartMixerControls extends ControlTable {

    private TextSlider volumeSlider;

    public TextSlider getVolumeSlider() {
        return volumeSlider;
    }

    private Knob panKnob;

    public Knob getPanKnob() {
        return panKnob;
    }

    private ToggleButton muteButton;

    public ToggleButton getMuteButton() {
        return muteButton;
    }

    private ToggleButton soloButton;

    public ToggleButton getSoloButton() {
        return soloButton;
    }

    private int channelIndex;

    public PartMixerControls(int channelIndex, Skin skin) {
        super(skin);
        this.channelIndex = channelIndex;
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        volumeSlider = new TextSlider("", 0f, 2f, 0.01f, true, getSkin());
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listener.onSliderChange(volumeSlider.getValue());
            }
        });
        add(volumeSlider).expandY().fillY();
        row();

        // pan knob
        panKnob = new Knob(-1f, 1f, 0.01f, getSkin());
        panKnob.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listener.onKnobChange(panKnob.getValue());
            }
        });
        add(panKnob);
        row();

        // channel# mute button
        muteButton = new ToggleButton("" + (channelIndex + 1), getSkin());
        muteButton.setToggle(true);
        muteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listener.onMuteChange(muteButton.isChecked());
            }
        });

        add(muteButton).fillX().expandX().pad(3f).height(35f);
        row();

        // solo button
        soloButton = new ToggleButton("S", getSkin());
        soloButton.setToggle(true);
        soloButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listener.onSoloChange(soloButton.isChecked());
            }
        });
        add(soloButton).fillX().expandX().pad(3f);
    }

    public float getValue() {
        return volumeSlider.getValue();
    }

    private OnPartMixerControlsListener listener;

    public void setOnPartMixerControlsListener(OnPartMixerControlsListener l) {
        this.listener = l;
    }

    public interface OnPartMixerControlsListener {
        void onSliderChange(float value);

        void onKnobChange(float value);

        void onMuteChange(boolean selected);

        void onSoloChange(boolean selected);
    }

}
