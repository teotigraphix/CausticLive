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

package com.teotigraphix.causticlive.view.admin;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.CausticLiveApp;
import com.teotigraphix.causticlive.view.UI;
import com.teotigraphix.causticlive.view.UI.Component;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.OverlayButton;
import com.teotigraphix.libgdx.ui.Pane;
import com.teotigraphix.libgdx.ui.PaneStack;

@Singleton
public class OptionsPaneMediator extends ScreenMediator {

    @Inject
    ProjectPaneMediator projectPaneMediator;

    @Inject
    IApplicationModel applicationModel;

    private Skin skin;

    private PaneStack paneStack;

    private OverlayButton backButton;

    public OptionsPaneMediator() {
    }

    @Override
    public void onInitialize(IScreen screen) {
        super.onInitialize(screen);

        addMediator(projectPaneMediator);
    }

    @Override
    public void onAttach(IScreen screen) {
        super.onAttach(screen);
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);
        skin = screen.getSkin();

        Table table = UI.createComponent(screen, Component.OptionsPane);

        paneStack = new PaneStack(skin, Align.top);
        paneStack.setMaxButtonSize(100f);

        backButton = new OverlayButton("Back", skin);
        backButton.setToggle(false);
        backButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                applicationModel.pushScreen(CausticLiveApp.MAIN_SCREEN);
            }
        });

        paneStack.getToolsBar().add(backButton).height(30f).width(75f);

        // LibraryPane
        Pane pane1 = new Pane(skin, "Project");
        projectPaneMediator.onCreate(screen, pane1);
        paneStack.addPane(pane1);

        table.add(paneStack).fill().expand();
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);
    }
}
