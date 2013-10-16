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

package com.teotigraphix.causticlive.screen;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.view.main.BankBarMediator;
import com.teotigraphix.causticlive.view.main.ContextPaneMediator;
import com.teotigraphix.causticlive.view.main.ContextPaneToolBar;
import com.teotigraphix.causticlive.view.main.ExtrasBarMediator;
import com.teotigraphix.causticlive.view.main.MainToggleMediator;
import com.teotigraphix.causticlive.view.main.MainToolBarMediator;
import com.teotigraphix.causticlive.view.main.PadGridMediator;
import com.teotigraphix.causticlive.view.main.PadGridToolBarMediator;
import com.teotigraphix.causticlive.view.main.TransportBarMediator;
import com.teotigraphix.libgdx.application.IGame;
import com.teotigraphix.libgdx.screen.ScreenBase;

@Singleton
public class MainScreen extends ScreenBase {

    @Inject
    MainToolBarMediator mainToolBarMediator;

    @Inject
    ContextPaneToolBar contextPaneToolBar;

    @Inject
    MainToggleMediator mainToggleMediator;

    @Inject
    PadGridToolBarMediator padGridToolBarMediator;

    @Inject
    ContextPaneMediator contextPaneMediator;

    @Inject
    TransportBarMediator transportBarMediator;

    @Inject
    BankBarMediator bankBarMediator;

    @Inject
    ExtrasBarMediator extrasBarMediator;

    @Inject
    PadGridMediator padGridMediator;

    public MainScreen() {
    }

    @Override
    public void initialize(IGame game) {
        super.initialize(game);
        SkinRegistry.register(getSkin());

        addMediator(mainToolBarMediator);
        addMediator(contextPaneToolBar);
        addMediator(mainToggleMediator);
        addMediator(padGridToolBarMediator);
        addMediator(contextPaneMediator);
        addMediator(transportBarMediator);
        addMediator(bankBarMediator);
        addMediator(extrasBarMediator);
        addMediator(padGridMediator);
    }

    @Override
    public void show() {
        //        if (splashImage == null) {
        //            AtlasRegion splashRegion = getAtlas().findRegion("splash");
        //            Drawable splashDrawable = new TextureRegionDrawable(splashRegion);
        //
        //            // here we create the splash image actor; its size is set when the
        //            // resize() method gets called
        //            //splashImage = new Image(splashDrawable, Scaling.stretch);
        //            //splashImage.setFillParent(true);
        //            //splashImage.getColor().a = 0f;
        //
        //            //stage.addActor(splashImage);
        //        }

        super.show();
    }
}
