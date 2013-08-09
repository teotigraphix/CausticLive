
package com.teotigraphix.causticlive.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.sound.ISoundSource;
import com.teotigraphix.caustk.tone.BeatboxTone;
import com.teotigraphix.caustk.tone.ModularTone;
import com.teotigraphix.caustk.tone.PCMSynthTone;
import com.teotigraphix.caustk.tone.PadSynthTone;
import com.teotigraphix.caustk.tone.SubSynthTone;
import com.teotigraphix.caustk.tone.Tone;

public class PadDataTest extends CaustkTestBase {

    private ISoundSource soundSource;

    private Tone track0;

    private Tone track3;

    private Tone track5;

    private Tone track8;

    private Tone track13;

    private PadMap map;

    private Library library;

    @Override
    protected void start() throws CausticException, IOException {
        soundSource = controller.getSoundSource();

        track0 = soundSource.createTone(0, "track0", SubSynthTone.class);
        track3 = soundSource.createTone(3, "track3", PCMSynthTone.class);
        track5 = soundSource.createTone(5, "track5", ModularTone.class);
        track8 = soundSource.createTone(8, "track8", BeatboxTone.class);
        track13 = soundSource.createTone(13, "track13", PadSynthTone.class);

        map = new PadMap(controller);
        map.initialize(4, 16);

        File libraryFile = new File("src/test/resources/unit_test/libraries/PadDataTest");
        library = controller.getLibraryManager().loadLibrary(libraryFile);

    }

    @Override
    protected void end() {

    }

    @Test
    public void test_init() {

        PadData pad = map.getPad(3, 4);
        // get the channel for the PadSynth
        PadChannel channel = pad.getChannel(track13.getIndex());
        
        assertSame(track13, channel.getTone());
        assertEquals(3, channel.getBankIndex());
        assertEquals(4, channel.getPatternIndex());
        assertEquals(13, channel.getIndex());

        
        ///--------------------------
        
        channel.setPhrase(library, library.getPhrases().get(0));
        
        String string = controller.getSerializeService().toPrettyString(map);

        PadMap map2 = controller.getSerializeService().fromString(string, PadMap.class);
        String string2 = controller.getSerializeService().toPrettyString(map);
        assertEquals(string, string2);
        
        PadChannel channel2 = map2.getPad(0, 0).getChannel(13);
        
        assertSame(track13, channel2.getTone());
    }
}
