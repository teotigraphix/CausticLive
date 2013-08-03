
package com.teotigraphix.causticlive.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.tone.Tone;

@Singleton
public class PadModel extends ModelBase {

    @Inject
    SoundModel soundModel;

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

    //----------------------------------
    // selectedFunction
    //----------------------------------

    private PadFunction selectedFunction;

    public PadFunction getSelectedFunction() {
        return selectedFunction;
    }

    /**
     * @see OnPadModelSelectedFunctionChange
     * @param value
     */
    public void setSelectedFunction(PadFunction value) {
        if (value == selectedFunction)
            return;
        selectedFunction = value;
        trigger(new OnPadModelSelectedFunctionChange());
    }

    public static class OnPadModelSelectedFunctionChange {

    }

    //----------------------------------
    // assignmentIndex
    //----------------------------------

    private int assignmentIndex;

    /**
     * Sets the current assignment index when in {@link PadFunction#ASSIGN}
     * mode.
     * 
     * @param value
     */
    public void setAssignmentIndex(int value) {
        if (value == assignmentIndex)
            return;
        assignmentIndex = value;
        trigger(new OnPadModelAssignmentIndexChange());
    }

    /**
     * The assignment index is used in conjunction with the
     * {@link #getSelectedBank()} index to locate the current {@link PadData} to
     * be edited.
     * 
     * @see #getSelectedBank()
     */
    public int getAssignmentIndex() {
        return assignmentIndex;
    }

    /**
     * Returns the current {@link PadData} using the
     * {@link #getAssignmentIndex()} and {@link #getSelectedBank()}.
     */
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

    public final int getSelectedBank() {
        return selectedBank;
    }

    /**
     * @see OnPadModelSelectedBankChange
     * @param value
     */
    public final void setSelectedBank(int value) {
        if (value == selectedBank)
            return;
        CtkDebug.model("Set selectedBank " + value);
        selectedBank = value;
        trigger(new OnPadModelSelectedBankChange(selectedBank, getPadDataView()));
    }

    @Inject
    public PadModel(ICaustkApplicationProvider provider) {
        super(provider);
    }

    /**
     * Selects a {@link PadData} based on the current bank.
     * 
     * @param index
     */
    public void select(int index, boolean selected) {
        CtkDebug.model("Select Pad " + index + ":" + selected);
        List<PadData> list = getPadDataView();
        PadData data = list.get(index);
        Tone tone = getTone(data);
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

    private Tone getTone(PadData data) {
        return getController().getSoundSource().getTone(data.getToneIndex());
    }

    @Override
    public void onRegister() {
        super.onRegister();

        createPadData();
    }

    @Override
    protected void onShow() {
        super.onShow();

        setSelectedFunction(PadFunction.PATTERN);
        setSelectedBank(0);

        //        // assign patches and phrases to pad data
        //        Library library = getController().getLibraryManager().getSelectedLibrary();
        //        List<LibraryPatch> patches = library.getPatches();
        //        List<LibraryPhrase> phrases = library.getPhrases();
        //
        //        Random rand = new Random();
        //        int min = 0;
        //        int max = getController().getSoundSource().getToneCount();
        //        Collection<Tone> tones = getController().getSoundSource().getTones();
        //        List<Tone> list = new ArrayList<>(tones);
        //
        //        // assign tones
        //        for (PadData data : datas) {
        //            // nextInt is normally exclusive of the top value,
        //            // so add 1 to make it inclusive
        //            int randomNum = rand.nextInt(max - min) + min;
        //            CtkDebug.err(randomNum + "");
        //            data.setToneIndex(list.get(randomNum).getIndex());
        //        }
        //
        //        rand = new Random();
        //        min = 0;
        //        max = phrases.size();
        //
        //        // assign patches
        //        for (PadData data : datas) {
        //            // nextInt is normally exclusive of the top value,
        //            // so add 1 to make it inclusive
        //            int randomNum = rand.nextInt(max - min) + min;
        //            CtkDebug.err(randomNum + "");
        //            data.setPhraseId(phrases.get(randomNum).getId());
        //        }
        //
        //        // init the patterns in the tones
        //        // assign patches
        //        for (PadData data : datas) {
        //            Tone tone = getController().getSoundSource().getTone(data.getToneIndex());
        //            PatternSequencerComponent sequencer = tone
        //                    .getComponent(PatternSequencerComponent.class);
        //            LibraryPhrase phrase = getController().getLibraryManager().getSelectedLibrary()
        //                    .findPhraseById(data.getPhraseId());
        //            if(data.getBank() == 3 && data.getLocalIndex() == 15)
        //                continue;
        //            
        //            sequencer.setSelectedPattern(data.getBank(), data.getLocalIndex());
        //            sequencer.initializeData(phrase.getNoteData());
        //
        //            trigger(new OnPadModelPadAssign(data));
        //        }
        //        
        //        for (Tone tone : list) {
        //            PatternSequencerComponent sequencer = tone
        //                    .getComponent(PatternSequencerComponent.class);
        //            sequencer.setSelectedPattern(3, 15);
        //            SynthComponent synthComponent = tone.getComponent(SynthComponent.class);
        //            
        //            List<LibraryPatch> patchList = library.findPatchByTag(tone.getName());
        //            
        //            String path = library.getPresetFile(patchList.get(0).getPresetFile()).getPath();
        //            synthComponent.loadPreset(path);
        //        }
        //        
        getController().getSystemSequencer().play(SequencerMode.PATTERN);
    }

    /**
     * Returns the {@link PadData} instances that are in the selected bank.
     */
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

    public enum PadDataState {
        IDLE, SELECTED, QUEUED
    }

    public static class OnPadModelPadDataRefresh {

    }

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

    public void updatePhrase(PadData data, UUID phraseId) {
        Library library = soundModel.getLibrary();
        LibraryPhrase phrase = library.findPhraseById(phraseId);

        phrase.getToneType();

        Tone tone = getController().getSoundSource().getTone(data.getToneIndex());
        if (tone == null)
            return; // XXX HACK
        PatternSequencerComponent sequencer = tone.getComponent(PatternSequencerComponent.class);
        if (data.getBank() == 3 && data.getLocalIndex() == 15)
            return;

        sequencer.setSelectedPattern(data.getBank(), data.getLocalIndex());
        sequencer.clearIndex(data.getLocalIndex());
        sequencer.setLength(phrase.getLength());
        sequencer.initializeData(phrase.getNoteData());

        trigger(new OnPadModelPadDataRefresh());
    }

}
