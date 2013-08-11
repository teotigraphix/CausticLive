
package com.teotigraphix.causticlive.model;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ICaustkModelState;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.LibraryPhrase;

@Singleton
public class ChannelModel extends ModelBase implements IChannelModel {

    @Inject
    IPadMapModel padMapModel;

    @Inject
    IPadModel padModel;

    private PadChannel selectedChannel;

    @Override
    public void setSelectedChannel(PadChannel value) {
        selectedChannel = value;
        trigger(new OnChannelModelSelectedChannelChange(selectedChannel));
    }

    @Override
    public PadChannel getSelectedChannel() {
        return selectedChannel;
    }

    @Override
    public List<PadChannel> getChannleView() {
        return padModel.getSelectedData().getChannels();
    }

    public ChannelModel() {
        setStateFactory(ChannelModelState.class);
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onRegister() {

    }

    @Override
    public boolean assignPhrase(PadChannel channel, LibraryPhrase phrase) {
        channel.assignPhrase(phrase);
        return true;
    }

    public static class ChannelModelState implements ICaustkModelState {

        public ChannelModelState() {
        }

        @Override
        public void sleep() {
        }

        @Override
        public void wakeup(ICaustkController controller) {
        }

    }

}
