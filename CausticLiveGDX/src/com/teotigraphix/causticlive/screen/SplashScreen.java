
package com.teotigraphix.causticlive.screen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.google.inject.Singleton;
import com.teotigraphix.libgdx.screen.ScreenBase;

@Singleton
public class SplashScreen extends ScreenBase {

    private Image splashImage;

    public SplashScreen() {
    }

    @Override
    public void show() {
        super.show();

        AtlasRegion splashRegion = getAtlas().findRegion("splash");
        Drawable splashDrawable = new TextureRegionDrawable(splashRegion);

        // here we create the splash image actor; its size is set when the
        // resize() method gets called
        splashImage = new Image(splashDrawable, Scaling.stretch);
        splashImage.setFillParent(true);
        //splashImage.getColor().a = 0f;
        splashImage.addAction(Actions.delay(0.01f, new Action() {
            @Override
            public boolean act(float delta) {
                game.setScreen(ICausticLiveScreen.MAIN_SCREEN);
                return true;
            }
        }));

        stage.addActor(splashImage);
    }

}
