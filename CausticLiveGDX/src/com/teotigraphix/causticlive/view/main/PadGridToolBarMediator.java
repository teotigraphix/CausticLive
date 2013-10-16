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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.ISequencerModel.OnSequencerModelPropertyChange;
import com.teotigraphix.causticlive.model.ISequencerModel.PadState;
import com.teotigraphix.causticlive.view.UI;
import com.teotigraphix.causticlive.view.UI.Component;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ButtonBar;
import com.teotigraphix.libgdx.ui.ButtonBar.OnButtonBarListener;

public class PadGridToolBarMediator extends ScreenMediator {

    @Inject
    ISequencerModel sequencerModel;

    private ButtonBar stateButtonBar;

    private String[] items = {
            "Perform", "Assign"
    };

    public PadGridToolBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        Table table = UI.createComponent(screen, Component.PadGridToolBar);
        table.padLeft(6f);

        stateButtonBar = createStateButtonBar(screen.getSkin());
        stateButtonBar.defaults().space(10f);
        table.add(stateButtonBar).width(190f).height(30f);

        table.add().expand();
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);
        stateButtonBar.select(sequencerModel.getPadState().getValue(), true);
    }

    @Override
    public void onAttach(IScreen screen) {
        super.onAttach(screen);

        register(sequencerModel, OnSequencerModelPropertyChange.class,
                new EventObserver<OnSequencerModelPropertyChange>() {
                    @Override
                    public void trigger(OnSequencerModelPropertyChange object) {
                        switch (object.getKind()) {
                            case PadState:
                                switch (sequencerModel.getPadState()) {
                                    case Perform:
                                        stateButtonBar.select(0, true);
                                        break;

                                    case Assign:
                                        stateButtonBar.select(1, true);
                                        break;
                                }

                                break;

                            case Bank:
                                break;

                            case ActiveData:
                                break;
                        }
                    }
                });
    }

    private ButtonBar createStateButtonBar(Skin skin) {
        ButtonBar buttonBar = new ButtonBar(skin, items, false, "default");
        buttonBar.setOnButtonBarListener(new OnButtonBarListener() {
            @Override
            public void onChange(int index) {
                switch (index) {
                    case 0:
                        sequencerModel.setPadState(PadState.Perform);
                        break;

                    case 1:
                        sequencerModel.setPadState(PadState.Assign);
                        break;
                }
            }
        });

        return buttonBar;
    }
}
