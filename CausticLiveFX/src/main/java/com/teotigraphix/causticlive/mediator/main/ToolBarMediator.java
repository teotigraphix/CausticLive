
package com.teotigraphix.causticlive.mediator.main;

import java.util.List;

import org.androidtransfuse.event.EventObserver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.caustic.screen.IScreenManager;
import com.teotigraphix.causticlive.model.PadModel;
import com.teotigraphix.causticlive.model.PadModel.OnPadModelAssignmentIndexChange;
import com.teotigraphix.causticlive.model.PadModel.PadData;
import com.teotigraphix.causticlive.model.SoundModel;
import com.teotigraphix.causticlive.model.SoundModel.OnSoundModelSelectedToneChange;
import com.teotigraphix.causticlive.model.SoundModel.ToneData;
import com.teotigraphix.causticlive.sceen.MachineScreenView;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.library.LibraryScene;

public class ToolBarMediator extends DesktopMediatorBase {

    private Button machineViewButton;

    private Pane machineToggleButtons;

    private ListView<LibraryPhrase> phraseList;

    @Inject
    IScreenManager screenManager;

    @Inject
    SoundModel soundModel;

    @Inject
    PadModel padModel;

    private boolean reseting;

    @Inject
    public ToolBarMediator(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void create(Pane root) {
        machineViewButton = (Button)root.lookup("#machineViewButton");
        machineViewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onMachineViewClick();
            }
        });

        machineToggleButtons = (Pane)root.lookup("#machineToggleButtons");
        for (Node node : machineToggleButtons.getChildren()) {
            final ToggleButton button = (ToggleButton)node;
            button.getProperties().put("index", machineToggleButtons.getChildren().indexOf(button));

            button.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable,
                        Boolean oldValue, Boolean newValue) {
                    onMachineButtonSelected(button);
                }
            });

            // XXX have to add KeyEvent for space, enter
            button.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (button.isSelected())
                        event.consume();
                }
            });
        }

        phraseList = (ListView<LibraryPhrase>)root.lookup("#phraseList");

    }

    @Override
    public void onRegister() {
        super.onRegister();

        Library library = getController().getLibraryManager().getSelectedLibrary();

        List<LibraryPhrase> phrases = library.getPhrases();
        ObservableList<LibraryPhrase> items1 = FXCollections.observableArrayList(phrases);
        phraseList.setItems(items1);

        phraseList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<LibraryPhrase>() {
                    @Override
                    public void changed(ObservableValue<? extends LibraryPhrase> value,
                            LibraryPhrase arg1, LibraryPhrase arg2) {
                        LibraryPhrase libraryPhrase = value.getValue();
                        onPhraseListselectedItemChange(libraryPhrase);
                    }
                });
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        soundModel.getDispatcher().register(OnSoundModelSelectedToneChange.class,
                new EventObserver<OnSoundModelSelectedToneChange>() {
                    @Override
                    public void trigger(OnSoundModelSelectedToneChange object) {
                        ToneData tone = object.getTone();
                        if (tone != null) {
                            ToggleButton button = (ToggleButton)machineToggleButtons.getChildren()
                                    .get(tone.getIndex());
                            button.setSelected(true);
                        }
                    }
                });

        padModel.getDispatcher().register(OnPadModelAssignmentIndexChange.class,
                new EventObserver<OnPadModelAssignmentIndexChange>() {
                    @Override
                    public void trigger(OnPadModelAssignmentIndexChange object) {
                        onPadModelAssignmentIndexChange();
                    }
                });

    }

    protected void onPhraseListselectedItemChange(LibraryPhrase libraryPhrase) {
        if (reseting)
            return;
        
        PadData data = padModel.getAssignmentData();
        data.setPhraseId(libraryPhrase.getId());
    }

    protected void onPadModelAssignmentIndexChange() {
        PadData data = padModel.getAssignmentData();
        int toneIndex = data.getToneIndex();
        reseting = true;
        for (Node node : machineToggleButtons.getChildren()) {
            ToggleButton button = (ToggleButton)node;
            button.setSelected(false);
        }
        reseting = false;

        if (toneIndex != -1) {
            ToggleButton button = (ToggleButton)machineToggleButtons.getChildren().get(toneIndex);
            button.setSelected(true);
        }
    }

    protected void onMachineButtonSelected(ToggleButton button) {
        if (reseting)
            return;
        // set the tone index
        // this has to update the pad text view
        PadData data = padModel.getAssignmentData();
        int toneIndex = (int)button.getProperties().get("index");
        data.setToneIndex(toneIndex);
    }

    protected void onMachineViewClick() {
        screenManager.showPopUp(MachineScreenView.class);
    }

}
