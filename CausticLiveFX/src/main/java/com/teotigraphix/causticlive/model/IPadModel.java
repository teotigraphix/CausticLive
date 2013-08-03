
package com.teotigraphix.causticlive.model;

import java.util.List;
import java.util.UUID;

import com.teotigraphix.caustic.model.ICaustkModel;
import com.teotigraphix.causticlive.model.vo.PadData;

public interface IPadModel extends ICaustkModel {

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // selectedBank
    //----------------------------------

    /**
     * @see OnPadModelSelectedBankChange
     * @param value
     */
    void setSelectedBank(int value);

    int getSelectedBank();

    public static class OnPadModelSelectedBankChange {
        private int bank;

        private List<PadData> view;

        public final List<PadData> getView() {
            return view;
        }

        public final int getBank() {
            return bank;
        }

        public OnPadModelSelectedBankChange(int bank, List<PadData> view) {
            this.bank = bank;
            this.view = view;
        }

    }

    //----------------------------------
    // assignmentIndex
    //----------------------------------

    /**
     * Sets the current assignment index when in {@link PadFunction#ASSIGN}
     * mode.
     * 
     * @param value
     */
    void setAssignmentIndex(int value);

    /**
     * The assignment index is used in conjunction with the
     * {@link #getSelectedBank()} index to locate the current {@link PadData} to
     * be edited.
     * 
     * @see #getSelectedBank()
     */
    int getAssignmentIndex();

    /**
     * Returns the current {@link PadData} using the
     * {@link #getAssignmentIndex()} and {@link #getSelectedBank()}.
     */
    PadData getSelectedAssignmentData();

    /**
     * Returns the {@link PadData} in the current view of 16 pads using the
     * local index.
     * 
     * @param localIndex The local index 0-15 to retrieve data based on the
     *            current bank.
     */
    PadData getAssignmentDataAt(int localIndex);

    //----------------------------------
    // padDataView
    //----------------------------------

    List<PadData> getPadDataView();

    //----------------------------------
    // selectedFunction
    //----------------------------------

    PadFunction getSelectedFunction();

    /**
     * @see OnPadModelSelectedFunctionChange
     * @param value
     */
    void setSelectedFunction(PadFunction value);

    //--------------------------------------------------------------------------
    // Method API
    //--------------------------------------------------------------------------

    /**
     * Selects a {@link PadData} based on the current bank.
     * 
     * @param index
     */
    void select(int index, boolean selected);

    /**
     * @see OnPadModelPadDataRefresh
     * @param index
     */
    void setAssignmentToneIndex(int index);

    void setAssignmentPhraseId(UUID id);

    //--------------------------------------------------------------------------
    // Event API
    //--------------------------------------------------------------------------

    public static class OnPadModelPadDataDeselect {

        private PadData data;

        public PadData getData() {
            return data;
        }

        public OnPadModelPadDataDeselect(PadData data) {
            this.data = data;
        }

    }

    public static class OnPadModelPadDataRefresh {
    }

    public static class OnPadModelSelectedFunctionChange {
    }

    //--------------------------------------------------------------------------
    // Enum API
    //--------------------------------------------------------------------------

    public enum PadFunction {
        PATTERN(0), ASSIGN(1);

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
        IDLE, SELECTED, QUEUED
    }

}
