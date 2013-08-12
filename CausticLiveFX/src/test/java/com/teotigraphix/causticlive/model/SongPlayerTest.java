
package com.teotigraphix.causticlive.model;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.teotigraphix.causticlive.model.IPadModel.PadDataState;
import com.teotigraphix.caustk.CaustkTestBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.RackMessage;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.sequencer.TrackSong;
import com.teotigraphix.caustk.utils.RuntimeUtils;

public class SongPlayerTest extends CaustkTestBase {

    private SongPlayer player;

    private TrackSong trackSong;

    private MockPadMapModel model;

    private PadData A01;

    private PadData A02;

    private PadData B04;

    private PadData D12;

    private Library library;

    @Override
    protected void start() throws CausticException, IOException {

        File libraryFile = new File("src/test/resources/libraries/PadDataTest");
        library = controller.getLibraryManager().loadLibrary(libraryFile);

        controller.getSoundSource().createScene(library.getScenes().get(1));

        model = new MockPadMapModel(controller, 4, 16);

        trackSong = controller.getSongManager().create(new File("SongPlayerTestSong.ctks"));
        trackSong.setNumTracks(14);

        player = new SongPlayer(controller);
        player.setPadMap(model);
        player.setSong(trackSong);

        A01 = player.getPadMap().getPad(0, 0);
        A01.getChannel(0).setLoopEnabled(false);
        A01.getChannel(0).assignPhrase(library.getPhrases().get(0));

        A02 = player.getPadMap().getPad(0, 1);
        A02.getChannel(1).assignPhrase(library.findPhrasesByTag("length-2").get(0));

        B04 = player.getPadMap().getPad(1, 3);
        B04.getChannel(2).assignPhrase(library.getPhrases().get(2));

        D12 = player.getPadMap().getPad(3, 11);
        D12.getChannel(3).assignPhrase(library.getPhrases().get(3));
    }

    @Override
    protected void end() {
        player = null;
    }

    @Test
    public void test_init() throws CausticException {
        // we have to do this so the nextBeat() puts the beat at 0
        player.play();

        int numMeasures = 120;
        int beat = 0;
        for (int i = 0; i < numMeasures; i++) {
            for (int j = 0; j < 4; j++) {
                playAssert(i, beat);
                beat++;
            }
        }
    }

    // there was a bug where turning
    @Test
    public void test_queue_double_loop_on_off_on() throws CausticException {
        //int len1 = A01.getChannel(0).getChannelPhrase().getLength();
        //int len2 = A02.getChannel(1).getChannelPhrase().getLength();
        player.queue(A02); // length 1
        player.queue(B04); // length 2
        player.play();
        nextMeasure(1);
        assertQueuedSize(0);
        assertPlayQueueSize(2);
        nextMeasure(2);
        nextMeasure(3); // In the middle of A02
        player.unqueue(A02);
        assertQueuedSize(0);
        assertPlayQueueSize(2);
        nextMeasure(4); // A02 removed
        assertQueuedSize(0);
        //assertPlayQueueSize(1);
        
        // XXX THIS IS THE BUG, adding A02 back off it's beat
        player.queue(A02);
        nextMeasure(5);
        // instead of waiting for another measure since it's length is 2, the palyer inserts
        // a full two measure phrase in the middle of the existing, thus exception is thrown
        nextMeasure(6);
        nextMeasure(7);
        nextMeasure(8);
        RackMessage.SAVE_SONG.send(controller, "TestSeq");
    }

    @Test
    public void test_queue_single_loop_on_off() throws CausticException {
        A01.getChannel(0).setLoopEnabled(true);
        player.queue(A01);
        player.play(); // /caustic/sequencer/pattern_event 1 0 0 1 1
        nextMeasure(1); // /caustic/sequencer/pattern_event 1 1 0 1 2
        assertPosition(0, 3);

        assertQueuedSize(0);
        assertPlayQueueSize(1);
        player.unqueue(A01); // just dosn't add the pattern
        nextMeasure(2);
        assertPosition(1, 7);

        assertQueuedSize(0);
        assertPlayQueueSize(0);

        // put it back on
        player.queue(A01); // Queue:Data[0,1]
        assertQueuedSize(1);
        assertPlayQueueSize(0);
        nextMeasure(3); // /caustic/sequencer/pattern_event 1 2 0 1 3
        assertPosition(2, 11);

        assertQueuedSize(0);
        assertPlayQueueSize(1);
    }

    @Test
    public void test_queue_single_loop() throws CausticException {
        player.queue(A02);
        assertQueuedSize(1);
        assertPlayQueueSize(0);
        player.play();
        assertQueuedSize(0);
        assertPlayQueueSize(1);
        assertEquals(PadDataState.SELECTED, A02.getState());
        nextMeasure(1);
        // The channel loops, still in play queue
        assertQueuedSize(0);
        assertPlayQueueSize(1);
        assertEquals(PadDataState.SELECTED, A02.getState());
        nextMeasure(2); // /caustic/sequencer/pattern_event 1 2 0 1 3
        nextMeasure(3); // /caustic/sequencer/pattern_event 1 3 0 1 4
        nextMeasure(4); // /caustic/sequencer/pattern_event 1 4 0 1 5
        nextMeasure(5); // /caustic/sequencer/pattern_event 1 5 0 1 6
        assertQueuedSize(0);
        assertPlayQueueSize(1);
        assertEquals(PadDataState.SELECTED, A02.getState());
    }

    /**
     * @param measure The exact measure in the Caustic sequencer,
     */
    private void nextMeasure(int measure) {
        for (int i = 0; i < 4; i++) {
            player.beatChange(measure - 1, i);
        }
    }

    @Test
    public void test_queue_single_oneshot() throws CausticException {
        assertQueuedSize(0);
        assertPlayQueueSize(0);

        // we are not playing yet
        player.queue(A01);

        assertQueuedSize(1);
        assertPlayQueueSize(0);

        player.play();

        assertQueuedSize(0);
        assertPlayQueueSize(1);
        assertEquals(PadDataState.SELECTED, A01.getState());

        // run to the next measure
        nextMeasure(1);
        assertEquals(3, trackSong.getCurrentBeat());

        // The channel does not loop, one shot, so it was taken out
        assertQueuedSize(0);
        assertPlayQueueSize(0);
        assertEquals(PadDataState.IDLE, A01.getState());
    }

    private void assertQueuedSize(int size) {
        assertEquals(size, player.getQueued().size());
    }

    private void assertPlayQueueSize(int size) {
        assertEquals(size, player.getPlayQueue().size());
    }

    private void assertPosition(int measure, int beat) {
        assertEquals(beat, trackSong.getCurrentBeat());
        assertEquals(measure, trackSong.getCurrentMeasure());
    }

    private void playAssert(int measure, int beat) {
        player.beatChange(measure, beat);
        assertEquals(beat, trackSong.getCurrentBeat());
        assertEquals(measure, trackSong.getCurrentMeasure());
    }
}
