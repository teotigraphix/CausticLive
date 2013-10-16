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

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.causticlive.view.UI;
import com.teotigraphix.causticlive.view.UI.Component;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Pane;
import com.teotigraphix.libgdx.ui.PaneStack;
import com.teotigraphix.libgdx.ui.ScrollList;

public class ContextPaneMediator extends ScreenMediator {

    private PaneStack paneStack;

    public ContextPaneMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        Skin skin = screen.getSkin();

        Table table = UI.createComponent(screen, Component.ContextPane);

        paneStack = new PaneStack(skin, Align.top);

        Pane pane1 = new Pane(skin, "Library");

        PaneStack libraryStack = new PaneStack(skin, Align.bottom);
        libraryStack.setSelectedIndex(2); // Phrase (temp)
        pane1.add(libraryStack).expand().fill();
        libraryStack.addPane(new Pane(skin, "Scene"));
        libraryStack.addPane(new Pane(skin, "Phrase"));
        Pane patchPane = new Pane(skin, "Patch");
        libraryStack.addPane(patchPane);

        ScrollList list = new ScrollList(skin);
        list.setOverscroll(false, true);
        list.setItems(getPhraseItems());
        patchPane.add(list).fill().expand();

        paneStack.addPane(pane1);

        Pane pane2 = new Pane(skin, "Mixer");
        paneStack.addPane(pane2);

        Pane pane3 = new Pane(skin, "Controls");
        paneStack.addPane(pane3);
        pane3.setBackground("pad_selected");

        table.add(paneStack).fill().expand();
    }

    private Array<?> getPhraseItems() {
        final List<LibraryPhrase> phrases = getController().getLibraryManager()
                .getSelectedLibrary().getPhrases();
        Array<LibraryPhrase> result = new Array<LibraryPhrase>();
        for (LibraryPhrase libraryPhrase : phrases) {
            result.add(libraryPhrase);
        }
        return result;
    }
}
