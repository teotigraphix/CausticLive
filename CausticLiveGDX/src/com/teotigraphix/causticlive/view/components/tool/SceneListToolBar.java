
package com.teotigraphix.causticlive.view.components.tool;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teotigraphix.libgdx.ui.GDXButton;

public class SceneListToolBar extends Table {

    private Skin skin;

    private GDXButton loadButton;

    private OnSceneListToolBarListener listener;

    public SceneListToolBar(Skin skin) {
        super(skin);
        this.skin = skin;
        initialize();
    }

    private void initialize() {
        loadButton = new GDXButton("Load", skin);
        loadButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                listener.onLoadTap();
            }
        });
        add(loadButton).width(100f).fillY().expandY();
    }

    public void setOnSceneListToolBarListener(OnSceneListToolBarListener l) {
        listener = l;
    }

    public interface OnSceneListToolBarListener {
        void onLoadTap();
    }
}
