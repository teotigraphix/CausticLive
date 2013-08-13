
package com.teotigraphix.causticlive.mediator.assignment;

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
import com.teotigraphix.causticlive.model.IChannelModel;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.model.IChannelModel.OnChannelModelSelectedChannelChange;
import com.teotigraphix.causticlive.model.ISoundModel.OnSoundModelLibraryImportComplete;
import com.teotigraphix.causticlive.model.PadChannel;
import com.teotigraphix.causticlive.model.PadData;
import com.teotigraphix.causticlive.screen.AssignmentScreenView;
import com.teotigraphix.causticlive.utils.ImageUtils;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.library.ILibraryManager.OnLibraryManagerSelectedLibraryChange;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.pattern.PatternUtils;
import com.teotigraphix.caustk.tone.Tone;

@Singleton
public class AssignmentControlsMediator extends DesktopMediatorBase {

    private Button backButton;

    @Inject
    ISoundModel soundModel;

    @Inject
    IPadModel padModel;

    @Inject
    IScreenManager screenManager;

    @Inject
    IChannelModel channelModel;

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

        phraseList = (ListView<LibraryPhrase>)root.lookup("#phraseList");
        //phraseList.setDisable(true);

        // Grid
        int index = 0;
        for (Node node : channelGroup.getChildren()) {
            final ToggleButton button = (ToggleButton)node;
            button.getProperties().put("index", index);
            button.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable,
                        Boolean oldValue, Boolean newValue) {
                    onChannelGroupSelected(button);
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
            //button.setDisable(true);
            index++;
        }
    }

    protected void onMachineGroupSelected(ToggleButton button) {
        // Assign machine to channel
        //PadChannel channel = channelModel.getSelectedChannel();
        //channel.setEnabled(false);
        //onShow();
    }

    protected void onChannelGroupSelected(ToggleButton button) {
        if (!button.isSelected())
            return;

        int toneIndex = (int)button.getProperties().get("index");
        PadData selectedData = padModel.getSelectedData();
        PadChannel channel = selectedData.getChannel(toneIndex);
        channelModel.setSelectedChannel(channel);
    }

    protected void onHide() {
        CtkDebug.view("Hide");
    }

    protected void onShow() {

        // update controls based on selectedData
        PadData selectedData = padModel.getSelectedData();
        CtkDebug.view("Show;" + selectedData);

        List<PadChannel> channleView = channelModel.getChannleView();
        // update each
        int toneIndex = 0;
        for (Node node : channelGroup.getChildren()) {
            ToggleButton button = (ToggleButton)node;
            PadChannel channel = selectedData.findChannel(toneIndex);

            Tone tone = getController().getSoundSource().getTone(toneIndex);
            if (tone != null) {
                // update image
                ImageUtils.assignMachineIcon(tone, button);

                if (channel != null) {

                    // update label
                    button.setText(tone.getName()
                            + " "
                            + PatternUtils.toString(channel.getBankIndex(),
                                    channel.getPatternIndex()));
                } else {
                    // button.setGraphic(null);
                    button.setText("Unassigned");
                }
            } else {
                button.setText("Empty");
                //button.setDisable(true);
            }

            toneIndex++;
        }
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        channelModel.getDispatcher().register(OnChannelModelSelectedChannelChange.class,
                new EventObserver<OnChannelModelSelectedChannelChange>() {
                    @Override
                    public void trigger(OnChannelModelSelectedChannelChange object) {
                        onChannelModelSelectedChannelChange(object.getChannel());
                    }
                });

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

    protected void onChannelModelSelectedChannelChange(PadChannel channel) {

        // update phrase selectedItem

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
        PadChannel channel = channelModel.getSelectedChannel();
        if (channel == null)
            return;

        channelModel.assignPhrase(channel, libraryPhrase);

        onShow();
    }
}
