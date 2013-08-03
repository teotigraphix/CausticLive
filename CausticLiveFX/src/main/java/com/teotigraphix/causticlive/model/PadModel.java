
package com.teotigraphix.causticlive.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.causticlive.model.vo.PadData;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
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

    private int assignmentIndex = -1;

    @Override
    public void setAssignmentIndex(int value) {
        if (value == assignmentIndex)
            return;
        assignmentIndex = value;
        trigger(new OnPadModelAssignmentIndexChange(assignmentIndex));
    }

    @Override
    public int getAssignmentIndex() {
        return assignmentIndex;
    }

    @Override
    public final PadData getSelectedAssignmentData() {
        int localIndex = getAssignmentIndex();
        PadData data = getAssignmentDataAt(localIndex);
        return data;
    }

    @Override
    public final PadData getAssignmentDataAt(int localIndex) {
        return getPadDataView().get(localIndex);
    }

    public static class OnPadModelAssignmentIndexChange {

        private int index;

        public int getIndex() {
            return index;
        }

        public OnPadModelAssignmentIndexChange(int index) {
            this.index = index;
        }

        public boolean isNoSelection() {
            return index == -1;
        }

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
        if (tone == null) {
            // the pattern phrase has not been assigned to the button
            // XXX the buttons should be disabled, this shouldn't happen
            return;
        }

        if (selected)
            deselectOthersOfSameTone(data.getToneIndex(), data);

        PatternSequencerComponent component = tone.getComponent(PatternSequencerComponent.class);
        if (selected) {
            data.setState(PadDataState.SELECTED);
            component.setSelectedPattern(data.getBank(), data.getLocalIndex());
        } else {
            data.setState(PadDataState.IDLE);
            component.setSelectedPattern(3, 15);
        }
        // this call always comes from a view so the UI is already updated.

        // need to create a way that updates the ui based on a load etc.
    }

    private void deselectOthersOfSameTone(int toneIndex, PadData exclude) {
        for (PadData data : datas) {
            if (data == exclude)
                continue;

            if (data.getToneIndex() == toneIndex)
                deselect(data);
        }
    }

    private void deselect(PadData data) {
        data.setState(PadDataState.IDLE);
        trigger(new OnPadModelPadDataDeselect(data));
        //Tone tone = PadModelUtils.getTone(getController(), data);
        //PatternSequencerComponent component = tone.getComponent(PatternSequencerComponent.class);
        //component.setSelectedPattern(3, 15);
    }

    @Override
    public void onRegister() {
        createPadData();
    }

    @Override
    public void onShow() {

        setSelectedFunction(PadFunction.PATTERN);
        setSelectedBank(0);

        //getController().getSystemSequencer().play(SequencerMode.PATTERN);
    }

    /**
     * Returns the {@link PadData} instances that are in the selected bank.
     */
    @Override
    public List<PadData> getPadDataView() {
        List<PadData> result = new ArrayList<PadData>();
        for (PadData data : datas) {
            if (data.getBank() == selectedBank)
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

    public void updatePhrase(PadData data, UUID phraseId) {
        try {
            PadModelUtils.updatePhrase(getController(), data, phraseId);
        } catch (CausticException e) {
            e.printStackTrace();
        }
        trigger(new OnPadModelPadDataRefresh());
    }

    @Override
    public void setAssignmentToneIndex(int index) {
        // use selected data
        PadData data = getSelectedAssignmentData();
        data.setToneIndex(index);
        trigger(new OnPadModelPadDataRefresh());
    }

    @Override
    public void setAssignmentPhraseId(UUID id) {
        PadData data = getSelectedAssignmentData();

        try {
            PadModelUtils.updatePhrase(getController(), data, id);
        } catch (CausticException e) {
            e.printStackTrace();
        }

        trigger(new OnPadModelPadDataRefresh());
    }

}
