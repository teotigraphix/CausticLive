
package com.teotigraphix.causticlive.model;

import java.util.ArrayList;
import java.util.List;

import com.teotigraphix.causticlive.model.IPadModel.PadDataState;
import com.teotigraphix.causticlive.model.vo.PadData;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.sequencer.ChannelPhrase;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.sequencer.Track;
import com.teotigraphix.caustk.sequencer.TrackItem;
import com.teotigraphix.caustk.sequencer.TrackSong;
import com.teotigraphix.caustk.tone.Tone;

public class PhrasePlayer {

    private List<PadData> flushedQueue = new ArrayList<PadData>();

    private List<PadData> playQueue = new ArrayList<PadData>();

    List<PadData> getPlayQueue() {
        return playQueue;
    }

    private List<PadData> queued = new ArrayList<PadData>();

    List<PadData> getQueued() {
        return queued;
    }

    private TrackSong song;

    private ICaustkController controller;

    private IPadMap padMap;

    private int currentLocalBeat;

    public IPadMap getPadMap() {
        return padMap;
    }

    public void setPadMap(IPadMap value) {
        padMap = value;
    }

    public TrackSong getSong() {
        return song;
    }

    public void setSong(TrackSong value) {
        song = value;
    }

    public PhrasePlayer(ICaustkController controller) {
        this.controller = controller;
    }

    public void beatChange(float beat) {
        getSong().nextBeat();

        CtkDebug.model(">> Beat:" + beat);

        currentLocalBeat = (int)(beat % 4);

        // start new measure
        if (currentLocalBeat == 0) {
            for (PadData data : flushedQueue) {
                if (data.getState() != PadDataState.QUEUED) {
                    data.setState(PadDataState.IDLE);
                    PadChannel channel = data.getChannel(data.getViewChannel());
                    channel.setCurrentBeat(0);
                }

                for (PadChannel channel : data.getChannels()) {
                    Tone tone = controller.getSoundSource().getTone(channel.getIndex());
                    tone.setMuted(true);
                }
            }

            flushedQueue.clear();

            for (PadData data : playQueue) {
                for (PadChannel channel : data.getChannels()) {
                    Tone tone = controller.getSoundSource().getTone(channel.getIndex());
                    if (tone != null)
                        tone.setMuted(false);
                }
            }

            //CtkDebug.model(">> Remainder:" + remainder);
            lockAndExtendPlayingTracks();
        }

        // last beat in measure
        if (currentLocalBeat == 3) {
            queueTracks();
        }

        for (PadData data : playQueue) {
            PadChannel channel = data.getChannel(data.getViewChannel());
            //int calcBeatInMeasure = channel.getChannelPhrase().getLength();
            //Track track = getSong().getTrack(channel.getIndex());
            //TrackItem trackItem = track.getTrackItem(measure);
            //if (trackItem != null) {
            //int beats = trackItem.getStartMeasure() * 4;
            //beats = beat - beats;
            //int currentLocalBeat
            //.setCurrentBeat(beat);
            channel.setCurrentBeat(channel.getCurrentBeat() + 1);
            //}
        }
    }

    private void queueTracks() {
        @SuppressWarnings("unused")
        final int currentBeat = getSong().getCurrentBeat();
        final int currentMeasure = getSong().getCurrentMeasure();

        //CtkDebug.log("Setup queued [" + currentBeat + "," + currentMeasure + "]");

        ArrayList<PadData> copied = new ArrayList<PadData>(queued);
        // loop through the queue and add queued items
        for (PadData data : copied) {
            if (data.getState() == PadDataState.QUEUED) {
                // add to sequencer
                for (PadChannel channel : data.getChannels()) {
                    Track track = song.getTrack(channel.getIndex());
                    addPhraseAt(track, currentMeasure + 1, channel.getChannelPhrase());
                }
                startPlaying(data);
            } else if (data.getState() == PadDataState.SELECTED) {

            }
        }
    }

    public boolean queue(PadData data) {
        if (currentLocalBeat == 3)
            return false;

        if (!queued.contains(data)) {
            CtkDebug.log("Queue:" + data);
            data.setState(PadDataState.QUEUED);
            queued.add(data);
        }
        return true;
    }

