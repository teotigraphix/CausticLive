
package com.teotigraphix.causticlive.view.main.tool;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.model.ILibraryModel.OnLibraryModelLibraryChange;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.view.components.tool.SceneListToolBar;
import com.teotigraphix.causticlive.view.components.tool.SceneListToolBar.OnSceneListToolBarListener;
import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ScrollList;

public class SceneListToolMediator extends ToolBarMediator {

    @Inject
    ILibraryModel libraryModel;

    @Inject
    ISoundModel soundModel;

    private ScrollList sceneList;

    private SceneListToolBar toolBar;

    public SceneListToolMediator() {
    }

    @Override
    public void create(IScreen screen) {
        sceneList = new ScrollList(screen.getSkin());
        setList(sceneList);
        toolBar = new SceneListToolBar(screen.getSkin());
        toolBar.setOnSceneListToolBarListener(new OnSceneListToolBarListener() {
            @Override
            public void onLoadTap() {
                LibraryScene item = (LibraryScene)sceneList.getItem(sceneList.getSelectedIndex());
                soundModel.loadScene(item);
            }
        });
        setToolBar(toolBar);
    }

    @Override
    public void onAttach() {
        register(libraryModel, OnLibraryModelLibraryChange.class,
                new EventObserver<OnLibraryModelLibraryChange>() {
                    @Override
                    public void trigger(OnLibraryModelLibraryChange object) {
                        refreshList();
                    }
                });
    }

    protected void refreshList() {
        Array<LibraryScene> scenes = libraryModel.getScenes();
        sceneList.setItems(scenes);
    }
}
