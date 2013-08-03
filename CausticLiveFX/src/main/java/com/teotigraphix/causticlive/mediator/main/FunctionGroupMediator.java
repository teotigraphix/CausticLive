
package com.teotigraphix.causticlive.mediator.main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.caustic.mediator.DesktopMediatorBase;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelSelectedFunctionChange;
import com.teotigraphix.causticlive.model.IPadModel.PadFunction;
import com.teotigraphix.causticlive.model.PadModel;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;

public class FunctionGroupMediator extends DesktopMediatorBase {

    private ToggleButton patternButton;

    private ToggleButton assignButton;

    @Inject
    PadModel padModel;

    @Inject
    public FunctionGroupMediator(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    public void create(Pane root) {
        Parent group = (Parent)root.lookup("#functionButtonGroup");
        ObservableList<Node> list = group.getChildrenUnmodifiable();

        patternButton = (ToggleButton)list.get(0);
        patternButton.getProperties().put("function", PadFunction.PATTERN);
        assignButton = (ToggleButton)list.get(1);
        assignButton.getProperties().put("function", PadFunction.ASSIGN);

        patternButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                if (patternButton.isSelected()) {
                    padModel.setSelectedFunction(PadFunction.PATTERN);
                }
            }
        });

        assignButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                if (assignButton.isSelected()) {
                    padModel.setSelectedFunction(PadFunction.ASSIGN);
                }
            }
        });
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        padModel.getDispatcher().register(OnPadModelSelectedFunctionChange.class,
                new EventObserver<OnPadModelSelectedFunctionChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedFunctionChange object) {
                        if (padModel.getSelectedFunction() == PadFunction.ASSIGN) {
                            assignButton.setSelected(true);
                        } else if (padModel.getSelectedFunction() == PadFunction.PATTERN) {
                            patternButton.setSelected(true);
                        }
                    }
                });
    }

}
