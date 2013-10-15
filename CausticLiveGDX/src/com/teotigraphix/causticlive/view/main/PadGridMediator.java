////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.causticlive.view.main;

import java.util.Collection;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.PopUp;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.ISequencerModel.OnSequencerModelPropertyChange;
import com.teotigraphix.causticlive.model.ISequencerModel.PadState;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.screen.ICausticLiveScreen;
import com.teotigraphix.causticlive.view.main.components.PadButton;
import com.teotigraphix.causticlive.view.main.components.PadGrid;
import com.teotigraphix.causticlive.view.main.components.PadGrid.OnPadGridListener;
import com.teotigraphix.caustk.sequencer.IQueueSequencer.OnQueueSequencerDataChange;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSystemSequencerBeatChange;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;
import com.teotigraphix.libgdx.screen.IScreen;

@Singleton
public class PadGridMediator extends ScreenMediator {

    @Inject
    IDialogManager dialogManager;

    @Inject
    IScreenProvider screenProvider;

    @Inject
    ISequencerModel sequencerModel;

    @Inject
    ISoundModel soundModel;

    @Inject
    ILibraryModel libraryModel;

    private PadGrid view;

    private SelectBox toneSelectBox;

    private Skin skin;

    private PopUp toneSelectPopUp;

    public PadGridMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        skin = screen.getSkin();

        view = new PadGrid(screen.getSkin());
        view.setOnPadGridListener(new OnPadGridListener() {

            @Override
            public void onChange(int localIndex, QueueData data) {
                //getController().getLogger().log("", localIndex + "");
                if (sequencerModel.getPadState() == PadState.Perform) {
                    if (data != null) {
                        if (data.getViewChannelIndex() != -1)
                            sequencerModel.touch(data);
                    }
                } else {
                    sequencerModel.setActiveData(sequencerModel.getQueueData(
                            sequencerModel.getSelectedBank(), localIndex));
                }
            }

            @Override
            public void onLongPress(Integer localIndex, float x, float y) {
                getController().getLogger().log("PadGridMediator", "long press");
                sequencerModel.setActiveData(sequencerModel.getQueueData(
                        sequencerModel.getSelectedBank(), localIndex));
                screenProvider.getScreen().getGame().setScreen(ICausticLiveScreen.ASSIGN_SCREEN);
            }

        });

        view.setPosition(400f, 0f);
        screen.getStage().addActor(view);

        //------------------------------------
        createToneSelectBox();

    }

    @Override
    public void onAttach(IScreen screen) {
        super.onAttach(screen);

        register(getController(), OnSystemSequencerBeatChange.class,
                new EventObserver<OnSystemSequencerBeatChange>() {
                    @Override
                    public void trigger(OnSystemSequencerBeatChange object) {
                        //final float beat = object.getBeat();
                        // XXX for now this is the easiest way to know the pads
                        // will be redrawn every beat fo the ui
                        updateView(sequencerModel.getViewData(sequencerModel.getSelectedBank()));
                    }
                });

        register(getController(), OnQueueSequencerDataChange.class,
                new EventObserver<OnQueueSequencerDataChange>() {
                    @Override
                    public void trigger(OnQueueSequencerDataChange object) {
                        updateView(sequencerModel.getViewData(sequencerModel.getSelectedBank()));
                    }
                });

        register(sequencerModel, OnSequencerModelPropertyChange.class,
                new EventObserver<OnSequencerModelPropertyChange>() {
                    @Override
                    public void trigger(OnSequencerModelPropertyChange object) {
                        switch (object.getKind()) {
                            case PadState:
                                switch (sequencerModel.getPadState()) {
                                    case Perform:
                                        invalidateActivePadOverlay();
                                        break;

                                    case Assign:
                                        invalidateActivePadOverlay();
                                        break;
                                }

                                break;

                            case Bank:
                                updateView(sequencerModel.getViewData(sequencerModel
                                        .getSelectedBank()));
                                break;

                            case ActiveData:
                                invalidateActivePadOverlay();
                                break;
                        }
                    }
                });
    }

    protected void invalidateActivePadOverlay() {
        boolean show = false;
        switch (sequencerModel.getPadState()) {
            case Perform:
                show = false;
                break;

            case Assign:
                show = true;
                break;
        }

        view.updateActive(sequencerModel.getActiveData(), show);

        if (show && sequencerModel.getActiveData() != null
                && sequencerModel.getActiveData().getViewChannelIndex() == -1) {
            if (toneSelectPopUp == null) {
                toneSelectPopUp = dialogManager.createPopUp(screenProvider.getScreen(),
                        "Select Machine", null);
                toneSelectPopUp.add(toneSelectBox);
                toneSelectPopUp.pad(1f);
                toneSelectPopUp.padTop(20f);
                toneSelectPopUp.show(screenProvider.getScreen().getStage());
            }

            updateToneIndex();

            PadButton button = (PadButton)view.getChildren().get(
                    sequencerModel.getActiveData().getPatternIndex());
            Vector2 localCoords = new Vector2(button.getX(), button.getY());
            localCoords = button.getParent().localToStageCoordinates(localCoords);
            toneSelectPopUp.setPosition(localCoords.x - toneSelectPopUp.getWidth(), localCoords.y);
        } else if (toneSelectPopUp != null) {
            toneSelectPopUp.hide();
            toneSelectPopUp = null;
        }
    }

    private void updateToneIndex() {
        if (sequencerModel.getActiveData() == null) {
            toneSelectPopUp.hide();
            toneSelectPopUp = null;
            return;
        }
        int index = sequencerModel.getActiveData().getViewChannelIndex();
        if (index == -1)
            index = toneSelectBox.getItems().length - 1;
        toneSelectBox.setSelection(index);
    }

    @Override
    public void onShow(IScreen screen) {
        updateView(sequencerModel.getViewData(sequencerModel.getSelectedBank()));
        invalidateActivePadOverlay();
    }

    protected void updateView(Collection<QueueData> viewData) {
        view.refresh(viewData, true);
    }

    private void createToneSelectBox() {
        toneSelectBox = new SelectBox(soundModel.getToneNames(true), skin, "default");
        toneSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final int index = toneSelectBox.getSelectionIndex();
                if (getController().getRack().getSoundSource().hasTone(index)) {
                    QueueData activeData = sequencerModel.getActiveData();
                    if (activeData.getViewChannelIndex() == index)
                        return;
                    libraryModel.assignTone(index, activeData);
                    updateView(sequencerModel.getViewData(sequencerModel.getSelectedBank()));
                }
            }
        });
    }
}
