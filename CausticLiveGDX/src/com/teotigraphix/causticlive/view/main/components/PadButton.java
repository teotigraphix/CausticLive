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

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Pools;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.libgdx.ui.OverlayButton.OnPadButtonListener;

public class PadButton extends TextButton {

    protected boolean longPressed;

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    private PadButtonStyle style;

    @Override
    public PadButtonStyle getStyle() {
        return style;
    }

    //----------------------------------
    // properties
    //----------------------------------

    Map<String, Object> properties;

    public final Map<String, Object> getProperties() {
        if (properties == null)
            properties = new HashMap<String, Object>();
        return properties;
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

    public boolean isSelected() {
        return false;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public PadButton(String text, Skin skin) {
        super(text, skin);
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

                if (isDisabled() /*|| queueData == null*/)
                    return;

                ChangeEvent changeEvent = Pools.obtain(ChangeEvent.class);
                if (fire(changeEvent)) {
                }
                Pools.free(changeEvent);
            }

            @Override
            public boolean longPress(Actor actor, float x, float y) {
                longPressed = true;
                listener.onLongPress((Integer)getProperties().get("index"), x, y);
                return true;
            }
        });
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
                setText(tone.getName());
            }
        } else {
            setText("Unassigned");
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
