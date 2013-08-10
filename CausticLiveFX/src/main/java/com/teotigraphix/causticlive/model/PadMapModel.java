
package com.teotigraphix.causticlive.model;

import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ICaustkModelState;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.controller.ICaustkController;

@Singleton
public class PadMapModel extends ModelBase implements IPadMapModel {

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
        PadMapModelState s = (PadMapModelState)state;
        s.initialize(getController(), 4, 16);
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

    }

}
