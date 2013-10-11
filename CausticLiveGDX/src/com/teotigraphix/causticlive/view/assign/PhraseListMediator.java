
package com.teotigraphix.causticlive.view.assign;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.track.Phrase;
import com.teotigraphix.caustk.sequencer.track.Track;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.OverlayButton;
import com.teotigraphix.libgdx.ui.ScrollList;

@Singleton
public class PhraseListMediator extends ScreenMediator {

    @Inject
    ISequencerModel sequencerModel;

    private ScrollList view; // need composite component

    private OverlayButton assignButton; // this should be in the view

    public PhraseListMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);
        view = new ScrollList(screen.getSkin());
        view.setPosition(25f, 100f);
        view.setSize(400f, 400f);
        screen.getStage().addActor(view);

        assignButton = new OverlayButton("Assign", screen.getSkin());
        assignButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                final QueueData data = sequencerModel.getActiveData();
                LibraryPhrase libraryPhrase = (LibraryPhrase)view.getSelectedItem();
                Track track = getController().getRack().getTrackSequencer()
                        .getTrack(data.getViewChannelIndex());
                Phrase phrase = data.getPhrase();
                if (phrase != null) {
                    phrase.clear();
                }
                sequencerModel.assignPhrase(data, track, libraryPhrase);
            }
        });
        assignButton.setPosition(325f, 510f);
        assignButton.setSize(100f, 40f);

        screen.getStage().addActor(assignButton);
    }

    private Array<?> getPhraseItems() {
        final List<LibraryPhrase> phrases = getController().getLibraryManager()
                .getSelectedLibrary().getPhrases();
        Array<LibraryPhrase> result = new Array<LibraryPhrase>();
        for (LibraryPhrase libraryPhrase : phrases) {
            result.add(libraryPhrase);
        }
        return result;
    }

    @Override
    public void onShow(IScreen screen) {
        view.setItems(getPhraseItems());
        // set the selected index
        //        QueueData activeData = sequencerModel.getActiveData();
        //        UUID phraseId = activeData.getPhraseId();
        //        if (phraseId != null) {
        //            int index = findPhraseIndex(phraseId);
        //            if (index != -1) {
        //                view.setSelectedIndex(index);
        //            }
        //        }
    }

    @SuppressWarnings("unused")
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
