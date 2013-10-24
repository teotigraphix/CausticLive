
package com.teotigraphix.causticlive.view.admin.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teotigraphix.causticlive.screen.ComponentFactory;
import com.teotigraphix.libgdx.ui.OverlayButton;
import com.teotigraphix.libgdx.ui.Pane;

public class LoadProjectPane extends Pane {

    private OverlayButton loadButton;

    private Label projectLabel;

    public Label getProjectLabel() {
        return projectLabel;
    }

    public LoadProjectPane(Skin skin, String label) {
        super(skin, label);
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        loadButton = createLoadButton(getSkin());
        add(loadButton).size(100f, 40f);

        projectLabel = ComponentFactory.createLabel(getSkin(), "Foo", "default-font", Color.WHITE);
        addActor(projectLabel);
    }

    private OverlayButton createLoadButton(Skin skin) {
        OverlayButton button = new OverlayButton("Load", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                listener.onLoadTap();
            }
        });
        return button;
    }

    private OnLoadProjectPaneListener listener;

    public void setOnLoadProjectPaneListener(OnLoadProjectPaneListener l) {
        listener = l;
    }

    public interface OnLoadProjectPaneListener {
        void onLoadTap();
    }
}
