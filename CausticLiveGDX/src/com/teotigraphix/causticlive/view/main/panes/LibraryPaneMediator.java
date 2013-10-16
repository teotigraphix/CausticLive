
package com.teotigraphix.causticlive.view.main.panes;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListChangeEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListDoubleTapEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.track.Phrase;
import com.teotigraphix.caustk.sequencer.track.Track;
import com.teotigraphix.libgdx.controller.ScreenMediatorChild;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Pane;
import com.teotigraphix.libgdx.ui.PaneStack;
import com.teotigraphix.libgdx.ui.ScrollList;

@Singleton
public class LibraryPaneMediator extends ScreenMediatorChild {

    @Inject
    ISequencerModel sequencerModel;

    @Inject
    IDialogManager dialogManager;

    private ScrollList list;

    public LibraryPaneMediator() {
    }

    @Override
    public void onCreate(IScreen screen, WidgetGroup parent) {
        super.onCreate(screen, parent);

        setupPane(screen.getSkin(), (Pane)parent);
    }

    private Pane setupPane(Skin skin, Pane pane) {

        PaneStack libraryStack = new PaneStack(skin, Align.bottom);
        libraryStack.setSelectedIndex(2); // Phrase (temp)
        pane.add(libraryStack).expand().fill();
        libraryStack.addPane(new Pane(skin, "Scene"));
        libraryStack.addPane(new Pane(skin, "Phrase"));
        Pane patchPane = new Pane(skin, "Patch");
        libraryStack.addPane(patchPane);

        list = new ScrollList(skin);
        list.setOverscroll(false, true);
        list.setItems(getPhraseItems());
        list.addListener(new AdvancedListListener() {
            @Override
            public void changed(AdvancedListChangeEvent event, Actor actor) {
                getController().getLogger().log("", "Change");
            }

            @Override
            public void doubleTap(AdvancedListDoubleTapEvent event, Actor actor) {

                // XXX warning ?
                final QueueData data = sequencerModel.getActiveData();
                if (data == null)
                    return;

                LibraryPhrase libraryPhrase = (LibraryPhrase)list.getSelectedItem();
                Track track = getController().getRack().getTrackSequencer()
                        .getTrack(data.getViewChannelIndex());
                Phrase phrase = data.getPhrase();
                if (phrase != null) {
                    phrase.clear();
                }

                sequencerModel.assignPhrase(data, track, libraryPhrase);

                dialogManager.createToast("Selected phrase added to " + data.getName(), 1f);
            }

        });
        patchPane.add(list).fill().expand();

        return pane;
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

}
