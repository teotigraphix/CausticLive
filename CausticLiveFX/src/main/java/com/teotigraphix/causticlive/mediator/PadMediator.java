
package com.teotigraphix.causticlive.mediator;

import java.util.ArrayList;
import java.util.List;

import org.androidtransfuse.event.EventObserver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.causticlive.model.PadModel;
import com.teotigraphix.causticlive.model.PadModel.OnPadModelPadAssign;
import com.teotigraphix.causticlive.model.PadModel.OnPadModelSelectedBankChange;
import com.teotigraphix.causticlive.model.PadModel.PadData;
import com.teotigraphix.causticlive.model.PadModel.PadDataState;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.core.PatternUtils;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.tone.Tone;

@Singleton
public class PadMediator extends DesktopMediatorBase {

    PadModel padModel;

    List<ToggleButton> pads = new ArrayList<>();

    private boolean updatingView;

    @Inject
    public PadMediator(ICaustkApplicationProvider provider, PadModel padModel) {
        super(provider);
        this.padModel = padModel;
    }

    @Override
    public void create(Pane root) {
        CtkDebug.view("   Create: PadMediator");

        createBankButtons(root);

        double buttonWidth = 115;
        double buttonHeight = 115;

        double startX = 650;
        double startY = 117;

        double calcX = startX;
        double calcY = startY;

        double gap = 29;

        int i = 0;
        for (int row = 0; row < 4; row++) {

            for (int column = 0; column < 4; column++) {

                final ToggleButton button = new ToggleButton("" + i);
                button.setId("pad" + i);
                button.setLayoutX(calcX);
                button.setLayoutY(calcY);
                button.setPrefSize(buttonWidth, buttonHeight);
                button.getProperties().put("index", i);
                button.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable,
                            Boolean oldValue, Boolean newValue) {
                        onPadButtonSelected(button);
                    }
                });
                root.getChildren().add(button);
                pads.add(button);

                calcX += gap + buttonWidth;
                i++;
            }
            calcX = startX;
            calcY = calcY + buttonHeight + gap;
        }
    }

    private void createBankButtons(Pane root) {

        double buttonWidth = 50;
        double buttonHeight = 50;

        double startX = 550;
        double startY = 120;

        double calcX = startX;
        double calcY = startY;

        double gap = 20;

        ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 0; i < 4; i++) {
            final ToggleButton button = new ToggleButton("" + i);
            button.setId("bank" + i);
            button.setLayoutX(calcX);
            button.setLayoutY(calcY);
            button.setPrefSize(buttonWidth, buttonHeight);
            button.getProperties().put("index", i);
            button.setToggleGroup(toggleGroup);
            button.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable,
                        Boolean oldValue, Boolean newValue) {
                    onBankButtonSelected(button);
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
            root.getChildren().add(button);

            calcY += gap + buttonHeight;
        }

    }

    protected void onBankButtonSelected(ToggleButton button) {
        CtkDebug.ui("Bank pressed " + button + ":" + button.isSelected());
        padModel.setSelectedBank((int)button.getProperties().get("index"));
    }

    protected void onPadButtonSelected(ToggleButton button) {
        CtkDebug.ui("Pad pressed " + button + ":" + button.isSelected());
        if (updatingView)
            return;
        padModel.select((int)button.getProperties().get("index"), button.isSelected());
    }

    @Override
    public void onRegister() {
        super.onRegister();
        CtkDebug.view("   Register: PadMediator");

        padModel.setSelectedBank(0);
    }

    protected void updatePadView(OnPadModelSelectedBankChange object) {
        updatingView = true;
        List<PadData> list = object.getView();
        for (PadData data : list) {
            ToggleButton button = pads.get(data.getLocalIndex());
            if (data.getState() == PadDataState.IDLE) {
                button.setSelected(false);
            } else if (data.getState() == PadDataState.SELECTED) {
                button.setSelected(true);
            }
            String text = getButtonText(data);
            button.setText(text);
        }
        updatingView = false;
    }

    private String getButtonText(PadData data) {
        LibraryPhrase phrase = getController().getLibraryManager().getSelectedLibrary()
                .findPhraseById(data.getPhraseId());
        if (phrase == null)
            return "not loaded";
        Tone tone = getController().getSoundSource().getTone(data.getToneIndex());
        StringBuilder sb = new StringBuilder();
        sb.append(tone.getName());
        sb.append(":");
        sb.append(PatternUtils.toString(phrase.getBankIndex(), phrase.getPatternIndex()));
        //PatternSequencerComponent component = tone.getComponent(PatternSequencerComponent.class);

        return sb.toString();
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        getController().getDispatcher().register(OnPadModelSelectedBankChange.class,
                new EventObserver<OnPadModelSelectedBankChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedBankChange object) {
                        updatePadView(object);
                    }
                });

        // PadModel
        padModel.getDispatcher().register(OnPadModelPadAssign.class,
                new EventObserver<OnPadModelPadAssign>() {
                    @Override
                    public void trigger(OnPadModelPadAssign object) {
                        onPadModelPadAssign(object.getData());
                    }
                });
    }

    protected void onPadModelPadAssign(PadData data) {
        // Update the buttons view
        if (isInView(data)) {

        }

    }

    private boolean isInView(PadData data) {
        return false;
    }
}
