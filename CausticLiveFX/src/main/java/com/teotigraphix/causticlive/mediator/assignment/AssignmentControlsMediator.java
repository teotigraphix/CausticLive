
package com.teotigraphix.causticlive.mediator.assignment;

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
import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.caustic.screen.IScreenManager;
import com.teotigraphix.caustic.utils.UIUtils;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.model.ISoundModel.OnSoundModelLibraryImportComplete;
import com.teotigraphix.causticlive.model.PadData;
import com.teotigraphix.causticlive.screen.AssignmentScreenView;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerSelectedLibraryChange;

@Singleton
public class AssignmentControlsMediator extends DesktopMediatorBase {

    private Button backButton;

    @Inject
    ISoundModel soundModel;

    @Inject
    IPadModel padModel;

    @Inject
    IScreenManager screenManager;

    private ListView<LibraryPhrase> phraseList;

    private Pane channelGroup;

    public AssignmentControlsMediator() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void create(Pane root) {
        root.visibleProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                if (observable.getValue()) {
                    onShow();
                } else {
                    onHide();
                }
            }
        });

        backButton = (Button)root.lookup("#backButton");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                onBackClick();
            }
        });

        channelGroup = (Pane)root.lookup("#channelGroup");
        //UIUtils.setSelected(channelGroup.getChildren(), false);
        
        phraseList = (ListView<LibraryPhrase>)root.lookup("#phraseList");
        phraseList.setDisable(true);
    }

    protected void onHide() {
        CtkDebug.view("Hide");
    }

    protected void onShow() {

        // update controls based on selectedData
        PadData selectedData = padModel.getSelectedData();
        CtkDebug.view("Show;" + selectedData);
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        soundModel.getDispatcher().register(OnSoundModelLibraryImportComplete.class,
                new EventObserver<OnSoundModelLibraryImportComplete>() {
                    @Override
                    public void trigger(OnSoundModelLibraryImportComplete object) {
                        fillPhraseList();
                    }
                });

        getController().getDispatcher().register(OnLibraryManagerSelectedLibraryChange.class,
                new EventObserver<OnLibraryManagerSelectedLibraryChange>() {
                    @Override
                    public void trigger(OnLibraryManagerSelectedLibraryChange object) {
                        fillPhraseList();
                    }
                });
    }

    @Override
    public void onRegister() {
        phraseList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<LibraryPhrase>() {
                    @Override
                    public void changed(ObservableValue<? extends LibraryPhrase> value,
                            LibraryPhrase arg1, LibraryPhrase arg2) {
                        LibraryPhrase libraryPhrase = value.getValue();
                        onPhraseListselectedItemChange(libraryPhrase);
                    }
                });

        fillPhraseList();
    }

    private void fillPhraseList() {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        if (library == null)
            return;
        List<LibraryPhrase> phrases = library.getPhrases();
        ObservableList<LibraryPhrase> items1 = FXCollections.observableArrayList(phrases);
        phraseList.setItems(items1);
    }

    protected void onBackClick() {
        screenManager.hidePopUp(AssignmentScreenView.class);
    }

    protected void onPhraseListselectedItemChange(LibraryPhrase libraryPhrase) {
        //        if (reseting)
        //            return;
        //
        //        if (libraryPhrase == null) {
        //            CtkDebug.err("ToolBarMediator.onPhraseListselectedItemChange() libraryPhrase NULL");
        //            return;
        //        }
        //
        //        padModel.setAssignmentPhraseId(libraryPhrase.getId());

    }
}
