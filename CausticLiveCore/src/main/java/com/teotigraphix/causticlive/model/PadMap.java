
package com.teotigraphix.causticlive.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;

/*

The PadMap holds all banks and patterns of the pad view.

The PadData is the sate of the IPadModel that gets serialized.



*/

public class PadMap implements ISerialize {

    private Map<Integer, PadData> map = new TreeMap<Integer, PadData>();

    private transient ICaustkController controller;

    public PadMap(ICaustkController controller) {
        this.controller = controller;
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;
        for (PadData data : map.values()) {
            data.wakeup(controller);
        }
    }

    /**
     * Initializes the pad map with all banks and pads.
     * <p>
     * This method is not called when deserializing, since the data already
     * exists.
     * <p>
     * Only needed when creating new songs or projects.
     * 
     * @param banks The number of bank buttons.
     * @param pads The number of pad buttons.
     */
    public void initialize(int banks, int pads) {
        int index = 0;
        for (int bank = 0; bank < banks; bank++) {
            for (int localIndex = 0; localIndex < pads; localIndex++) {
                PadData data = new PadData(index, bank, localIndex);
                data.wakeup(controller);
                map.put(index, data);
                index++;
            }
        }
    }

    /**
     * Creates a channel, if one exists, the {@link PadChannel} will be
     * returned.
     * 
     * @param bank
     * @param localIndex
     * @param toneIndex The index of the channel (0-13) for 14 machines in the
     *            core.
     */
    public PadChannel createChannel(int bank, int localIndex, int toneIndex) {
        PadData pad = getPad(bank, localIndex);
        PadChannel channel = pad.createChannel(toneIndex);
        return channel;
    }

    /**
     * Returns the {@link PadData} at the index within the whole model.
     * 
     * @param index The full pad index bank * localIndex.
     */
    public PadData getPad(int index) {
        return map.get(index);
    }

    /**
     * Returns the {@link PadData} at the localIndex within a bank.
     * 
     * @param bank The bank index.
     * @param localIndex The local index.
     */
    public PadData getPad(int bank, int localIndex) {
        for (PadData data : map.values()) {
            if (data.getBank() == bank && data.getLocalIndex() == localIndex)
                return data;
        }
        return null;
    }

    public List<PadData> getPads(int bank) {
        List<PadData> result = new ArrayList<PadData>();
        for (PadData data : map.values()) {
            if (data.getBank() == bank)
                result.add(data);
        }
        return result;
    }

    public Collection<PadData> getPads() {
        return map.values();
    }
}
