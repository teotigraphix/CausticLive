
package com.teotigraphix.causticlive.view.assign;

import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ScrollList;

public class PhraseListMediator extends MediatorBase {

    private ScrollList view;

    public PhraseListMediator() {
    }

    @Override
    public void create(IScreen screen) {
        view = new ScrollList(screen.getSkin());
        view.setPosition(25f, 100f);
        view.setSize(400f, 400f);
        screen.getStage().addActor(view);
    }

    private Array<?> getPatchItems() {
        final List<LibraryPhrase> phrases = getController().getLibraryManager()
                .getSelectedLibrary().getPhrases();
        Array<LibraryPhrase> result = new Array<LibraryPhrase>();
        for (LibraryPhrase libraryPhrase : phrases) {
            result.add(libraryPhrase);
        }
        return result;
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow(IScreen screen) {
        view.setItems(getPatchItems());
        // set the selected index

    }

}
