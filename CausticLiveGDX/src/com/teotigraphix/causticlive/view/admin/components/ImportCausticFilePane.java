
package com.teotigraphix.causticlive.view.admin.components;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teotigraphix.libgdx.ui.OverlayButton;
import com.teotigraphix.libgdx.ui.Pane;

public class ImportCausticFilePane extends Pane {

    private OverlayButton importButton;

    public ImportCausticFilePane(Skin skin, String label) {
        super(skin, label);
    }

    @Override
    protected void createChildren() {
        super.createChildren();

        importButton = createImportButton(getSkin());
        add(importButton).size(100f, 40f);
    }

    private OverlayButton createImportButton(Skin skin) {
        OverlayButton button = new OverlayButton("Import", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                listener.onImportTap();
            }
        });
        return button;
    }

    private OnImportCausticFilePaneListener listener;

    public void setOnImportCausticFilePaneListener(OnImportCausticFilePaneListener l) {
        listener = l;
    }

    public interface OnImportCausticFilePaneListener {
        void onImportTap();
    }
}
