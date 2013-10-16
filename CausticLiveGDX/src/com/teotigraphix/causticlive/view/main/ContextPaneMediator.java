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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.view.UI;
import com.teotigraphix.causticlive.view.UI.Component;
import com.teotigraphix.causticlive.view.main.panes.ControlsPaneMediator;
import com.teotigraphix.causticlive.view.main.panes.LibraryPaneMediator;
import com.teotigraphix.causticlive.view.main.panes.MixerPaneMediator;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Pane;
import com.teotigraphix.libgdx.ui.PaneStack;

@Singleton
public class ContextPaneMediator extends ScreenMediator {

    @Inject
    LibraryPaneMediator libraryPaneMediator;

    @Inject
    MixerPaneMediator mixerPaneMediator;

    @Inject
    ControlsPaneMediator controlsPaneMediator;

    //----------------------------------

    private PaneStack paneStack;

    public ContextPaneMediator() {
    }

    @Override
    public void onInitialize(IScreen screen) {
        super.onInitialize(screen);

        addMediator(libraryPaneMediator);
        addMediator(mixerPaneMediator);
        addMediator(controlsPaneMediator);
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        Skin skin = screen.getSkin();

        Table table = UI.createComponent(screen, Component.ContextPane);

        paneStack = new PaneStack(skin, Align.top);

        // LibraryPane
        Pane pane1 = new Pane(skin, "Library");
        libraryPaneMediator.onCreate(screen, pane1);
        paneStack.addPane(pane1);

        // MixerPane
        Pane pane2 = new Pane(skin, "Mixer");
        mixerPaneMediator.onCreate(screen, pane2);
        paneStack.addPane(pane2);

        // ControlsPane
        Pane pane3 = new Pane(skin, "Controls");
        controlsPaneMediator.onCreate(screen, pane3);
        paneStack.addPane(pane3);

        table.add(paneStack).fill().expand();
    }
}
