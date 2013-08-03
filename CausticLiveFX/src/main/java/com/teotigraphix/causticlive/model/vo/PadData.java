
package com.teotigraphix.causticlive.model.vo;

import java.util.UUID;

import com.teotigraphix.causticlive.model.IPadModel.PadDataState;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.service.ISerialize;

public class PadData implements ISerialize {

    //----------------------------------
    // index
    //----------------------------------

    private int index;

    public final int getIndex() {
        return index;
    }

    //----------------------------------
    // bank
    //----------------------------------

    private int bank;

    public final int getBank() {
        return bank;
    }

    //----------------------------------
    // localIndex
    //----------------------------------

    private int localIndex;

    public final int getLocalIndex() {
        return localIndex;
    }

    //----------------------------------
    // toneIndex
    //----------------------------------

    private int toneIndex = -1;

    public final int getToneIndex() {
        return toneIndex;
    }

    public final void setToneIndex(int value) {
        toneIndex = value;
    }

    //----------------------------------
    // patchId
    //----------------------------------

    private UUID patchId;

    public UUID getPatchId() {
        return patchId;
    }

    public void setPatchId(UUID value) {
        patchId = value;
    }

    //----------------------------------
    // phraseId
    //----------------------------------

    private UUID phraseId;

    public UUID getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(UUID value) {
        phraseId = value;
    }

    //----------------------------------
    // state
    //----------------------------------

    private PadDataState state = PadDataState.IDLE;

    public final PadDataState getState() {
        return state;
    }

    public void setState(PadDataState value) {
        state = value;
    }

    public PadData(int index, int bank, int localIndex) {
        this.index = index;
        this.bank = bank;
        this.localIndex = localIndex;
    }

    @Override
    public String toString() {
        return index + " " + bank + " " + localIndex;
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
    }

    public boolean isEnabled() {
        return toneIndex != -1 && phraseId != null;
    }
}
