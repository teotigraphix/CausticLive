
package com.teotigraphix.causticlive.view.main.panes;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.teotigraphix.libgdx.controller.ScreenMediatorChild;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Pane;

public class ControlsPaneMediator extends ScreenMediatorChild {

    public ControlsPaneMediator() {
    }

    @Override
    public void onCreate(IScreen screen, WidgetGroup parent) {
        super.onCreate(screen, parent);

        setupPane(screen.getSkin(), (Pane)parent);
    }

    private void setupPane(Skin skin, Pane pane) {
        pane.setBackground("pad_selected");
    }
}
