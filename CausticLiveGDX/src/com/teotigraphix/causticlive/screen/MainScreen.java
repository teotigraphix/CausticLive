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

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.view.main.BankBarMediator;
import com.teotigraphix.causticlive.view.main.PadGridMediator;
import com.teotigraphix.causticlive.view.main.ToolBarMediator;
import com.teotigraphix.causticlive.view.main.TransportControlMediator;
import com.teotigraphix.libgdx.application.IGame;
import com.teotigraphix.libgdx.screen.ScreenBase;

@Singleton
public class MainScreen extends ScreenBase {

    @Inject
    PadGridMediator PadGridMediator;

    @Inject
    TransportControlMediator TransportControlMediator;

    @Inject
    BankBarMediator BankMediator;

    //    @Inject
    //    SongListMediator SongListMediator;
    //
    //    @Inject
    //    LibraryItemSelectMediator LibraryItemSelectMediator;

    @Inject
    ToolBarMediator toolBarMediator;

    private Image splashImage;

    public MainScreen() {
    }

    @Override
    public void initialize(IGame game) {
        super.initialize(game);
        SkinRegistry.register(getSkin());
        addMediator(PadGridMediator);

        addMediator(TransportControlMediator);
        addMediator(BankMediator);
        //        addMediator(SongListMediator);
        //        addMediator(LibraryItemSelectMediator);
        addMediator(toolBarMediator);
    }

    @Override
    public void show() {
        if (splashImage == null) {
            AtlasRegion splashRegion = getAtlas().findRegion("splash");
            Drawable splashDrawable = new TextureRegionDrawable(splashRegion);

            // here we create the splash image actor; its size is set when the
            // resize() method gets called
            splashImage = new Image(splashDrawable, Scaling.stretch);
            splashImage.setFillParent(true);
            //splashImage.getColor().a = 0f;

            //stage.addActor(splashImage);
        }

        super.show();
    }
}
