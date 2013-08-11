
package com.teotigraphix.causticlive.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.causticlive.model.IPadModel.PadDataState;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;

public class PadData implements ISerialize {

    private transient ICaustkController controller;

    private Map<Integer, PadChannel> map = new TreeMap<Integer, PadChannel>();

    //----------------------------------
    // index
    //----------------------------------

    private int index;

    public final int getIndex() {
        return index;
    }

    //----------------------------------
    // bank
    //----------------------------------

    private int bank;

    public final int getBank() {
        return bank;
    }

    //----------------------------------
    // localIndex
    //----------------------------------

    private int localIndex;

    public final int getLocalIndex() {
        return localIndex;
    }

    //----------------------------------
    // state
    //----------------------------------

    private PadDataState state = PadDataState.IDLE;

    public final PadDataState getState() {
        return state;
    }

    public void setState(PadDataState value) {
        state = value;
    }

    //----------------------------------
    // state
    //----------------------------------

    private Integer viewChannel = null;

    /**
     * Returns the {@link PadChannel} index that is considered the top view.
     */
    public int getViewChannel() {
        if (viewChannel == null) {
            for (PadChannel channel : map.values()) {
                return channel.getIndex();
            }
            viewChannel = 0;
        }
        return viewChannel;
    }

    public void setViewChannel(int viewChannel) {
        this.viewChannel = viewChannel;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public PadData(int index, int bank, int localIndex) {
        this.index = index;
        this.bank = bank;
        this.localIndex = localIndex;
    }

    /**
     * Creates a channel, if one exists, the {@link PadChannel} will be
     * returned.
     * 
     * @param index The index of the channel (0-13) for 14 machines in the core.
     */
    PadChannel createChannel(int toneIndex) {
        PadChannel channel = new PadChannel(toneIndex);
        channel.setPadData(this);
        channel.wakeup(controller);
        map.put(toneIndex, channel);
        return channel;
    }

    /**
     * Returns whether the pad contains a {@link PadChannel} at the tone index.
     * 
     * @param toneIndex The toneIndex to test.
     */
    public boolean hasChannel(int toneIndex) {
        return map.containsKey(toneIndex);
    }

    /**
     * Returns a {@link PadChannel} at the toneIndex, will return
     * <code>null</code> if the channel has not been created.
     * 
     * @param toneIndex The toneIndex to retrieve.
     */
    public PadChannel findChannel(int toneIndex) {
        return map.get(toneIndex);
    }

    /**
     * Returns a {@link PadChannel} at the tone index, this method will create
     * the channel if it does not exist.
     * 
     * @param toneIndex The toneIndex to retrieve.
     */
    public PadChannel getChannel(int toneIndex) {
        PadChannel channel = map.get(toneIndex);
        if (channel == null) {
            channel = createChannel(toneIndex);
        }
        return channel;
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (PadChannel channel : map.values()) {
            channel.setPadData(this);
            channel.wakeup(controller);
        }
    }

    @Override
    public String toString() {
        return "Data[" + bank + "," + localIndex + "]";
    }

    public List<PadChannel> getChannels() {
        return new ArrayList<>(map.values());
    }

    public boolean hasChannels() {
        return map.size() > 0;
    }
}
