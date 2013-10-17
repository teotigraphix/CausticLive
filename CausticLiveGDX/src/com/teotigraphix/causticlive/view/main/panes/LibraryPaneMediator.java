
package com.teotigraphix.causticlive.view.main.panes;

import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListChangeEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListDoubleTapEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListListener;
import com.badlogic.gdx.scenes.scene2d.ui.AdvancedList.AdvancedListLongPressEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.NameUtils;
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
        libraryStack.setSelectedIndex(1); // Phrase (temp)
        pane.add(libraryStack).expand().fill();
        libraryStack.addPane(new Pane(skin, "Scene"));
        Pane phrasePane = new Pane(skin, "Phrase");
        libraryStack.addPane(phrasePane);
        Pane patchPane = new Pane(skin, "Patch");
        libraryStack.addPane(patchPane);

        list = new ScrollList(skin);
        list.setOverscroll(false, true);
        list.setFadeScrollBars(false);
        list.setItems(getPhraseItems());
        list.addListener(new AdvancedListListener() {
            @Override
            public void changed(AdvancedListChangeEvent event, Actor actor) {
                getController().getLogger().log("LibraryPaneMediator", "changed");
            }

            @Override
            public void longPress(AdvancedListLongPressEvent event, Actor actor) {
                getController().getLogger().log("LibraryPaneMediator", "longPress");
                // have to check the the long pressed data index 
                // is the same index as the selected data index
                int index = actor.getParent().getChildren().indexOf(actor, true);
                if (list.getSelectedIndex() == index) {
                    getController().getLogger().log("LibraryPaneMediator", "identical index");
                    final LibraryPhrase phrase = (LibraryPhrase)list.getSelectedItem();
                    final String name = phrase.getMetadataInfo().getName();

                    Gdx.input.getTextInput(new TextInputListener() {
                        @Override
                        public void input(String text) {
                            getController().getLogger().log("LibraryPaneMediator", text);
                            phrase.getMetadataInfo().setName(text);
                            try {
                                getController().getLibraryManager().save();
                                list.refresh();
                                // update padgrid as well

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void canceled() {
                            getController().getLogger().log("LibraryPaneMediator",
                                    "Name change Canceled");
                        }
                    }, "Enter a new name", name);

                }
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

                dialogManager.createToast(
                        "Selected phrase added to " + NameUtils.dataDisplayName(data), 1f);
            }

        });
        phrasePane.add(list).fill().expand();

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
