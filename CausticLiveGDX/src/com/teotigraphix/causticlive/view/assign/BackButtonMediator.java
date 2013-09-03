
package com.teotigraphix.causticlive.view.assign;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.screen.ICausticLiveScreen;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.GDXButton;

public class BackButtonMediator extends MediatorBase {

    @Inject
    IApplicationModel applicationModel;

    public BackButtonMediator() {
    }

    @Override
    public void create(IScreen screen) {
        final Stage stage = screen.getStage();
        GDXButton button = new GDXButton("Back", screen.getSkin());
        button.setPosition(1090f, 705f);
        button.setSize(100f, 40f);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                applicationModel.setScreen(ICausticLiveScreen.MAIN_SCREEN);
            }
        });
        stage.addActor(button);
    }

    @Override
    public void onRegister() {
    }

}
