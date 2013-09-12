
package com.teotigraphix.causticlive.view.components.tool;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.teotigraphix.libgdx.ui.OldSelectButton;

public class SongListToolBar extends Table {

    private Skin skin;

    private OldSelectButton loadButton;

    private OnSongListToolBarListener listener;

    public SongListToolBar(Skin skin) {
        super(skin);
        this.skin = skin;
        initialize();
    }

    private void initialize() {
        loadButton = new OldSelectButton("Load", skin);
        loadButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                listener.onLoadTap();
            }
        });
        add(loadButton).width(100f).fillY().expandY();
    }

    public void setOnSongListToolBarListener(OnSongListToolBarListener l) {
        listener = l;
    }

    public interface OnSongListToolBarListener {
        void onLoadTap();
    }

}
