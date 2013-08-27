
package com.teotigraphix.causticlive.screen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.view.BankBarMediator;
import com.teotigraphix.causticlive.view.PadGridMediator;
import com.teotigraphix.causticlive.view.SongListMediator;
import com.teotigraphix.causticlive.view.TransportControlMediator;
import com.teotigraphix.libgdx.application.IGame;
import com.teotigraphix.libgdx.screen.ScreenBase;

@Singleton
public class MainScreen extends ScreenBase {

    @Inject
    PadGridMediator PadGridMediator;
    
    @Inject
    SongListMediator SongListMediator;
    
    @Inject
    TransportControlMediator TransportControlMediator;
    
    @Inject
    BankBarMediator BankMediator;

    private Image splashImage;

    public MainScreen() {
    }

    @Override
    public void initialize(IGame game) {
        super.initialize(game);
        addMediator(PadGridMediator);
        addMediator(SongListMediator);
        addMediator(TransportControlMediator);
        addMediator(BankMediator);
    }

    @Override
    public void show() {
        AtlasRegion splashRegion = getAtlas().findRegion("splash");
        Drawable splashDrawable = new TextureRegionDrawable(splashRegion);

        // here we create the splash image actor; its size is set when the
        // resize() method gets called
        splashImage = new Image(splashDrawable, Scaling.stretch);
        splashImage.setFillParent(true);
        //splashImage.getColor().a = 0f;

        stage.addActor(splashImage);

        super.show();
    }
}
