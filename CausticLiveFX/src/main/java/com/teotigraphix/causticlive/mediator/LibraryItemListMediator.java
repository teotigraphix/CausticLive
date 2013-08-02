
package com.teotigraphix.causticlive.mediator;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryItem;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;

@Singleton
public class LibraryItemListMediator extends DesktopMediatorBase {

    private ListView<LibraryPhrase> phraseList;

    private ListView<LibraryPatch> patchList;

    private ToggleButton phraseButton;

    private ToggleButton patchButton;

    @Inject
    public LibraryItemListMediator(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    public void create(Pane root) {
        phraseList = new ListView<LibraryPhrase>();
        layout(phraseList, 75, 120, 300, 400);
        root.getChildren().add(phraseList);

        patchList = new ListView<LibraryPatch>();
        layout(patchList, 75, 120, 300, 400);
        root.getChildren().add(patchList);

        createToolBar(root);
    }

    private void createToolBar(Pane root) {
        ToggleGroup group = new ToggleGroup();

        phraseButton = new ToggleButton("Phrase");
        phraseButton.setToggleGroup(group);
        layout(phraseButton, 75, 80, 90, 30);
        phraseButton.getProperties().put("list", 0);
        phraseButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                onToolBarChange(phraseButton);
            }
        });

        patchButton = new ToggleButton("Patch");
        patchButton.setToggleGroup(group);
        layout(patchButton, 175, 80, 90, 30);
        patchButton.getProperties().put("list", 1);
        patchButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                onToolBarChange(patchButton);
            }
        });

        root.getChildren().add(phraseButton);
        root.getChildren().add(patchButton);
    }

    protected void onToolBarChange(ToggleButton button) {
        int list = (int)button.getProperties().get("list");
        switch (list) {
            case 0:
                phraseList.setVisible(button.isSelected());
                break;
            case 1:
                patchList.setVisible(button.isSelected());
                break;
        }

    }

    public static void layout(Control node, int x, int y, int width, int height) {
        node.setLayoutX(x);
        node.setLayoutY(y);
        node.setPrefWidth(width);
        node.setPrefHeight(height);
    }

    @Override
    public void onRegister() {
        super.onRegister();
        Library library = getController().getLibraryManager().getSelectedLibrary();
        List<LibraryPhrase> phrases = library.getPhrases();
        ObservableList<LibraryPhrase> items1 = FXCollections.observableArrayList(phrases);
        phraseList.setItems(items1);

        List<LibraryPatch> patches = library.getPatches();
        ObservableList<LibraryPatch> items2 = FXCollections.observableArrayList(patches);
        patchList.setItems(items2);
    }

}
