
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
import com.teotigraphix.caustic.utils.UIUtils;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelPadDataDeselect;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelPadDataRefresh;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelSelectedBankChange;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelSelectedFunctionChange;
import com.teotigraphix.causticlive.model.IPadModel.PadDataState;
import com.teotigraphix.causticlive.model.IPadModel.PadFunction;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.model.PadModel.OnPadModelAssignmentIndexChange;
import com.teotigraphix.causticlive.model.SoundModel.OnSoundModelSceneLoad;
import com.teotigraphix.causticlive.model.vo.PadData;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.core.PatternUtils;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.tone.Tone;

public class PadMediator extends DesktopMediatorBase {

    private List<ToggleButton> pads = new ArrayList<>();

    private Pane padSelectionOverlay;

    @Inject
    IPadModel padModel;

    @Inject
    ISoundModel soundModel;

    private Pane padButtonPane;

    private Pane padFocusOverlay;

    public void layoutFocusOverlay(ToggleButton button) {
        if (button == null)
            return;

        Point2D point = new Point2D(button.getLayoutX(), button.getLayoutY());
        point = button.getParent().localToScene(point);

        UIUtils.layout(padFocusOverlay, point.getX(), point.getY(), button.getWidth(),
                button.getHeight());
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
                        updateEnabledForActivePads();
                        padModel.setAssignmentIndex(-1);
                    }
                });

        // OnPadModelPadDataDeselect
        padModel.getDispatcher().register(OnPadModelPadDataDeselect.class,
                new EventObserver<OnPadModelPadDataDeselect>() {
                    @Override
                    public void trigger(OnPadModelPadDataDeselect object) {
                        //CtkDebug.err(data.toString());
                        PadData data = object.getData();
                        ToggleButton node = (ToggleButton)padButtonPane.getChildren().get(
                                data.getLocalIndex());
                        node.disarm();
                        node.setSelected(false);
                        node.arm();
                    }
                });

        // OnPadModelPadDataRefresh
        padModel.getDispatcher().register(OnPadModelPadDataRefresh.class,
                new EventObserver<OnPadModelPadDataRefresh>() {
                    @Override
                    public void trigger(OnPadModelPadDataRefresh object) {
                        updatePadView(padModel.getPadDataView());
                        updateEnabledForActivePads();
                    }
                });

        // OnPadModelSelectedFunctionChange
        padModel.getDispatcher().register(OnPadModelSelectedFunctionChange.class,
                new EventObserver<OnPadModelSelectedFunctionChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedFunctionChange object) {
                        if (padModel.getSelectedFunction() == PadFunction.ASSIGN) {
                            padSelectionOverlay.setVisible(true);
                            padFocusOverlay.setVisible(padModel.getAssignmentIndex() != -1);
                        } else if (padModel.getSelectedFunction() == PadFunction.PATTERN) {
                            padSelectionOverlay.setVisible(false);
                            padFocusOverlay.setVisible(false);
                        }
                    }
                });

        // OnSoundModelSceneLoad
        soundModel.getDispatcher().register(OnSoundModelSceneLoad.class,
                new EventObserver<OnSoundModelSceneLoad>() {
                    @Override
                    public void trigger(OnSoundModelSceneLoad object) {
                        padSelectionOverlay.setDisable(false);
                        updateEnabledForActivePads();
                    }
                });

        // OnPadModelAssignmentIndexChange
        padModel.getDispatcher().register(OnPadModelAssignmentIndexChange.class,
                new EventObserver<OnPadModelAssignmentIndexChange>() {
                    @Override
                    public void trigger(OnPadModelAssignmentIndexChange object) {
                        if (!object.isNoSelection())
                            onPadModelAssignmentIndexChange();
                        else
                            padFocusOverlay.setVisible(false);
                    }
                });

    }

    protected void updateEnabledForActivePads() {
        // loop through all the 16 pads, get the data for each and
        // enable ONLY if the tone index and phrase have been assigned
        ObservableList<Node> children = padButtonPane.getChildren();
        for (Node node : children) {
            boolean disabled = true;
            int localIndex = (int)node.getProperties().get("index");
            PadData data = padModel.getAssignmentDataAt(localIndex);
            if (data.isEnabled()) {
                disabled = false;
            }
            node.setDisable(disabled);
        }

    }

    protected void onPadModelAssignmentIndexChange() {
        // the current PadData for the assignment pad pressed
        PadData data = padModel.getSelectedAssignmentData();
        if (data == null) {
            padFocusOverlay.setVisible(false);
            return;
        }
        CtkDebug.view("onPadModelAssignmentIndexChange() " + data.getBank() + " "
                + data.getLocalIndex());
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
        Library library = getController().getLibraryManager().getSelectedLibrary();
        if (library == null)
            return "not loaded";

        LibraryPhrase phrase = library.findPhraseById(data.getPhraseId());
        //        if (phrase == null)
        Tone tone = getController().getSoundSource().getTone(data.getToneIndex());
        if (tone == null)
            return "not loaded";

        StringBuilder sb = new StringBuilder();
        sb.append(tone.getName());
        if (phrase == null)
            sb.append(":[-1,-1]");
        else {
            String pat = PatternUtils.toString(phrase.getBankIndex(), phrase.getPatternIndex());
            sb.append(":[" + pat + "]");
        }

        //sb.append(PatternUtils.toString(phrase.getBankIndex(), phrase.getPatternIndex()));
        //PatternSequencerComponent component = tone.getComponent(PatternSequencerComponent.class);

        return sb.toString();
    }
}
