
package com.teotigraphix.causticlive.view.assign;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.GDXButton;
import com.teotigraphix.libgdx.ui.ScrollList;

public class PhraseListMediator extends MediatorBase {

    @Inject
    ISequencerModel sequencerModel;

    private ScrollList view; // need composite component

    private GDXButton assignButton; // this should be in the view

    public PhraseListMediator() {
    }

    @Override
    public void create(IScreen screen) {
        view = new ScrollList(screen.getSkin());
        view.setPosition(25f, 100f);
        view.setSize(400f, 400f);
        screen.getStage().addActor(view);

        assignButton = new GDXButton("Assign", screen.getSkin());
        assignButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                final QueueData data = sequencerModel.getActiveData();
                LibraryPhrase libraryPhrase = (LibraryPhrase)view.getSelectedItem();
                sequencerModel.assignPhrase(data, libraryPhrase);
            }
        });
        assignButton.setPosition(325f, 510f);
        assignButton.setSize(100f, 40f);

        screen.getStage().addActor(assignButton);
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
        QueueData activeData = sequencerModel.getActiveData();
        UUID phraseId = activeData.getPhraseId();
        if (phraseId != null) {
            int index = findPhraseIndex(phraseId);
            if (index != -1) {
                view.setSelectedIndex(index);
            }
        }
    }

    private int findPhraseIndex(UUID phraseId) {
        Iterator<?> i = view.getItems().iterator();
        int index = 0;
        while (i.hasNext()) {
            LibraryPhrase libraryPhrase = (LibraryPhrase)i.next();
            if (libraryPhrase.getId().equals(phraseId))
                return index;
            index++;
        }
        return -1;
    }

}