
package com.teotigraphix.causticlive.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.tone.Tone;

@Singleton
public class PadModel extends ModelBase {

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
        getController().getDispatcher().trigger(
                new OnPadModelSelectedBankChange(selectedBank, getPadDataView()));
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

        // assign patches and phrases to pad data
        Library library = getController().getLibraryManager().getSelectedLibrary();
        List<LibraryPatch> patches = library.getPatches();
        List<LibraryPhrase> phrases = library.getPhrases();

        Random rand = new Random();
        int min = 0;
        int max = getController().getSoundSource().getToneCount();
        Collection<Tone> tones = getController().getSoundSource().getTones();
        List<Tone> list = new ArrayList<>(tones);

        // assign tones
        for (PadData data : datas) {
            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNum = rand.nextInt(max - min) + min;
            CtkDebug.err(randomNum + "");
            data.setToneIndex(list.get(randomNum).getIndex());
        }

        rand = new Random();
        min = 0;
        max = phrases.size();

        // assign patches
        for (PadData data : datas) {
            // nextInt is normally exclusive of the top value,
            // so add 1 to make it inclusive
            int randomNum = rand.nextInt(max - min) + min;
            CtkDebug.err(randomNum + "");
            data.setPhraseId(phrases.get(randomNum).getId());
        }

        // init the patterns in the tones
        // assign patches
        for (PadData data : datas) {
            Tone tone = getController().getSoundSource().getTone(data.getToneIndex());
            PatternSequencerComponent sequencer = tone
                    .getComponent(PatternSequencerComponent.class);
            LibraryPhrase phrase = getController().getLibraryManager().getSelectedLibrary()
                    .findPhraseById(data.getPhraseId());
            if(data.getBank() == 3 && data.getLocalIndex() == 15)
                continue;
            
            sequencer.setSelectedPattern(data.getBank(), data.getLocalIndex());
            sequencer.initializeData(phrase.getNoteData());

            trigger(new OnPadModelPadAssign(data));
        }
        
        for (Tone tone : list) {
            PatternSequencerComponent sequencer = tone
                    .getComponent(PatternSequencerComponent.class);
            sequencer.setSelectedPattern(3, 15);
            SynthComponent synthComponent = tone.getComponent(SynthComponent.class);
            
            List<LibraryPatch> patchList = library.findPatchByTag(tone.getName());
            
            String path = library.getPresetFile(patchList.get(0).getPresetFile()).getPath();
            synthComponent.loadPreset(path);
        }
        
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

    public static class PadData {

        private int index;

        private int bank;

        private int localIndex;

        private int toneIndex;

        public final int getToneIndex() {
            return toneIndex;
        }

        public final void setToneIndex(int toneIndex) {
            this.toneIndex = toneIndex;
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
}
