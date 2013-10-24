
package com.teotigraphix.causticlive.view.main.panes;

import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.NameUtils;
import com.teotigraphix.causticlive.view.main.components.LibraryPatchPane;
import com.teotigraphix.causticlive.view.main.components.LibraryPhrasePane;
import com.teotigraphix.causticlive.view.main.components.LibraryScenePane;
import com.teotigraphix.caustk.library.item.LibraryPatch;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.caustk.rack.queue.QueueData;
import com.teotigraphix.caustk.rack.track.Phrase;
import com.teotigraphix.caustk.rack.track.Track;
import com.teotigraphix.libgdx.controller.ScreenMediatorChild;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Pane;
import com.teotigraphix.libgdx.ui.PaneStack;
import com.teotigraphix.libgdx.ui.ScrollListPane.OnScrollListPaneListener;

@Singleton
public class LibraryPaneMediator extends ScreenMediatorChild {

    private static final String PREF_SELECTED_PHRASE = "selectedPhrase";

    @Inject
    ISequencerModel sequencerModel;

    @Inject
    IDialogManager dialogManager;

    private PaneStack libraryPane;

    private LibraryPhrasePane phrasePane;

    private LibraryScenePane scenePane;

    private LibraryPatchPane patchPane;

    public LibraryPaneMediator() {
    }

    @Override
    public void onCreate(IScreen screen, WidgetGroup parent) {
        super.onCreate(screen, parent);

        setupPane(screen.getSkin(), (Pane)parent);
    }

    private Pane setupPane(Skin skin, Pane pane) {

        libraryPane = new PaneStack(skin, Align.bottom);
        libraryPane.setSelectedIndex(1); // Phrase (temp)
        pane.add(libraryPane).expand().fill();

        scenePane = new LibraryScenePane(skin, "Scene");
        scenePane.setOnScrollListPaneListener(new OnScrollListPaneListener() {
            @Override
            public void onListLongPress(int index) {

            }

            @Override
            public void onListDoubleTap(int index) {

            }

            @Override
            public void onListChange(int index) {

            }
        });
        libraryPane.addPane(scenePane);

        phrasePane = new LibraryPhrasePane(skin, "Phrase");
        phrasePane.setOnScrollListPaneListener(new OnScrollListPaneListener() {
            @Override
            public void onListLongPress(int index) {
                getController().getLogger().log("LibraryPaneMediator", "longPress");
                // have to check the the long pressed data index 
                // is the same index as the selected data index
                if (phrasePane.getSelectedIndex() == index) {
                    getController().getLogger().log("LibraryPaneMediator", "identical index");
                    final LibraryPhrase phrase = (LibraryPhrase)phrasePane.getSelectedItem();
                    final String name = phrase.getMetadataInfo().getName();

                    Gdx.input.getTextInput(new TextInputListener() {
                        @Override
                        public void input(String text) {
                            getController().getLogger().log("LibraryPaneMediator", text);
                            phrase.getMetadataInfo().setName(text);
                            try {
                                getController().getLibraryManager().save();
                                phrasePane.refresh();
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
            public void onListDoubleTap(int index) {
                // XXX warning ?
                final QueueData data = sequencerModel.getActiveData();
                if (data == null)
                    return;

                LibraryPhrase libraryPhrase = (LibraryPhrase)phrasePane.getSelectedItem();
                Track track = getController().getRack().getTrackSequencer()
                        .getTrack(data.getViewChannelIndex());
                Phrase phrase = data.getPhrase();
                if (phrase != null) {
                    phrase.clear();
                }

                sequencerModel.assignPhrase(data, track, libraryPhrase);

                dialogManager.createToast(
                        "Selected phrase added to "
                                + NameUtils.dataDisplayName(getController().getLibraryManager()
                                        .getSelectedLibrary(), data), 1f);
            }

            @Override
            public void onListChange(int index) {
                getController().getLogger().log("LibraryPaneMediator", "changed");
                selectPaneIndex(index);
            }
        });
        libraryPane.addPane(phrasePane);

        patchPane = new LibraryPatchPane(skin, "Patch");
        patchPane.setOnScrollListPaneListener(new OnScrollListPaneListener() {
            @Override
            public void onListLongPress(int index) {
            }

            @Override
            public void onListDoubleTap(int index) {
            }

            @Override
            public void onListChange(int index) {
            }
        });
        libraryPane.addPane(patchPane);

        return pane;
    }

    protected void selectPaneIndex(int selectedIndex) {
        putPref(PREF_SELECTED_PHRASE, selectedIndex);
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);

        scenePane.setItems(getSceneItems());

        Integer index = getInteger(PREF_SELECTED_PHRASE, 0);
        phrasePane.setItems(getPhraseItems());
        phrasePane.setSelectedIndex(index);

        patchPane.setItems(getPatchItems());
    }

    private Array<?> getSceneItems() {
        final List<LibraryScene> items = getController().getLibraryManager().getSelectedLibrary()
                .getScenes();
        Array<LibraryScene> result = new Array<LibraryScene>();
        for (LibraryScene item : items) {
            result.add(item);
        }
        return result;
    }

    private Array<?> getPhraseItems() {
        final List<LibraryPhrase> items = getController().getLibraryManager().getSelectedLibrary()
                .getPhrases();
        Array<LibraryPhrase> result = new Array<LibraryPhrase>();
        for (LibraryPhrase item : items) {
            result.add(item);
        }
        return result;
    }

    private Array<?> getPatchItems() {
        final List<LibraryPatch> items = getController().getLibraryManager().getSelectedLibrary()
                .getPatches();
        Array<LibraryPatch> result = new Array<LibraryPatch>();
        for (LibraryPatch item : items) {
            result.add(item);
        }
        return result;
    }
}
