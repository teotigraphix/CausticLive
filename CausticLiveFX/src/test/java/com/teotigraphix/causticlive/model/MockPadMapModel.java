
package com.teotigraphix.causticlive.model;

import java.util.List;

import com.teotigraphix.caustk.controller.ICaustkController;

public class MockPadMapModel implements IPadMap {

    private PadMap map;

    public MockPadMapModel(ICaustkController controller, int banks, int patterns) {
        map = new PadMap(controller);
        map.initialize(banks, patterns);
    }

    @Override
    public PadData getPad(int bank, int localIndex) {
        return map.getPad(bank, localIndex);
    }

    @Override
    public List<PadData> getPads(int bank) {
        return map.getPads(bank);
    }

}
