
package com.teotigraphix.causticlive.model;

import java.util.UUID;

import com.teotigraphix.causticlive.model.PadModel.PadData;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.components.PatternSequencerComponent;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.tone.Tone;

public class PadModelUtils {

    public static Tone getTone(ICaustkController controller, PadData data) {
        return controller.getSoundSource().getTone(data.getToneIndex());
    }

    public static void updatePhrase(ICaustkController controller, PadData data, UUID phraseId)
            throws CausticException {
        Library library = controller.getLibraryManager().getSelectedLibrary();
        LibraryPhrase phrase = library.findPhraseById(phraseId);

        Tone tone = controller.getSoundSource().getTone(data.getToneIndex());
        if (tone == null)
            return; // XXX HACK

        PatternSequencerComponent sequencer = tone.getComponent(PatternSequencerComponent.class);
        if (data.getBank() == 3 && data.getLocalIndex() == 15)
            return;

        sequencer.setSelectedPattern(data.getBank(), data.getLocalIndex());
        sequencer.clearIndex(data.getLocalIndex());
        sequencer.setLength(phrase.getLength());
        sequencer.initializeData(phrase.getNoteData());
    }
}
