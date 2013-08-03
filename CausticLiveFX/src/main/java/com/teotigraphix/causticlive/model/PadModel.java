
package com.teotigraphix.causticlive.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.tone.Tone;

@Singleton
public class PadModel extends ModelBase implements IPadModel {

    @Inject
    ISoundModel soundModel;

    //----------------------------------
    // selectedFunction
    //----------------------------------

    private PadFunction selectedFunction;

    @Override
    public PadFunction getSelectedFunction() {
        return selectedFunction;
    }

    @Override
    public void setSelectedFunction(PadFunction value) {
        if (value == selectedFunction)
            return;
        selectedFunction = value;
        trigger(new OnPadModelSelectedFunctionChange());
    }

    //----------------------------------
    // assignmentIndex
    //----------------------------------

    private int assignmentIndex;

    @Override
    public void setAssignmentIndex(int value) {
        if (value == assignmentIndex)
            return;
        assignmentIndex = value;
        trigger(new OnPadModelAssignmentIndexChange());
    }

    @Override
    public int getAssignmentIndex() {
        return assignmentIndex;
    }

    @Override
    public PadData getAssignmentData() {
        int localIndex = getAssignmentIndex();
        PadData data = getPadDataView().get(localIndex);
        return data;
    }

    public static class OnPadModelAssignmentIndexChange {

    }

    //----------------------------------
    // selectedBank
    //----------------------------------

    private int selectedBank = -1;

    @Override
    public final int getSelectedBank() {
        return selectedBank;
    }

    @Override
    public final void setSelectedBank(int value) {
        if (value == selectedBank)
            return;
        CtkDebug.model("Set selectedBank " + value);
        selectedBank = value;
        trigger(new OnPadModelSelectedBankChange(selectedBank, getPadDataView()));
    }

    public PadModel() {
        super();
    }

    @Override
    public void select(int index, boolean selected) {
        CtkDebug.model("Select Pad " + index + ":" + selected);
        List<PadData> list = getPadDataView();
        PadData data = list.get(index);
        Tone tone = PadModelUtils.getTone(getController(), data);
        PatternSequencerComponent component = tone.getComponent(PatternSequencerComponent.class);
        if (selected) {
            data.state = PadDataState.SELECTED;
            component.setSelectedPattern(data.getBank(), data.getLocalIndex());
        } else {
            data.state = PadDataState.IDLE;
            component.setSelectedPattern(3, 15);
        }
        // this call always comes from a view so the UI is already updated.

        // need to create a way that updates the ui based on a load etc.
    }

    @Override
    public void onRegister() {
        createPadData();
    }

    @Override
    public void onShow() {

        setSelectedFunction(PadFunction.PATTERN);
        setSelectedBank(0);

        getController().getSystemSequencer().play(SequencerMode.PATTERN);
    }

    /**
     * Returns the {@link PadData} instances that are in the selected bank.
     */
    @Override
    public List<PadData> getPadDataView() {
        List<PadData> result = new ArrayList<PadData>();
        for (PadData data : datas) {
            if (data.bank == selectedBank)
                result.add(data);
        }
        return result;
    }

    private List<PadData> datas = new ArrayList<PadData>();

    private void createPadData() {
        int index = 0;
        for (int bank = 0; bank < 4; bank++) {
            int localIndex = 0;
            for (int row = 0; row < 4; row++) {
                for (int column = 0; column < 4; column++) {
                    PadData data = new PadData(index, bank, localIndex);
                    datas.add(data);
                    index++;
                    localIndex++;
                }
            }
        }
        index = 0;
    }

    /**
     * @see ModelBase#getDispatcher()
     */
    public static class OnPadModelPadAssign {

        private PadData data;

        public OnPadModelPadAssign(PadData data) {
            this.data = data;
        }

        public final PadData getData() {
            return data;
        }
    }

    public class PadData {

        private int index;

        private int bank;

        private int localIndex;

        private int toneIndex = -1;

        public final int getToneIndex() {
            return toneIndex;
        }

        public final void setToneIndex(int toneIndex) {
            this.toneIndex = toneIndex;
            PadModel.this.trigger(new OnPadModelPadDataRefresh());
        }

        public final int getIndex() {
            return index;
        }

        private UUID patchId;

        private UUID phraseId;

        public final int getBank() {
            return bank;
        }

        public final int getLocalIndex() {
            return localIndex;
        }

        public final PadDataState getState() {
            return state;
        }

        private PadDataState state = PadDataState.IDLE;

        public PadData(int index, int bank, int localIndex) {
            this.index = index;
            this.bank = bank;
            this.localIndex = localIndex;
        }

        @Override
        public String toString() {
            return index + " " + bank + " " + localIndex;
        }

        public UUID getPhraseId() {
            return phraseId;
        }

        public void setPhraseId(UUID phraseId) {
            this.phraseId = phraseId;
            updatePhrase(this, phraseId);
        }

        public UUID getPatchId() {
            return patchId;
        }

        public void setPatchId(UUID patchId) {
            this.patchId = patchId;
        }
    }

    public void updatePhrase(PadData data, UUID phraseId) {
        try {
            PadModelUtils.updatePhrase(getController(), data, phraseId);
        } catch (CausticException e) {
            e.printStackTrace();
        }
        trigger(new OnPadModelPadDataRefresh());
    }

}
