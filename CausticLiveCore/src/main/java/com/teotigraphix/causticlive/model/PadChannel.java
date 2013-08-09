
package com.teotigraphix.causticlive.model;

import java.util.UUID;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.ChannelPhrase;
import com.teotigraphix.caustk.service.ISerialize;
import com.teotigraphix.caustk.tone.Tone;

/*

14 machines

14 * 4 * 16 = 896 possible patterns

A channel is basically a concrete pattern_sequencer location within a machine.

64 possible pattern locations in a machine, we have 64 pads
- each PadData bank & localIndex DIRECTLY corresponds to the machines pattern bank

- A channel is assigned a phraseId

Channel;
 - tone index
 - sequencer bank index
 - sequencer pattern index

*/

public class PadChannel implements ISerialize {

    private transient ICaustkController controller;

    //----------------------------------
    // padData
    //----------------------------------

    private transient PadData padData;

    /**
     * Returns the owner.
     */
    public PadData getPadData() {
        return padData;
    }

    public void setPadData(PadData padData) {
        this.padData = padData;
    }

    //----------------------------------
    // bankIndex
    //----------------------------------

    /**
     * Returns the pattern sequencer bank index this channel is assigned to.
     */
    public final int getBankIndex() {
        return padData.getBank();
    }

    //----------------------------------
    // patternIndex
    //----------------------------------

    /**
     * Returns the pattern sequencer pattern index this channel is assigned to.
     */
    public final int getPatternIndex() {
        return padData.getLocalIndex();
    }

    //----------------------------------
    // enabled
    //----------------------------------

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private int index;

    /**
     * Returns the index of the channel within it's {@link PadData} stack.
     * <p>
     * This index represents the tone index assigned to the channel. Which is a
     * machine in the rack.
     */
    public int getIndex() {
        return index;
    }

    //----------------------------------
    // libraryId
    //----------------------------------

    private UUID ownerLibraryId;

    public UUID getOwnerLibraryId() {
        return ownerLibraryId;
    }

    //----------------------------------
    // phraseId
    //----------------------------------

    private UUID ownerPhraseId;

    public UUID getOwnerPhraseId() {
        return ownerPhraseId;
    }

    private ChannelPhrase channelPhrase;

    public ChannelPhrase getChannelPhrase() {
        return channelPhrase;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    /**
     * @param index The tone index.
     */
    public PadChannel(int index) {
        this.index = index;
    }

    @Override
    public void sleep() {
    }

    @Override
    public void wakeup(ICaustkController controller) {
        this.controller = controller;

    }

    public Tone getTone() {
        return controller.getSoundSource().getTone(index);
    }

    public void assignPhrase(LibraryPhrase phrase) {
        if (phrase == null) {
            clearPhrase();
            return;
        }
        // save original the libraryId and phraseId if ever this
        // channel wants to try and reset its phrase data
        // but we copy the whole thing so there is no dependencies
        // between this project and library since they could get disconnected
        ownerLibraryId = phrase.getLibrary().getId();
        ownerPhraseId = phrase.getId();
        LibraryPhrase newPhrase = controller.getSerializeService()
                .copy(phrase, LibraryPhrase.class);
        
        // update the decoration to hold our position
        // the original ID from the library can always get the original bank/pattern locations
        newPhrase.setId(UUID.randomUUID());
        newPhrase.setBankIndex(getBankIndex());
        newPhrase.setPatternIndex(getPatternIndex());
        
        channelPhrase = new ChannelPhrase(newPhrase);
        
        String data = channelPhrase.getNoteData();
        int oldBank = getTone().getPatternSequencer().getSelectedBank();
        int oldPattern = getTone().getPatternSequencer().getSelectedIndex();
        getTone().getPatternSequencer().setSelectedPattern(getBankIndex(), getPatternIndex());
        getTone().getPatternSequencer().initializeData(data);
        getTone().getPatternSequencer().setSelectedPattern(oldBank, oldPattern);
    }

    private void clearPhrase() {
        ownerLibraryId = null;
        ownerPhraseId = null;
        if (channelPhrase != null) {
            // remove note data from machines pattern sequencer
            getTone().getPatternSequencer().clearIndex(getBankIndex(), getPatternIndex());
        }
        channelPhrase = null;
    }

}
