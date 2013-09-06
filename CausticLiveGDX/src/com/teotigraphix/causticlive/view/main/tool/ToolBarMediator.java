
package com.teotigraphix.causticlive.view.main.tool;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.ui.ScrollList;

public abstract class ToolBarMediator extends CaustkMediator {

    private ScrollList list;

    public ScrollList getList() {
        return list;
    }

    void setList(ScrollList list) {
        this.list = list;
    }

    private Actor toolBar;

    public Actor getToolBar() {
        return toolBar;
    }

    void setToolBar(Actor toolBar) {
        this.toolBar = toolBar;
    }

    public ToolBarMediator() {
    }

}
