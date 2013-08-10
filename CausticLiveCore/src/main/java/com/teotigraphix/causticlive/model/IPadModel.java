
package com.teotigraphix.causticlive.model;

import java.util.List;

import com.teotigraphix.caustic.model.ICaustkModel;

public interface IPadModel extends ICaustkModel {

    PadFunction getSelectedFunction();

    /**
     * * @param value
     * 
     * @see OnPadModelSelectedFunctionChange
     */
    void setSelectedFunction(PadFunction value);

    /**
     * @param bank
     * @param localIndex
     * @see OnPadModelSelectedDataChange
     */
    void select(int bank, int localIndex);

    /**
     * @param bank
     * @see OnPadModelSelectedDataChange
     */
    void select(int bank);

    /**
     * @see OnPadModelSelectedDataChange
     * @return
     */
    PadData getSelectedData();

    List<PadData> getPadDataView();

    /**
     * Dispatched the the selected bank has changed in the {@link IPadModel}.
     * <p>
     * This also implicitly says the selected data has changed, but its up to
     * the views to update based on this event.
     * <p>
     * The {@link OnPadModelSelectedDataChange} event is the explicit change of
     * the localIndex, either by user action or programmaticly.
     */
    public static class OnPadModelSelectedBankChange {
        public OnPadModelSelectedBankChange() {
        }
    }

    public static class OnPadModelSelectedDataChange {

        private PadData data;

        private List<PadData> view;

        public final PadData getData() {
            return data;
        }

        public List<PadData> getView() {
            return view;
        }

        public OnPadModelSelectedDataChange(PadData data, List<PadData> view) {
            this.data = data;
            this.view = view;
        }

    }

    public static class OnPadModelSelectedFunctionChange {

        private PadFunction function;

        public PadFunction getFunction() {
            return function;
        }

        public OnPadModelSelectedFunctionChange(PadFunction function) {
            this.function = function;
        }
    }

    //--------------------------------------------------------------------------
    // Enum API
    //--------------------------------------------------------------------------

    public enum PadFunction {

        PATTERN(0),

        ASSIGN(1);

        private int value;

        public final int getValue() {
            return value;
        }

        PadFunction(int value) {
            this.value = value;
        }

        public static PadFunction fromInt(int value) {
            for (PadFunction function : values()) {
                if (function.getValue() == value)
                    return function;
            }
            return null;
        }
    }

    public enum PadDataState {
        IDLE,

        SELECTED,

        QUEUED
    }

}
