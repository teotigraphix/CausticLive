
package com.teotigraphix.causticlive.model;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.model.ModelBase;

@Singleton
public class ToneModel extends ModelBase implements IToneModel {

    @Override
    public void assignTone(int toneIndex, QueueData queueData) {
        queueData.setViewChannel(toneIndex);
    }

    public ToneModel() {
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow() {
    }

}
