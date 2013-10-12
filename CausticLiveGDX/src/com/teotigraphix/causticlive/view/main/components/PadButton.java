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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ToggleButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueData.QueueDataState;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.libgdx.ui.OverlayButton.OnPadButtonListener;

public class PadButton extends ToggleButton {

    protected boolean longPressed;

    private Skin skin;

    private Label beatLabel;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    private PadButtonStyle style;

    @Override
    public PadButtonStyle getStyle() {
        return style;
    }

    //----------------------------------
    // queueData
    //----------------------------------

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int value) {
        index = value;
    }

    //----------------------------------
    // queueData
    //----------------------------------

    private QueueData queueData;

    public QueueData getData() {
        return queueData;
    }

    public void setData(QueueData value) {
        queueData = value;
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public PadButton(String text, Skin skin) {
        super(text, skin);
        this.skin = skin;
        style = skin.get("default", PadButtonStyle.class);
        init();
    }

    public PadButton(String text, PadButtonStyle style) {
        super(text, style);
        init();
    }

    public PadButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
        init();
    }

    private void init() {
        clearListeners();

        addListener(new ActorGestureListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (longPressed) {
                    longPressed = false;
                    return;
                }

                if (isDisabled() || queueData == null)
                    return;

                ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
                if (fire(changeEvent)) {
                }
                Pools.free(changeEvent);
            }

            @Override
            public boolean longPress(Actor actor, float x, float y) {
                longPressed = true;
                listener.onLongPress(getIndex(), x, y);
                return true;
            }
        });

        LabelStyle labelStyle = new LabelStyle(skin.getFont("default-font"), skin.getColor("white"));
        beatLabel = new Label("beat", labelStyle);
        beatLabel.setPosition(5f, 1);
        addActor(beatLabel);
    }

    //--------------------------------------------------------------------------
    // Overridden Public :: Methods
    //--------------------------------------------------------------------------

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        Drawable queueOverlay = getStyle().queueOverlay;
        Drawable playOverlay = getStyle().playOverlay;
        Drawable lockOverlay = getStyle().lockOverlay;

        beatLabel.setColor(Color.WHITE);

        if (queueData != null) {
            switch (queueData.getState()) {
                case Queue:
                    queueOverlay.draw(batch, getX(), getY(), getWidth(), getHeight());
                    break;
                case Play:
                    playOverlay.draw(batch, getX(), getY(), getWidth(), getHeight());
                    break;
                case PlayUnqueued:
                case UnQueued:
                    lockOverlay.draw(batch, getX(), getY(), getWidth(), getHeight());
                    break;
                case Idle:
                    break;
            }

            int channel = queueData.getViewChannelIndex();
            if (channel != -1) {
                Tone tone = queueData.getRack().getSoundSource().getTone(channel);
                if (queueData.getState() == QueueDataState.Play
                        || queueData.getState() == QueueDataState.PlayUnqueued) {
                    int beat = queueData.getPhrase().getLocalBeat();
                    if (queueData.getPhrase().isLastBeat())
                        beatLabel.setColor(Color.RED);
                    setText(tone.getName());
                    int numMeasures = queueData.getPhrase().getLength();
                    beatLabel.setText((numMeasures * 4) + ":" + (beat + 1));
                } else {
                    setText(tone.getName());
                    beatLabel.setText("");
                }
            }
        } else {
            setText("Unassigned");
            beatLabel.setText("");
        }
    }

    //--------------------------------------------------------------------------
    // Events
    //--------------------------------------------------------------------------

    private OnPadButtonListener listener;

    public void setOnPadButtonListener(OnPadButtonListener l) {
        this.listener = l;
    }

    //--------------------------------------------------------------------------
    // Style
    //--------------------------------------------------------------------------

    public static class PadButtonStyle extends TextButtonStyle {

        public Drawable queueOverlay;

        public Drawable playOverlay;

        public Drawable lockOverlay;

        public PadButtonStyle() {
        }

        public PadButtonStyle(Drawable up, Drawable down, Drawable checked, BitmapFont font) {
            super(up, down, checked, font);
        }

        public PadButtonStyle(PadButtonStyle style) {
            super(style);
        }

    }
}
