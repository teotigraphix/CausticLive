
package com.teotigraphix.causticlive.mediator.main;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;

import com.google.inject.Inject;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.caustic.screen.IScreenManager;
import com.teotigraphix.caustic.utils.FileUtil;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.screen.MachineScreenView;

public class ToolBarMediator extends DesktopMediatorBase {

    private Button machineViewButton;

    private Pane machineToggleButtons;

    //

    @Inject
    IScreenManager screenManager;

    @Inject
    ISoundModel soundModel;

    @Inject
    IPadModel padModel;

    private boolean reseting;

    private Button loadButton;

    @SuppressWarnings("unchecked")
    @Override
    public void create(Pane root) {

        loadButton = (Button)root.lookup("#loadButton");
        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onLoadClick();
            }
        });

        machineViewButton = (Button)root.lookup("#machineViewButton");
        machineViewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onMachineViewClick();
            }
        });

        // selectors on the main screen for the current assignment tone
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

    }

    protected void onLoadClick() {
        System.out.println("onLoadClick");
        //        FileChooser chooser = FileUtil.createDefaultFileChooser(RuntimeUtils
        //                .getCausticSongsDirectory().getAbsolutePath(), "Caustic song file", "*.caustic");
        //        File causticFile = chooser.showOpenDialog(null);
        //
        //        // if no current library open error dialog
        //        Library library = getController().getLibraryManager().getSelectedLibrary();
        //        try {
        //            getController().getLibraryManager().importSong(library, causticFile);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        } catch (CausticException e) {
        //            e.printStackTrace();
        //        }

        DirectoryChooser chooser = FileUtil.createDefaultDirectoryChooser(null,
                "Choose library directory");
        File file = chooser.showDialog(null);
        if (file == null)
            return;
        soundModel.loadLibrary(file);
    }

    @Override
    public void onRegister() {

    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        //        padModel.getDispatcher().register(OnPadModelAssignmentIndexChange.class,
        //                new EventObserver<OnPadModelAssignmentIndexChange>() {
        //                    @Override
        //                    public void trigger(OnPadModelAssignmentIndexChange object) {
        //                        if (object.isNoSelection()) {
        //                            phraseList.setDisable(true);
        //                            return;
        //                        } else {
        //                            phraseList.setDisable(false);
        //                            onPadModelAssignmentIndexChange();
        //                        }
        //                    }
        //                });

    }

    protected void onPadModelAssignmentIndexChange() {
        //        if (soundModel.getLibrary() == null)
        //            return;
        //
        //        PadData data = padModel.getSelectedAssignmentData();
        //        int toneIndex = data.getToneIndex();
        //        //reseting = true;
        //        for (Node node : machineToggleButtons.getChildren()) {
        //            ToggleButton button = (ToggleButton)node;
        //            button.disarm();
        //            button.setSelected(false);
        //            button.arm();
        //        }
        //        //reseting = false;
        //
        //        LibraryPhrase phrase = soundModel.getLibrary().findPhraseById(data.getPhraseId());
        //        if (phrase != null) {
        //            phraseList.getSelectionModel().select(phrase);
        //            phraseList.scrollTo(phraseList.getItems().indexOf(phrase));
        //        }
        //
        //        if (toneIndex != -1) {
        //            ToggleButton button = (ToggleButton)machineToggleButtons.getChildren().get(toneIndex);
        //            button.disarm();
        //            button.setSelected(true);
        //            button.arm();
        //        }
    }

    protected void onMachineButtonSelected(ToggleButton button) {
        //        if (reseting)
        //            return;
        //        // set the tone index
        //        // this has to update the pad text view
        //        int toneIndex = (int)button.getProperties().get("index");
        //        padModel.setAssignmentToneIndex(toneIndex);
    }

    protected void onMachineViewClick() {
        screenManager.showPopUp(MachineScreenView.class);
    }

}
