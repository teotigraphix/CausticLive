
package com.teotigraphix.causticlive.view.main;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.view.main.tool.SceneListToolMediator;
import com.teotigraphix.causticlive.view.main.tool.SongListToolMediator;
import com.teotigraphix.causticlive.view.main.tool.ToolBarMediator;
import com.teotigraphix.libgdx.controller.ICaustkMediator;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ButtonBar;
import com.teotigraphix.libgdx.ui.ButtonBar.OnButtonBarListener;
import com.teotigraphix.libgdx.ui.ScrollList;

public class LibraryItemSelectMediator extends MediatorBase implements ICaustkMediator {

    @Inject
    SongListToolMediator songListToolMediator;

    @Inject
    SceneListToolMediator sceneListToolMediator;

    @Inject
    ILibraryModel libraryModel;

    @Inject
    Injector injector;

    private String[] items = new String[] {
            "Import", "Scene"
    };

    private Array<ScrollList> lists = new Array<ScrollList>();

    private Array<Actor> tools = new Array<Actor>();

    private ButtonBar buttonBar;

    private Stack listStack;

    private Stack toolStack;

    private Array<ToolBarMediator> mediators = new Array<ToolBarMediator>();

    public LibraryItemSelectMediator() {
    }

    @Override
    public void create(IScreen screen) {

        mediators.add(songListToolMediator);
        mediators.add(sceneListToolMediator);

        float x = 65f;
        float y = 235f;
        float width = 330f;
        float height = 400f;

        Table table = new Table(screen.getSkin());
        listStack = new Stack();
        toolStack = new Stack();

        buttonBar = new ButtonBar(screen.getSkin(), items, false);
        buttonBar.setOnButtonBarListener(new OnButtonBarListener() {
            @Override
            public void onChange(int index) {
                setTop(index);
            }
        });

        table.setPosition(x, y);
        //table.debug();
        table.setSize(width, height);
        table.add(buttonBar).expandX().fillX().height(40f);
        table.row();
        table.add(listStack).fill().expand();
        table.row();
        table.add(toolStack).height(40f).left();

        screen.getStage().addActor(table);

        initTools(screen);

        setTop(0);
    }

    private void initTools(IScreen screen) {
        for (ToolBarMediator mediator : mediators) {
            mediator.create(screen);
            mediator.onRegisterObservers();
            mediator.onRegister();

            listStack.addActor(mediator.getList());
            lists.add(mediator.getList());

            toolStack.add(mediator.getToolBar());
            tools.add(mediator.getToolBar());
        }

    }

    private void setTop(int index) {
        for (ScrollList scrollList : lists) {
            scrollList.setVisible(false);
        }
        lists.get(index).setVisible(true);
        for (Actor actor : tools) {
            actor.setVisible(false);
        }
        if (index < tools.size)
            tools.get(index).setVisible(true);
    }

    @Override
    public void onRegister() {

    }

}
