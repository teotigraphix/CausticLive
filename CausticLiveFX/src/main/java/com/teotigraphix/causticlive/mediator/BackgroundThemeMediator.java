
package com.teotigraphix.causticlive.mediator;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.causticlive.CausticLiveApplication;
import com.teotigraphix.causticlive.config.ApplicationConstants;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.core.CtkDebug;

public class BackgroundThemeMediator extends DesktopMediatorBase {

    @Inject
    public BackgroundThemeMediator(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    public void create(Pane root) {
        CtkDebug.view("   Create: BackgroundThemeMediator");
        ImageView background = new ImageView(new Image(
                CausticLiveApplication.class
                        .getResourceAsStream(ApplicationConstants.IMAGE_BACKGROUND)));
        root.getChildren().add(background);
    }

    @Override
    public void onRegister() {
        super.onRegister();
        CtkDebug.view("   Register: BackgroundThemeMediator");
    }
}
