
package com.teotigraphix.causticlive.mediator.machine;

import java.util.List;

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

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.caustic.screen.IScreenManager;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.model.SoundModel.OnSoundModelSceneLoad;
import com.teotigraphix.causticlive.model.SoundModel.OnSoundModelSelectedToneChange;
import com.teotigraphix.causticlive.model.SoundModel.ToneData;
import com.teotigraphix.causticlive.screen.MachineScreenView;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerSelectedLibraryChange;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.tone.Tone;

@Singleton
public class MachineScreenMediator extends DesktopMediatorBase {

    private Button backButton;

    private Button loadSceneButton;

    private ListView<LibraryScene> sceneList;

    private ListView<LibraryPatch> patchList;

    private Pane machineButtonPane;

    @Inject
    ISoundModel soundModel;

    @Inject
    IScreenManager screenManager;

    @SuppressWarnings("unchecked")
    @Override
    public void create(Pane root) {

        machineButtonPane = (Pane)root.lookup("#machineButtonPane");

        backButton = (Button)root.lookup("#backButton");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onBackClick();
            }
        });

        loadSceneButton = (Button)root.lookup("#loadSceneButton");
        loadSceneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onLoadScene();
            }
        });

        sceneList = (ListView<LibraryScene>)root.lookup("#sceneList");

        patchList = (ListView<LibraryPatch>)root.lookup("#patchList");
    }

    protected void onLoadScene() {
        LibraryScene item = sceneList.getSelectionModel().getSelectedItem();
        if (item == null) {

            return;
        }

        soundModel.loadScene(item);
    }

    protected void onBackClick() {
        screenManager.hidePopUp(MachineScreenView.class);
    }

    @Override
    public void onRegister() {
        fillLists();

        patchList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<LibraryPatch>() {
                    @Override
                    public void changed(ObservableValue<? extends LibraryPatch> value,
                            LibraryPatch arg1, LibraryPatch arg2) {
                        LibraryPatch libraryPatch = value.getValue();
                        if (libraryPatch != null) {
                            boolean success = soundModel.setPatch(soundModel.getSelectedToneData(),
                                    libraryPatch);
                            if (!success) {
                                patchList.getSelectionModel().clearSelection(
                                        patchList.getSelectionModel().getSelectedIndex());
                            }
                        }
                    }
                });

        int index = 0;
        for (Node node : machineButtonPane.getChildren()) {
            final ToggleButton button = (ToggleButton)node;
            button.getProperties().put("index", index);
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
            button.setDisable(true);
            index++;
        }
    }

    protected void onMachineButtonSelected(ToggleButton button) {
        int toneIndex = (int)button.getProperties().get("index");
        if (button.isSelected()) {
            soundModel.setSelectedTone(toneIndex);
        }
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        // SoundModel

        // OnSoundModelSceneLoad
        soundModel.getDispatcher().register(OnSoundModelSceneLoad.class,
                new EventObserver<OnSoundModelSceneLoad>() {
                    @Override
                    public void trigger(OnSoundModelSceneLoad object) {
                        updateMachineButtons();
                    }
                });

        // OnSoundModelSelectedToneChange
        soundModel.getDispatcher().register(OnSoundModelSelectedToneChange.class,
                new EventObserver<OnSoundModelSelectedToneChange>() {
                    @Override
                    public void trigger(OnSoundModelSelectedToneChange object) {
                        updateSelectedTone(object);
                    }
                });

        // OnSoundModelLibraryImportComplete
        //        soundModel.getDispatcher().register(OnSoundModelLibraryImportComplete.class,
        //                new EventObserver<OnSoundModelLibraryImportComplete>() {
        //                    @Override
        //                    public void trigger(OnSoundModelLibraryImportComplete object) {
        //                        fillLists();
        //                    }
        //                });

        getController().getDispatcher().register(OnLibraryManagerSelectedLibraryChange.class,
                new EventObserver<OnLibraryManagerSelectedLibraryChange>() {
                    @Override
                    public void trigger(OnLibraryManagerSelectedLibraryChange object) {
                        fillLists();
                    }
                });
    }

    protected void fillLists() {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        if (library == null)
            return;
        
        List<LibraryScene> phrases = library.getScenes();
        ObservableList<LibraryScene> items1 = FXCollections.observableArrayList(phrases);
        sceneList.setItems(items1);

        List<LibraryPatch> patches = library.getPatches();
        ObservableList<LibraryPatch> items2 = FXCollections.observableArrayList(patches);
        patchList.setItems(items2);
    }

    protected void updateSelectedTone(OnSoundModelSelectedToneChange object) {
        // set the selection in the patch list
        ToneData data = object.getTone();
        // clear selection
        if (data == null)
            return;
        LibraryPatch libraryPatch = data.getLibraryPatch();
        if (libraryPatch == null) {
            patchList.getSelectionModel().clearSelection();
            return;
        }

        patchList.getSelectionModel().select(libraryPatch);

        // update tone button
        // XXX this is causing recursion
        //ToggleButton button = (ToggleButton)machineButtonPane.getChildren().get(data.getIndex());
        //button.setSelected(true);
    }

    /**
     * Updates the machine button when a new {@link LibraryScene} is loaded.
     */
    protected void updateMachineButtons() {
        for (Node node : machineButtonPane.getChildren()) {
            ToggleButton button = (ToggleButton)node;
            String name = "";
            button.setDisable(true);
            Tone tone = getController().getSoundSource().getTone(
                    (int)button.getProperties().get("index"));
            if (tone != null) {
                name = tone.getName();
                button.setDisable(false);
            }
            button.setText(name);

        }
    }
}
