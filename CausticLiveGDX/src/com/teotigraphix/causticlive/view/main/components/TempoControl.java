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

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.teotigraphix.libgdx.ui.AutoRepeatButton;
import com.teotigraphix.libgdx.ui.AutoRepeatButton.OnValueChangeListener;
import com.teotigraphix.libgdx.ui.ControlTable;

public class TempoControl extends ControlTable {

    private AutoRepeatButton decButton;

    private Label label;

    private AutoRepeatButton incButton;

    private OnTempControlListener listener;

    public TempoControl(Skin skin) {
        super(skin);
    }

    @Override
    protected void createChildren() {
        defaults().space(5f);

        decButton = new AutoRepeatButton("-", getSkin());
        decButton.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void valueChange() {
                listener.onDecrementChange();
            }
        });

        label = new Label("", getSkin());
        label.setAlignment(Align.center);

        incButton = new AutoRepeatButton("+", getSkin());
        incButton.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void valueChange() {
                listener.onIncrementChange();
            }
        });

        add(decButton).fillY().expandX().width(35f);
        add(label).width(35f);
        add(incButton).fillY().expandY().width(35f);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public interface OnTempControlListener {
        void onDecrementChange();

        void onIncrementChange();
    }

    public void setOnTempControlListener(OnTempControlListener l) {
        listener = l;
    }
}
