
package com.teotigraphix.causticlive.view.admin.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teotigraphix.libgdx.ui.OverlayButton;
import com.teotigraphix.libgdx.ui.Pane;

public class SaveProjectPane extends Pane {

    private OverlayButton saveButton;

    public SaveProjectPane(Skin skin, String label) {
        super(skin, label);
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        saveButton = createSaveButton(getSkin());
        add(saveButton).size(100f, 40f);
    }

    private OverlayButton createSaveButton(Skin skin) {
        OverlayButton button = new OverlayButton("Save", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                listener.onSaveTap();
            }
        });
        return button;
    }

    private OnSaveProjectPaneListener listener;

    public void setOnSaveProjectPaneListener(OnSaveProjectPaneListener l) {
        listener = l;
    }

    public interface OnSaveProjectPaneListener {
        void onSaveTap();
    }
}
