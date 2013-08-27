
package com.teotigraphix.causticlive.model;

import java.util.List;

import com.teotigraphix.causticlive.model.vo.PadData;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface IPadModel extends ICaustkModel {

    void edit(int localIndex);

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

    PadData getLocalData(int index);

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

    //--------------------------------------------------------------------------
    // Enum API
    //--------------------------------------------------------------------------

    public enum PadDataState {
        IDLE,

        SELECTED,

        QUEUED,

        UNQUEUED;
    }

}
