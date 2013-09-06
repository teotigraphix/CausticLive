
package com.teotigraphix.causticlive.model;

import java.util.UUID;

import com.teotigraphix.caustk.library.item.LibraryPatch;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.sequencer.queue.QueueDataChannel;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface IToneModel extends ICaustkModel {

    /**
     * The currently edited {@link QueueDataChannel} index.
     * 
     * @see ISequencerModel#getActiveData()
     */
    int getEditChannel();

    /**
     * Sets the currently edited {@link QueueDataChannel} index.
     * 
     * @param value 0-13
     * @see ISequencerModel#getActiveData()
     */
    void setEditChannel(int value);

    /**
     * Returns the id of the {@link LibraryPatch} applied to the toneIndex.
     * 
     * @param toneIndex
     */
    UUID getPatchId(int toneIndex);

    /**
     * @param toneIndex
     * @param queueData
     * @see OnToneModelMachineIndexChange
     */
    void assignTone(int toneIndex, QueueData queueData);

    void assignPatch(QueueData data, LibraryPatch libraryPatch);

    public enum ToneModelPropertyChangeKind {
        ChannelIndex
    }

    public static class OnToneModelPropertyChange {

        private IToneModel model;

        private ToneModelPropertyChangeKind kind;

        public IToneModel getModel() {
            return model;
        }

        public ToneModelPropertyChangeKind getKind() {
            return kind;
        }

        public OnToneModelPropertyChange(IToneModel model, ToneModelPropertyChangeKind kind) {
            this.model = model;
            this.kind = kind;
        }

    }

    public static class OnToneModelMachineIndexChange {

    }

}
