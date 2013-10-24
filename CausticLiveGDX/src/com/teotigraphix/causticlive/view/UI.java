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

package com.teotigraphix.causticlive.view;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.libgdx.screen.IScreen;

public final class UI {

    public static final float PAD_GRID_PADDING = 12f;

    public static final float PAD_GRID_PAD_SIZE = 76f;

    //----------------------------------
    // 800x480
    //----------------------------------

    public static Table createComponent(IScreen screen, Component component) {
        final Rectangle bounds = component.getBounds();
        final Table table = new Table(screen.getSkin());
        //table.debug();
        table.setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        screen.getStage().addActor(table);
        return table;
    }

    public enum Component {

        //------------------------------
        // MainScreen
        //------------------------------

        MainToolBar(0, 440, 800, 40),

        ContextPaneToolBar(0, 400, 350, 40),

        MainToggle(350, 400, 50, 40),

        PadGridToolBar(400, 400, 400, 40),

        ContextPane(0, 40, 350, 360),

        TransportBar(0, 0, 350, 40),

        BankBar(350, 200, 50, 200),

        ExtrasBar(350, 0, 50, 200),

        PadGrid(400, 0, 400, 400),

        //------------------------------
        // AdminScreen
        //------------------------------

        OptionsPane(0, 0, 800, 480);

        private Rectangle bounds;

        public Rectangle getBounds() {
            return bounds;
        }

        private Component(float x, float y, float width, float height) {
            bounds = new Rectangle(x, y, width, height);
        }
    }
}
