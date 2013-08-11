
package com.teotigraphix.causticlive.model;

import java.util.List;

import com.teotigraphix.caustic.model.ICaustkModel;
import com.teotigraphix.caustk.library.LibraryPhrase;

public interface IChannelModel extends ICaustkModel {

    /**
     * Returns all the {@link PadChannel}s in view for the
     * {@link IPadModel#getSelectedData()}.
     */
    List<PadChannel> getChannleView();

    /**
     * @param value
     * @see OnChannelModelSelectedChannelChange
     */
    void setSelectedChannel(PadChannel value);

    PadChannel getSelectedChannel();

    public static class OnChannelModelSelectedChannelChange {

        private PadChannel channel;

        public final PadChannel getChannel() {
            return channel;
        }

        public OnChannelModelSelectedChannelChange(PadChannel channel) {
            this.channel = channel;
        }
    }

    boolean assignPhrase(PadChannel channel, LibraryPhrase phrase);
}
