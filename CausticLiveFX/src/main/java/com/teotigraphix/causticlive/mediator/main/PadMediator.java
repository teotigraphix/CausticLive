
package com.teotigraphix.causticlive.mediator.main;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.caustic.screen.IScreenManager;
import com.teotigraphix.caustic.utils.UIUtils;
import com.teotigraphix.causticlive.model.IPadMapModel;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelSelectedBankChange;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelSelectedDataChange;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelSelectedFunctionChange;
import com.teotigraphix.causticlive.model.IPadModel.PadDataState;
import com.teotigraphix.causticlive.model.IPadModel.PadFunction;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.model.PadData;
import com.teotigraphix.causticlive.model.SoundModel.OnSoundModelLibrarySceneChange;
import com.teotigraphix.causticlive.screen.AssignmentScreenView;
import com.teotigraphix.caustk.core.CtkDebug;

public class PadMediator extends DesktopMediatorBase {

    private List<ToggleButton> pads = new ArrayList<>();

    private Pane padSelectionOverlay;

    @Inject
    IPadModel padModel;

    @Inject
    IPadMapModel padMapModel;

    @Inject
    ISoundModel soundModel;

    private Pane padButtonPane;

    private Pane padFocusOverlay;

    public void layoutFocusOverlay(ToggleButton button) {
        if (button == null)
            return;

        Point2D point = new Point2D(button.getLayoutX(), button.getLayoutY());
        point = button.getParent().localToScene(point);

        UIUtils.layout(padFocusOverlay, point.getX(), point.getY(), button.getPrefWidth(),
                button.getPrefHeight());
    }

    @Override
    public void create(Pane root) {

        padFocusOverlay = (Pane)root.lookup("#padFocusOverlay");
        padFocusOverlay.setVisible(false);

        padSelectionOverlay = (Pane)root.lookup("#padSelectionOverlay");
        padSelectionOverlay.setDisable(true);

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
                    padModel.select(padModel.getSelectedData().getBank(), i);
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
        //padModel.select((int)button.getProperties().get("index"), button.isSelected());
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        // OnPadModelSelectedDataChange
        padModel.getDispatcher().register(OnPadModelSelectedDataChange.class,
                new EventObserver<OnPadModelSelectedDataChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedDataChange object) {
                        onPadModelAssignmentIndexChange();
                    }
                });

        padModel.getDispatcher().register(OnPadModelSelectedBankChange.class,
                new EventObserver<OnPadModelSelectedBankChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedBankChange object) {
                        ToggleButton button = getButtonForData(padModel.getSelectedData());
                        layoutFocusOverlay(button);
                    }
                });

        // OnPadModelSelectedFunctionChange
        padModel.getDispatcher().register(OnPadModelSelectedFunctionChange.class,
                new EventObserver<OnPadModelSelectedFunctionChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedFunctionChange object) {
                        if (padModel.getSelectedFunction() == PadFunction.ASSIGN) {
                            padSelectionOverlay.setVisible(true);
                            padFocusOverlay.setVisible(padModel.getSelectedData() != null);
                        } else if (padModel.getSelectedFunction() == PadFunction.PATTERN) {
                            padSelectionOverlay.setVisible(false);
                            padFocusOverlay.setVisible(false);
                        }
                    }
                });

        // OnSoundModelLibrarySceneChange
        soundModel.getDispatcher().register(OnSoundModelLibrarySceneChange.class,
                new EventObserver<OnSoundModelLibrarySceneChange>() {
                    @Override
                    public void trigger(OnSoundModelLibrarySceneChange object) {
                        padSelectionOverlay.setDisable(false);
                        updateEnabledForActivePads();
                    }
                });
    }

    protected void updateEnabledForActivePads() {
        //        // loop through all the 16 pads, get the data for each and
        //        // enable ONLY if the tone index and phrase have been assigned
        //        ObservableList<Node> children = padButtonPane.getChildren();
        //        for (Node node : children) {
        //            boolean disabled = true;
        //            int localIndex = (int)node.getProperties().get("index");
        //            PadData data = padModel.getAssignmentDataAt(localIndex);
        //            if (data.isEnabled()) {
        //                disabled = false;
        //            }
        //            node.setDisable(disabled);
        //        }

    }

    @Inject
    IScreenManager screenManager;

    protected void onPadModelAssignmentIndexChange() {
        // the current PadData for the assignment pad pressed
        PadData data = padModel.getSelectedData();
        if (data == null) {
            padFocusOverlay.setVisible(false);
            return;
        }

        CtkDebug.view("onPadModelAssignmentIndexChange() " + data.getBank() + " "
                + data.getLocalIndex());

        screenManager.showPopUp(AssignmentScreenView.class);

        //        CtkDebug.view("onPadModelAssignmentIndexChange() " + data.getBank() + " "
        //                + data.getLocalIndex());
        ToggleButton button = getButtonForData(data);
        layoutFocusOverlay(button);
        padFocusOverlay.setVisible(true);
    }

    private ToggleButton getButtonForData(PadData data) {
        for (ToggleButton button : pads) {
            int index = (int)button.getProperties().get("index");
            if (index == data.getLocalIndex()) {
                return button;
            }
        }
        return null;
    }

    @Override
    public void onRegister() {
    }

    protected void updatePadView(List<PadData> view) {
        for (PadData data : view) {
            int localIndex = data.getLocalIndex();
            ToggleButton padButton = pads.get(localIndex);
            Button overlayButton = (Button)padSelectionOverlay.getChildren().get(localIndex);
            padButton.disarm();
            if (data.getState() == PadDataState.IDLE) {
                padButton.setSelected(false);
            } else if (data.getState() == PadDataState.SELECTED) {
                padButton.setSelected(true);
            }
            String text = getButtonText(data);
            padButton.setText(text);
            overlayButton.setText(text);
            padButton.arm();
        }
    }

    private String getButtonText(PadData data) {
        //        Library library = getController().getLibraryManager().getSelectedLibrary();
        //        if (library == null)
        //            return "not loaded";
        //
        //        LibraryPhrase phrase = library.findPhraseById(data.getPhraseId());
        //        //        if (phrase == null)
        //        Tone tone = getController().getSoundSource().getTone(data.getToneIndex());
        //        if (tone == null)
        //            return "not loaded";
        //
        //        StringBuilder sb = new StringBuilder();
        //        sb.append(tone.getName());
        //        if (phrase == null)
        //            sb.append(":[-1,-1]");
        //        else {
        //            String pat = PatternUtils.toString(phrase.getBankIndex(), phrase.getPatternIndex());
        //            sb.append(":[" + pat + "]");
        //        }
        //
        //        //sb.append(PatternUtils.toString(phrase.getBankIndex(), phrase.getPatternIndex()));
        //        //PatternSequencerComponent component = tone.getComponent(PatternSequencerComponent.class);

        return ""; //sb.toString();
    }
}
