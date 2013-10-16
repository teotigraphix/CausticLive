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

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ToggleButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.view.UI;
import com.teotigraphix.causticlive.view.UI.Component;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Led;

public class TransportBarMediator extends ScreenMediator {

    @Inject
    ISequencerModel sequencerModel;

    private ToggleButton playButton;

    private Led redLed;

    private Led greenLed;

    private Stack stack;

    public TransportBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        Table table = UI.createComponent(screen, Component.TransportBar);
        //table.setBackground(screen.getSkin().getDrawable("toolbar_background"));

        playButton = createPlayButton(screen.getSkin());
        table.add(playButton).size(70f, 30f);

        // spacer
        table.add().expand();

        // beat led
        stack = createBeatLed(screen.getSkin());
        table.add(stack).size(30f, 30f);
    }

    private ToggleButton createPlayButton(Skin skin) {
        ToggleButton button = new ToggleButton("Play", skin);
        button.setToggle(true);
        //button.setPosition(5f, 5f);
        //button.setSize(75f, 50f);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (playButton.isChecked()) {
                    sequencerModel.play();
                } else {
                    sequencerModel.stop();
                }
            }
        });

        return button;
    }

    private Stack createBeatLed(Skin skin) {
        Stack stack = new Stack();

        redLed = new Led(skin);
        redLed.setStyleName("led-red");

        greenLed = new Led(skin);
        greenLed.setStyleName("led-green");
        greenLed.setShowOnlyOn(true);

        stack.add(redLed);
        stack.add(greenLed);

        return stack;
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
    public void onShow(IScreen screen) {
        super.onShow(screen);

        playButton.check(getController().getRack().getSystemSequencer().isPlaying());
    }

    @Override
    public void onDispose(IScreen screen) {
        super.onDispose(screen);

        playButton.setChecked(false);
    }
}