    public boolean unqueue(PadData data) {
        if (currentLocalBeat == 3)
            return false;
        if (playQueue.contains(data)) {
            if (data.getState() == PadDataState.QUEUED) {
                data.setState(PadDataState.IDLE);
                playQueue.remove(data);
            } else {
                data.setState(PadDataState.UNQUEUED);
            }
        } else {
            queued.remove(data);
            data.setState(PadDataState.IDLE);
        }

        return true;
    }

    public void play() throws CausticException {
        getSong().rewind();

        ArrayList<PadData> copied = new ArrayList<PadData>(queued);
        for (PadData data : copied) {
            if (data.getState() == PadDataState.QUEUED) {
                // add to sequencer
                for (PadChannel channel : data.getChannels()) {
                    Track track = getSong().getTrack(channel.getIndex());
                    addPhraseAt(track, 0, channel.getChannelPhrase());
                }
                startPlaying(data);
            } else if (data.getState() == PadDataState.SELECTED) {

            }
        }
        //getSong().play();
        controller.getSystemSequencer().play(SequencerMode.SONG);
    }

    private void lockAndExtendPlayingTracks() {
        final int beat = getSong().getCurrentBeat();
        final int currentMeasure = getSong().getCurrentMeasure();
        @SuppressWarnings("unused")
        final int isNewMeasure = beat % 4;

        // from here on, we have everything correct with current beat and measure
        // the TrackSong's cursor is correct.
        // Check to see if there are any tracks that are in their last beat
        // All tracks in the last beat get extended their length

        for (Track track : getSong().getTracks()) {

            // First try an find a track item at the current measure
            List<TrackItem> list = track.getItemsOnMeasure(currentMeasure);
            for (TrackItem item : list) {
                //String name = PatternUtils.toString(item.getBankIndex(), item.getPatternIndex());

                //int numPhraseMeasures = item.getNumMeasures();
                //int numBeatsInPhrase = (4 * numPhraseMeasures);
                //int startMeasure = beat % numBeatsInPhrase;

                //CtkDebug.model("XXX:" + startMeasure);
                // If the current beat is the last beat in the phrase
                if (item.getEndMeasure() == currentMeasure + 1) {
                    //if (startMeasure == numBeatsInPhrase - 1) {

                    PadData data = padMap.getPad(item.getBankIndex(), item.getPatternIndex());

                    for (PadChannel channel : data.getChannels()) {

                        if (channel.isLoopEnabled()) {

                            if (data.getState() == PadDataState.SELECTED) {
                                // add the phrase at the very next measure
                                addPhraseAt(track, currentMeasure + 1, channel.getChannelPhrase());
                                channel.setCurrentBeat(0);
                            } else if (data.getState() == PadDataState.UNQUEUED) {
                                // remove the item from the que
                                stopPlaying(data);
                            }

                        } else {
                            // oneshot remove
                            stopPlaying(data);
                            // set this here since turning off is immediate
                            data.setState(PadDataState.IDLE);
                        }

                    }

                    //CtkDebug.model("Lock:" + beat);
                }
            }
        }
    }

    private void addPhraseAt(Track track, int start, ChannelPhrase phrase) {
        try {
            track.addPhraseAt(start, 1, phrase, true);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    private void startPlaying(PadData data) {
        data.setState(PadDataState.SELECTED);
        queued.remove(data);
        playQueue.add(data);
    }

    private void stopPlaying(PadData data) {
        flushedQueue.add(data);
        playQueue.remove(data);
        // things will get set to idel in flush if not queued again
        // data.setState(PadDataState.IDLE);
    }

    //--------------------------------------------------------------------------

    public static void _addChannelTracks(PadData data, TrackSong song, int currentMeasure) {
        for (PadChannel channel : data.getChannels()) {
            Track track = song.getTrack(channel.getIndex());
            try {
                track.addPhraseAt(currentMeasure, 1, channel.getChannelPhrase(), true);
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    }

}
