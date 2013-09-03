
package com.teotigraphix.causticlive.model;

import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface IToneModel extends ICaustkModel {

    void assignTone(int toneIndex, QueueData queueData);

    void assignPatch(QueueData data, LibraryPatch libraryPatch);

}
