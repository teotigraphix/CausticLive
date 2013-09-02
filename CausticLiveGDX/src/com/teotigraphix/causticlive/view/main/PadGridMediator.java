
package com.teotigraphix.causticlive.view.main;

import java.util.Collection;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.ISequencerModel.OnSequencerModelPropertyChange;
import com.teotigraphix.causticlive.view.components.PadGrid;
import com.teotigraphix.causticlive.view.components.PadGrid.OnPadGridListener;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueSequencer.OnQueueSequencerDataChange;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;
import com.teotigraphix.libgdx.screen.IScreen;

@Singleton
public class PadGridMediator extends MediatorBase {

    @Inject
    IDialogManager dialogManager;

    @Inject
    IScreenProvider screenProvider;

    @Inject
    ISequencerModel sequencerModel;

    private PadGrid view;

    public PadGridMediator() {
    }

    @Override
    public void create(IScreen screen) {
        view = new PadGrid(screen.getSkin());
        view.setOnPadGridListener(new OnPadGridListener() {

            @Override
            public void onLongPress(Integer localIndex, float x, float y) {
                System.out.println("Long Press");
                //                List<LibraryPhrase> phrases = getController().getLibraryManager()
                //                        .getSelectedLibrary().getPhrases();
                //
                //                final Object[] items = new String[phrases.size()];
                //                for (int i = 0; i < items.length; i++) {
                //                    items[i] = phrases.get(i).toString();
                //                }
                //                final ListDialog alert = dialogManager.createListDialog(screenProvider.getScreen(),
                //                        "Choose Phrase", items);
                //                alert.setOnAlertDialogListener(new OnAlertDialogListener() {
                //                    @Override
                //                    public void onOk() {
                //                        System.out.println(items[alert.getSelectedIndex()]);
                //                    }
                //
                //                    @Override
                //                    public void onCancel() {
                //                    }
                //                });
                //                alert.show(screenProvider.getScreen().getStage());
            }

            @Override
            public void onChange(int localIndex, boolean selected) {
                QueueData data = sequencerModel.getQueueData(sequencerModel.getSelectedBank(),
                        localIndex);
                if (selected) {
                    sequencerModel.queue(data);
                } else {
                    sequencerModel.unqueue(data);
                }

                updateView(sequencerModel.getViewData(sequencerModel.getSelectedBank()));
            }
        });

        view.setPosition(640f, 89f);
        screen.getStage().addActor(view);
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        register(OnQueueSequencerDataChange.class, new EventObserver<OnQueueSequencerDataChange>() {
            @Override
            public void trigger(OnQueueSequencerDataChange object) {
                updateView(sequencerModel.getViewData(sequencerModel.getSelectedBank()));
            }
        });

        register(sequencerModel.getDispatcher(), OnSequencerModelPropertyChange.class,
                new EventObserver<OnSequencerModelPropertyChange>() {
                    @Override
                    public void trigger(OnSequencerModelPropertyChange object) {
                        switch (object.getKind()) {
                            case Bank:
                                updateView(sequencerModel.getViewData(sequencerModel
                                        .getSelectedBank()));
                                break;
                        }
                    }
                });
    }

    protected void updateView(Collection<QueueData> viewData) {
        view.refresh(viewData, true);
    }

    @Override
    public void onRegister() {

    }

}
