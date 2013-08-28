
package com.teotigraphix.causticlive.view;

import java.util.List;

import org.androidtransfuse.event.EventObserver;

import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog.OnAlertDialogListener;
import com.badlogic.gdx.scenes.scene2d.ui.ListDialog;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelSelectedDataChange;
import com.teotigraphix.causticlive.model.IPadModel.PadDataState;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.causticlive.model.vo.PadData;
import com.teotigraphix.causticlive.view.components.PadGrid;
import com.teotigraphix.causticlive.view.components.PadGrid.OnPadGridListener;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ToggleButton;

@Singleton
public class PadGridMediator extends MediatorBase {

    @Inject
    IPadModel padModel;

    @Inject
    ISoundModel soundModel;

    @Inject
    IDialogManager dialogManager;

    @Inject
    IScreenProvider screenProvider;

    private PadGrid view;

    public PadGridMediator() {

    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        register(padModel.getDispatcher(), OnPadModelSelectedDataChange.class,
                new EventObserver<OnPadModelSelectedDataChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedDataChange object) {
                        updatePadView(padModel.getPadDataView());
                    }
                });
    }

    protected void updatePadView(List<PadData> list) {
        for (PadData data : list) {
            int localIndex = data.getLocalIndex();
            ToggleButton padButton = (ToggleButton)view.getChildren().get(localIndex);
            //padButton.setDisabled(!data.hasChannels());
            if (data.getState() == PadDataState.IDLE) {
                padButton.setChecked(false);
            } else if (data.getState() == PadDataState.SELECTED) {
                padButton.setChecked(true);
            }
            padButton.setText(data.getState().name());
        }
    }

    @Override
    public void create(IScreen screen) {
        super.create(screen);

        view = new PadGrid(screen.getSkin());
        view.setOnPadGridListener(new OnPadGridListener() {
            @Override
            public void onLongPress(Integer localIndex, float x, float y) {
                // padModel.edit(localIndex);
                List<LibraryPhrase> phrases = getController().getLibraryManager()
                        .getSelectedLibrary().getPhrases();

                final Object[] items = new String[phrases.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = phrases.get(i).toString();
                }
                final ListDialog alert = dialogManager.createListDialog(screenProvider.getScreen(),
                        "Choose Phrase", items);
                alert.setOnAlertDialogListener(new OnAlertDialogListener() {
                    @Override
                    public void onOk() {
                        System.out.println(items[alert.getSelectedIndex()]);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                alert.show(screenProvider.getScreen().getStage());
            }

            @Override
            public void onChange(int localIndex, boolean selected) {
                PadData data = padModel.getLocalData(localIndex);

                if (selected) {
                    soundModel.queue(data);
                } else {
                    soundModel.unqueue(data);
                }

                updatePadView(padModel.getPadDataView());
            }
        });
        view.setPosition(640f, 89f);
        screen.getStage().addActor(view);
    }

    @Override
    public void onRegister() {

    }
}
