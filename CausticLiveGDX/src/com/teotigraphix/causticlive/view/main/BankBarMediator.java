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

import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.ISequencerModel.OnSequencerModelPropertyChange;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ButtonBar;
import com.teotigraphix.libgdx.ui.ButtonBar.OnButtonBarListener;

public class BankBarMediator extends ScreenMediator {

    @Inject
    ISequencerModel sequencerModel;

    private ButtonBar view;

    public BankBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        view = new ButtonBar(screen.getSkin(), sequencerModel.getBankNames(), true, "default");
        view.setOnButtonBarListener(new OnButtonBarListener() {
            @Override
            public void onChange(int index) {
                sequencerModel.setSelectedBank(index);
            }
        });
        view.setPosition(540f, 365f);
        view.setSize(50f, 250f);
        screen.getStage().addActor(view);
    }

    @Override
    public void onAttach(IScreen screen) {
        super.onAttach(screen);

        register(sequencerModel, OnSequencerModelPropertyChange.class,
                new EventObserver<OnSequencerModelPropertyChange>() {
                    @Override
                    public void trigger(OnSequencerModelPropertyChange object) {
                        //view.updateSelection(sequencerModel.getSelectedBank(), true);
                    }
                });
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);

        view.select(sequencerModel.getSelectedBank(), true);
    }
}
