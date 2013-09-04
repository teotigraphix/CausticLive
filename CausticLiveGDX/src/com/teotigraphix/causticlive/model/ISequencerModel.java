
package com.teotigraphix.causticlive.model;

import java.util.Collection;

import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueDataChannel;
import com.teotigraphix.caustk.sequencer.track.TrackChannel;
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
        Bank,

        ActiveData;
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

    boolean isRecordMode();

    void setRecordMode(boolean value);

    void play();

    void stop();

    void assignPhrase(QueueData data, TrackChannel trackChannel, LibraryPhrase libraryPhrase);

    QueueData getActiveData();

    void setActiveData(QueueData queueData);

}
