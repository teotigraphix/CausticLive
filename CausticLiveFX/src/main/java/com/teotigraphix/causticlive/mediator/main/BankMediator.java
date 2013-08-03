
package com.teotigraphix.causticlive.mediator.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelSelectedBankChange;
import com.teotigraphix.caustk.core.CtkDebug;

@Singleton
public class BankMediator extends DesktopMediatorBase {

    private Pane bankButtonPane;

    @Inject
    IPadModel padModel;

    @Override
    public void create(Pane root) {
        bankButtonPane = (Pane)root.lookup("#bankButtonPane");
        ObservableList<Node> children = bankButtonPane.getChildren();

        int index = 0;
        for (Node node : children) {
            final ToggleButton button = (ToggleButton)node;
            button.getProperties().put("index", index);

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
            index++;
        }
    }

    protected void onBankButtonSelected(ToggleButton button) {
        CtkDebug.ui("Bank pressed " + button + ":" + button.isSelected());
        padModel.setSelectedBank((int)button.getProperties().get("index"));
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        padModel.getDispatcher().register(OnPadModelSelectedBankChange.class,
                new EventObserver<OnPadModelSelectedBankChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedBankChange object) {
                        int bank = object.getBank();
                        ToggleButton button = (ToggleButton)bankButtonPane.getChildren().get(bank);
                        button.setSelected(true);
                    }
                });
    }

    @Override
    public void onRegister() {
        // TODO Auto-generated method stub
        
    }
}
