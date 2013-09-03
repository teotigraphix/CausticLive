
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
import com.teotigraphix.causticlive.model.IToneModel;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.GDXButton;
import com.teotigraphix.libgdx.ui.ScrollList;

@Singleton
public class PatchListMediator extends MediatorBase {

    @Inject
    ISequencerModel sequencerModel;

    @Inject
    IToneModel toneModel;

    private ScrollList view; // need composite component

    private GDXButton assignButton; // this should be in the view

    public PatchListMediator() {
    }

    @Override
    public void create(IScreen screen) {
        view = new ScrollList(screen.getSkin());
        view.setPosition(775f, 100f);
        view.setSize(400f, 400f);
        screen.getStage().addActor(view);

        assignButton = new GDXButton("Assign", screen.getSkin());
        assignButton.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                final QueueData data = sequencerModel.getActiveData();
                LibraryPatch libraryPatch = (LibraryPatch)view.getSelectedItem();
                toneModel.assignPatch(data, libraryPatch);
            }
        });
        assignButton.setPosition(1075f, 510f);
        assignButton.setSize(100f, 40f);

        screen.getStage().addActor(assignButton);
    }

    @Override
    public void onShow(IScreen screen) {
        view.setItems(getPatchItems());
        // set the selected index
        QueueData activeData = sequencerModel.getActiveData();
        UUID phraseId = activeData.getPhraseId();
        if (phraseId != null) {
            int index = findPatchIndex(phraseId);
            if (index != -1) {
                view.setSelectedIndex(index);
            }
        }
    }

    private int findPatchIndex(UUID patchId) {
        Iterator<?> i = view.getItems().iterator();
        int index = 0;
        while (i.hasNext()) {
            LibraryPatch item = (LibraryPatch)i.next();
            if (item.getId().equals(patchId))
                return index;
            index++;
        }
        return -1;
    }

    @Override
    public void onRegister() {
        // TODO Auto-generated method stub

    }

    private Array<?> getPatchItems() {
        final List<LibraryPatch> patches = getController().getLibraryManager().getSelectedLibrary()
                .getPatches();
        Array<LibraryPatch> result = new Array<LibraryPatch>();
        for (LibraryPatch item : patches) {
            result.add(item);
        }
        return result;
    }
}
