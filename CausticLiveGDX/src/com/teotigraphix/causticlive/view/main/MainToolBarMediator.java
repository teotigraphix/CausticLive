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

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teotigraphix.causticlive.application.ApplicationMediator.OnApplicationMediatorNewProject;
import com.teotigraphix.causticlive.view.UI;
import com.teotigraphix.causticlive.view.UI.Component;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.OverlayButton;

public class MainToolBarMediator extends ScreenMediator {

    private OverlayButton loadButton;

    private OverlayButton createButton;

    private OverlayButton writeButton;

    public MainToolBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        Table table = UI.createComponent(screen, Component.MainToolBar);
        table.defaults().space(5f);
        table.setBackground("toolbar_background");

        loadButton = createLoadButton(screen.getSkin());
        table.add(loadButton).size(75f, 30f);

        createButton = createCreateButton(screen.getSkin());
        table.add(createButton).size(75f, 30f);

        writeButton = createWriteButton(screen.getSkin());
        table.add(writeButton).size(75f, 30f);

        table.add().expand();
    }

    private OverlayButton createLoadButton(Skin skin) {
        OverlayButton button = new OverlayButton("Load", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {

            }
        });
        return button;
    }

    private OverlayButton createCreateButton(Skin skin) {
        OverlayButton button = new OverlayButton("Create", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                getController().trigger(new OnApplicationMediatorNewProject());
            }
        });
        return button;
    }

    private OverlayButton createWriteButton(Skin skin) {
        OverlayButton button = new OverlayButton("Write", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {

            }
        });
        return button;
    }
}
