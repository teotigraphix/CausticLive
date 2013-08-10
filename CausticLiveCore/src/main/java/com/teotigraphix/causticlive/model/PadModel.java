
package com.teotigraphix.causticlive.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ICaustkModelState;
import com.teotigraphix.caustic.model.ModelBase;

@Singleton
public class PadModel extends ModelBase implements IPadModel {

    @Inject
    IPadMapModel padMapModel;

    @Override
    protected PadModelState getState() {
        return (PadModelState)super.getState();
    }

    @Override
    public PadData getSelectedData() {
        return padMapModel.getPad(getState().getSelectedBank(), getState().getSelectedIndex());
    }

    /**
     * Returns the {@link PadData} instances that are in the selected bank.
     */
    @Override
    public List<PadData> getPadDataView() {
        return padMapModel.getPads(getState().getSelectedBank());
    }

    public PadFunction getSelectedFunction() {
        return getState().getSelectedFunction();
    }

    public void setSelectedFunction(PadFunction value) {
        getState().setSelectedFunction(value);
        trigger(new OnPadModelSelectedFunctionChange(value));
    }

    public PadModel() {
        setStateFactory(PadModelState.class);
    }

    @Override
    protected void configState(ICaustkModelState state) {
        getState().selectedIndex.put(0, 0);
        getState().selectedIndex.put(1, 0);
        getState().selectedIndex.put(2, 0);
        getState().selectedIndex.put(3, 0);
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow() {
        setSelectedFunction(getSelectedFunction());
        select(getState().getSelectedBank(), getState().getSelectedIndex());
    }

    @Override
    public void select(int bank) {
        getState().setSelectedBank(bank);
        //getDispatcher().trigger(
        //        new OnPadModelSelectedDataChange(getSelectedData(), getPadDataView()));
        trigger(new OnPadModelSelectedBankChange());
    }

    @Override
    public void select(int bank, int localIndex) {
        getState().setSelectedBank(bank);
        getState().setSelectedIndex(localIndex);
        getDispatcher().trigger(
                new OnPadModelSelectedDataChange(getSelectedData(), getPadDataView()));
    }

    public static class PadModelState implements ICaustkModelState {

        int selectedBank = 0;

        public int getSelectedBank() {
            return selectedBank;
        }

        public void setSelectedBank(int value) {
            selectedBank = value;
        }

        Map<Integer, Integer> selectedIndex = new HashMap<Integer, Integer>();

        public int getSelectedIndex() {
            return selectedIndex.get(selectedBank);
        }

        public void setSelectedIndex(int value) {
            selectedIndex.put(selectedBank, value);
        }

        private PadFunction selectedFunction = PadFunction.PATTERN;

        public PadFunction getSelectedFunction() {
            return selectedFunction;
        }

        public void setSelectedFunction(PadFunction value) {
            selectedFunction = value;
        }

        public PadModelState() {
        }

    }

}
