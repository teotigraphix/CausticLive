
package com.teotigraphix.causticlive.view.admin.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teotigraphix.libgdx.ui.OverlayButton;
import com.teotigraphix.libgdx.ui.Pane;

public class NewProjectPane extends Pane {

    private OverlayButton createButton;

    public NewProjectPane(Skin skin, String label) {
        super(skin, label);
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        createButton = createCreateButton(getSkin());
        add(createButton).size(100f, 40f);
    }

    private OverlayButton createCreateButton(Skin skin) {
        OverlayButton button = new OverlayButton("Create", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                listener.onCreateTap();
            }
        });
        return button;
    }

    private OnNewProjectPaneListener listener;

    public void setOnNewProjectPaneListener(OnNewProjectPaneListener l) {
        listener = l;
    }

    public interface OnNewProjectPaneListener {
        void onCreateTap();
    }
}
