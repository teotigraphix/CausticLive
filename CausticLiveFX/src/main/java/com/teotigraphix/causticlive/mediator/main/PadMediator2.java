
package com.teotigraphix.causticlive.mediator.main;

import java.util.ArrayList;
import java.util.List;

import org.androidtransfuse.event.EventObserver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.causticlive.model.PadModel;
import com.teotigraphix.causticlive.model.PadModel.OnPadModelAssignmentIndexChange;
import com.teotigraphix.causticlive.model.PadModel.OnPadModelPadDataRefresh;
import com.teotigraphix.causticlive.model.PadModel.OnPadModelSelectedBankChange;
import com.teotigraphix.causticlive.model.PadModel.OnPadModelSelectedFunctionChange;
import com.teotigraphix.causticlive.model.PadModel.PadData;
import com.teotigraphix.causticlive.model.PadModel.PadDataState;
import com.teotigraphix.causticlive.model.PadModel.PadFunction;
import com.teotigraphix.causticlive.model.SoundModel;
import com.teotigraphix.causticlive.model.SoundModel.OnSoundModelSceneLoad;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.core.PatternUtils;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.tone.Tone;

public class PadMediator2 extends DesktopMediatorBase {

    private boolean updatingView;

    private List<ToggleButton> pads = new ArrayList<>();

    private Pane padSelectionOverlay;

    @Inject
    PadModel padModel;

    @Inject
    SoundModel soundModel;

    private Pane padButtonPane;

    @Inject
    public PadMediator2(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    public void create(Pane root) {

        padSelectionOverlay = (Pane)root.lookup("#padSelectionOverlay");

        padButtonPane = (Pane)root.lookup("#padButtonPane");
        int index = 0;
        for (Node node : padButtonPane.getChildren()) {
            final ToggleButton button = (ToggleButton)node;
            button.getProperties().put("index", index);
            button.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable,
                        Boolean oldValue, Boolean newValue) {
                    onPadButtonSelected(button);
                }
            });
            pads.add(button);
            index++;
        }

        //------------------------------
        // padSelectionOverlay
        index = 0;
        for (Node node : padSelectionOverlay.getChildren()) {
            final int i = index;
            final Button button = (Button)node;
            button.getProperties().put("index", index);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    padModel.setAssignmentIndex(i);
                }
            });
            index++;
        }

        setEnabled(false);
    }

    private void setEnabled(boolean enabled) {
        ObservableList<Node> children = padButtonPane.getChildren();
        for (Node node : children) {
            node.setDisable(!enabled);
        }
    }

    protected void onPadButtonSelected(ToggleButton button) {
        CtkDebug.ui("Pad pressed " + button + ":" + button.isSelected());
        if (updatingView)
            return;
        padModel.select((int)button.getProperties().get("index"), button.isSelected());
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        // OnPadModelSelectedBankChange
        padModel.getDispatcher().register(OnPadModelSelectedBankChange.class,
                new EventObserver<OnPadModelSelectedBankChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedBankChange object) {
                        updatePadView(object.getView());
                    }
                });

        // OnPadModelPadDataRefresh
        padModel.getDispatcher().register(OnPadModelPadDataRefresh.class,
                new EventObserver<OnPadModelPadDataRefresh>() {
                    @Override
                    public void trigger(OnPadModelPadDataRefresh object) {
                        updatePadView(padModel.getPadDataView());
                    }
                });

        // OnPadModelSelectedFunctionChange
        padModel.getDispatcher().register(OnPadModelSelectedFunctionChange.class,
                new EventObserver<OnPadModelSelectedFunctionChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedFunctionChange object) {
                        if (padModel.getSelectedFunction() == PadFunction.ASSIGN) {
                            padSelectionOverlay.setVisible(true);
                        } else if (padModel.getSelectedFunction() == PadFunction.PATTERN) {
                            padSelectionOverlay.setVisible(false);
                        }
                    }
                });

        // OnSoundModelSceneLoad
        soundModel.getDispatcher().register(OnSoundModelSceneLoad.class,
                new EventObserver<OnSoundModelSceneLoad>() {
                    @Override
                    public void trigger(OnSoundModelSceneLoad object) {
                        setEnabled(true);
                    }
                });

        // OnPadModelAssignmentIndexChange
        padModel.getDispatcher().register(OnPadModelAssignmentIndexChange.class,
                new EventObserver<OnPadModelAssignmentIndexChange>() {
                    @Override
                    public void trigger(OnPadModelAssignmentIndexChange object) {
                        onPadModelAssignmentIndexChange();
                    }
                });

    }

    protected void onPadModelAssignmentIndexChange() {
        // the current PadData for the assignment pad pressed
        PadData data = padModel.getAssignmentData();
        CtkDebug.view("onPadModelAssignmentIndexChange() " + data.getBank() + " "
                + data.getLocalIndex());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        CtkDebug.view("   Register: PadMediator");

        //padModel.setSelectedBank(0);
    }

    protected void updatePadView(List<PadData> view) {
        updatingView = true;
        for (PadData data : view) {
            int localIndex = data.getLocalIndex();
            ToggleButton padButton = pads.get(localIndex);
            Button overlayButton = (Button)padSelectionOverlay.getChildren().get(localIndex);
            if (data.getState() == PadDataState.IDLE) {
                padButton.setSelected(false);
            } else if (data.getState() == PadDataState.SELECTED) {
                padButton.setSelected(true);
            }
            String text = getButtonText(data);
            padButton.setText(text);
            overlayButton.setText(text);
        }
        updatingView = false;
    }

    private String getButtonText(PadData data) {
        //        LibraryPhrase phrase = getController().getLibraryManager().getSelectedLibrary()
        //                .findPhraseById(data.getPhraseId());
        //        if (phrase == null)
        Tone tone = getController().getSoundSource().getTone(data.getToneIndex());
        if (tone == null)
            return "not loaded";

        StringBuilder sb = new StringBuilder();
        sb.append(tone.getName());
        sb.append(":[-1,-1]");
        //sb.append(PatternUtils.toString(phrase.getBankIndex(), phrase.getPatternIndex()));
        //PatternSequencerComponent component = tone.getComponent(PatternSequencerComponent.class);

        return sb.toString();
    }
}
