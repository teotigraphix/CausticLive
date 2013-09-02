
package com.teotigraphix.causticlive.model;

import java.util.Collection;

import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueDataChannel;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface ISequencerModel extends ICaustkModel {

    int getSelectedBank();

    /**
     * @param value
     * @see OnSequencerModelPropertyChange
     * @see PropertyChangeKind#Bank
     */
    void setSelectedBank(int value);

    boolean queue(QueueData data);

    boolean unqueue(QueueData data);

    Collection<QueueData> getViewData(int bankIndex);

    QueueData getQueueData(int bankIndex, int patternIndex);

    QueueDataChannel getChannel(int bankIndex, int patternIndex, int toneIndex);

    public enum PropertyChangeKind {
        Bank;
    }

    public static class OnSequencerModelPropertyChange {

        private final PropertyChangeKind kind;

        public final PropertyChangeKind getKind() {
            return kind;
        }

        public OnSequencerModelPropertyChange(PropertyChangeKind kind) {
            this.kind = kind;
        }
    }

    void play();

    void stop();

}
