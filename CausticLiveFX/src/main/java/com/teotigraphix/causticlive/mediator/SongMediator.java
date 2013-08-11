
package com.teotigraphix.causticlive.mediator;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.MediatorBase;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSongSequencerBeatChange;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.OnSongSequencerMeasureChange;

@Singleton
public class SongMediator extends MediatorBase {

    @Inject
    ISoundModel soundModel;

    public SongMediator() {
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();

        getController().getDispatcher().register(OnSongSequencerMeasureChange.class,
                new EventObserver<OnSongSequencerMeasureChange>() {
                    @Override
                    public void trigger(OnSongSequencerMeasureChange object) {
                        //CtkDebug.model("Measure:" + object.getMeasure());
                        //soundModel.getSong().nextMeasure();
                        soundModel.measureChange(object.getMeasure());
                        //CtkDebug.model("M:" + soundModel.getSong().getCurrentBeat());
                    }
                });
        /*
        Beat:0 % 0
        Beat:0 % 1
        Beat:0 % 2
        Beat:0 % 3
        Beat:1 % 0
        Beat:1 % 1
        Beat:1 % 2
        Beat:1 % 3
        Beat:2 % 0
        Beat:0 % 0        
         */
        getController().getDispatcher().register(OnSongSequencerBeatChange.class,
                new EventObserver<OnSongSequencerBeatChange>() {
                    @Override
                    public void trigger(OnSongSequencerBeatChange object) {
                        int beat = object.getBeat();
                        int measure = beat / 4;
                        int startMeasure = beat % 4;
                        if (startMeasure == 0) {
                            // new measure
                          
                        } else if (startMeasure == 3) {
                            // one beat before changing measures
                            // signal add crap, lock UI for patterns in their last beat
                            // 1 measure, 3 beat, 2 measure 7 beat, 4 measure, 15 beat 8, 31
                        }
                        soundModel.beatChange(measure, beat);
                        //CtkDebug.model("Beat:" + beat / 4 + " % " + (beat % 4));
                    }
                });

    }

    @Override
    public void onRegister() {
        // TODO Auto-generated method stub

    }

}
