
package com.teotigraphix.causticlive.model;

import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface IToneModel extends ICaustkModel {

    void assignTone(int toneIndex, QueueData queueData);

}
