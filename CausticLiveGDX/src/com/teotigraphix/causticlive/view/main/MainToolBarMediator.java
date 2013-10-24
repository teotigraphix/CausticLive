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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.CausticLiveApp;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.screen.ComponentFactory;
import com.teotigraphix.causticlive.view.UI;
import com.teotigraphix.causticlive.view.UI.Component;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.OverlayButton;

public class MainToolBarMediator extends ScreenMediator {

    @Inject
    IApplicationModel applicationModel;

    @Inject
    ILibraryModel libraryModel;

    private OverlayButton optionsButton;

    private Table extrasTable;

    public MainToolBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        Table table = UI.createComponent(screen, Component.MainToolBar);

        table.defaults().space(5f);
        table.setBackground("toolbar_background");

        optionsButton = createOptionsButton(screen.getSkin());
        table.add(optionsButton).size(75f, 30f);

        table.add().expand();

        extrasTable = createExtras(screen.getSkin());
        table.add(extrasTable).fillY().expandY();
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);

        projectLabel.setText(applicationModel.getProject().getName());
    }

    private OverlayButton createOptionsButton(Skin skin) {
        OverlayButton button = new OverlayButton("Options", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                applicationModel.pushScreen(CausticLiveApp.ADMIN_SCREEN);
            }
        });
        return button;
    }

    //----------------------------------
    // Extras
    //----------------------------------

    private Label projectLabel;

    private Table createExtras(Skin skin) {
        Table table = new Table();
        // spacer for now to push the project label down
        table.add().expandY();
        table.row();

        projectLabel = createProjectLabel(skin);
        table.add(projectLabel).pad(2f);

        return table;
    }

    private Label createProjectLabel(Skin skin) {
        return ComponentFactory.createLabel(skin, "", "default-font", Color.BLACK);
    }

}
