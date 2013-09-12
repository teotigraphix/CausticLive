
package com.teotigraphix.causticlive.screen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.view.SkinRegistry;
import com.teotigraphix.causticlive.view.main.BankBarMediator;
import com.teotigraphix.causticlive.view.main.LibraryItemSelectMediator;
import com.teotigraphix.causticlive.view.main.PadGridMediator;
import com.teotigraphix.causticlive.view.main.SongListMediator;
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

    @Inject
    SongListMediator SongListMediator;

    @Inject
    LibraryItemSelectMediator LibraryItemSelectMediator;

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
        addMediator(SongListMediator);
        addMediator(LibraryItemSelectMediator);
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
