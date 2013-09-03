
package com.teotigraphix.causticlive.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.IQueueSequencer;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueDataChannel;
import com.teotigraphix.libgdx.model.ICaustkModelState;
import com.teotigraphix.libgdx.model.ModelBase;

@Singleton
public class SequencerModel extends ModelBase implements ISequencerModel {

    private QueueData activeData;

    @Override
    public QueueData getActiveData() {
        return activeData;
    }

    @Override
    public void setActiveData(QueueData value) {
        activeData = value;
    }

    protected IQueueSequencer getQueueSequencer() {
        return getController().getQueueSequencer();
    }

    @Override
    public boolean isRecordMode() {
        return getQueueSequencer().isRecordMode();
    }

    @Override
    public void setRecordMode(boolean value) {
        getQueueSequencer().setRecordMode(value);
    }

    @Override
    public void play() {
        try {
            getQueueSequencer().play();
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        getQueueSequencer().stop();
    }

    @Override
    protected SequencerModelState getState() {
        return (SequencerModelState)super.getState();
    }

    // get view pads
    @Override
    public Collection<QueueData> getViewData(int bankIndex) {
        Map<Integer, QueueData> view = getController().getQueueSequencer().getView(bankIndex);
        // this collection may not hold all 16 datas since we lazy load
        // will contain 16 items either null or QueueData
        Collection<QueueData> collection = new ArrayList<QueueData>(view.values());
        return collection;
    }

    @Override
    public int getSelectedBank() {
        return getState().getSelectedBank();
    }

    @Override
    public void setSelectedBank(int value) {
        getState().setSelectedBank(value);
        getDispatcher().trigger(new OnSequencerModelPropertyChange(PropertyChangeKind.Bank));
    }

    @Override
    public boolean queue(QueueData data) {
        boolean result = getController().getQueueSequencer().queue(data);
        return result;
    }

    @Override
    public boolean unqueue(QueueData data) {
        boolean result = getController().getQueueSequencer().unqueue(data);
        return result;
    }

    @Override
    public QueueData getQueueData(int bankIndex, int patternIndex) {
        return getController().getQueueSequencer().getQueueData(bankIndex, patternIndex);
    }

    @Override
    public QueueDataChannel getChannel(int bankIndex, int patternIndex, int toneIndex) {
        return getController().getQueueSequencer().getChannel(bankIndex, patternIndex, toneIndex);
    }

    public SequencerModel() {
        setStateFactory(SequencerModelState.class);
    }

    @Override
    protected void restoreState(ICaustkModelState state) {
        super.restoreState(state);

        setSelectedBank(getSelectedBank());
    }

    @Override
    public void onRegister() {

    }

    @Override
    public void onShow() {
    }

    public static class SequencerModelState implements ICaustkModelState {

        private transient ICaustkController controller;

        ICaustkController getController() {
            return controller;
        }

        private int selectedBank = 0;

        public int getSelectedBank() {
            return selectedBank;
        }

        public void setSelectedBank(int value) {
            selectedBank = value;
        }

        @Override
        public void sleep() {
        }

        @Override
        public void wakeup(ICaustkController controller) {
            this.controller = controller;
        }
    }

    @Override
    public void assignPhrase(QueueData data, LibraryPhrase libraryPhrase) {
        data.setPhraseId(libraryPhrase.getId());
        QueueDataChannel channel = data.getChannel(0);
        int bankIndex = data.getBankIndex();
        int patternIndex = data.getPatternIndex();

        channel.assignPhrase(libraryPhrase);

    }

}
