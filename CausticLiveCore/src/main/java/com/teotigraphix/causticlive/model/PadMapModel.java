
package com.teotigraphix.causticlive.model;

import java.util.List;

import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ICaustkModelState;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.controller.ICaustkController;

@Singleton
public class PadMapModel extends ModelBase implements IPadMapModel {

    @Override
    protected PadMapModelState getState() {
        return (PadMapModelState)super.getState();
    }

    public PadMapModel() {
        setStateFactory(PadMapModelState.class);
    }

    @Override
    public void onShow() {
    }

    @Override
    public void onRegister() {
    }

    @Override
    protected void configState(ICaustkModelState state) {
        getState().initialize(getController(), 4, 16);
    }
    
    public PadData getPad(int bank, int localIndex) {
        return getState().padMap.getPad(bank, localIndex);
    }

    public List<PadData> getPads(int bank) {
        return getState().padMap.getPads(bank);
    }

    public static class PadMapModelState implements ICaustkModelState {

        private PadMap padMap;

        PadMap getPadMap() {
            return padMap;
        }

        public void initialize(ICaustkController controller, int banks, int pads) {
            padMap = new PadMap(controller);
            padMap.initialize(banks, pads);
        }

        public PadMapModelState() {

        }

        @Override
        public void sleep() {
        }

        @Override
        public void wakeup(ICaustkController controller) {
            padMap.wakeup(controller);
        }
    }

}
