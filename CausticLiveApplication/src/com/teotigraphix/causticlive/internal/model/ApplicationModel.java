////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.causticlive.internal.model;

import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnCreateEvent;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.activity.event.OnNewIntentEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnRestartEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.activity.event.OnStartEvent;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.ContextSingleton;
import android.app.Activity;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.android.service.ITouchService;
import com.teotigraphix.caustic.controller.ISequencerController.OnSequencerSeekEvent;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.song.PatternQueue;
import com.teotigraphix.caustic.internal.song.PatternQueue.PatternQueueData;
import com.teotigraphix.caustic.internal.song.PatternQueue.QueueListener;
import com.teotigraphix.caustic.internal.song.TrackSong;
import com.teotigraphix.caustic.output.IOutputPanel.Mode;
import com.teotigraphix.caustic.part.IPart;
import com.teotigraphix.caustic.part.ISoundGenerator;
import com.teotigraphix.caustic.rack.IRack.SongStateChangeKind;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IProject.OnProjectSongChangeEvent;
import com.teotigraphix.caustic.song.ITrack;
import com.teotigraphix.caustic.song.ITrackSong;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.caustic.song.IWorkspace.OnRackStateChangedEvent;
import com.teotigraphix.caustic.song.IWorkspace.OnWorkspaceProjectChangeEvent;
import com.teotigraphix.caustic.song.IWorkspace.OnWorkspaceRunEvent;
import com.teotigraphix.causticlive.model.IApplicationModel;

@ContextSingleton
public class ApplicationModel implements IApplicationModel {

    protected static final String TAG = null;

    private ISoundGenerator mGenerator;

    //--------------------------------------------------------------------------
    // Mediators
    //--------------------------------------------------------------------------

    @Inject
    Activity activity;

    @Inject
    EventManager eventManager;

    @Inject
    ITouchService touchService;

    //@Inject
    //PadMatrixMediator padMatrixMediator;

    //@Inject
    //ControlBarMediator controlBarMediator;

    //@Inject
    //TransportControlMediator transportControlMediator;

    //--------------------------------------------------------------------------
    // IApplicationModel API :: Properties
    //--------------------------------------------------------------------------

    //----------------------------------
    // workspace
    //----------------------------------

    private IWorkspace workspace;

    @Override
    public IWorkspace getWorkspace() {
        return workspace;
    }

    @Inject
    void setWorkspace(IWorkspace value) {
        workspace = value;
        mGenerator = workspace.getGenerator();
    }

    //----------------------------------
    // queue
    //----------------------------------

    private PatternQueue mQueue;

    @Override
    public PatternQueue getQueue() {
        return mQueue;
    }

    //----------------------------------
    // project
    //----------------------------------

    public IProject getProject() {
        return workspace.getProject();
    }

    //----------------------------------
    // project
    //----------------------------------

    public ITrackSong getSelectedSong() {
        return (ITrackSong)workspace.getProject().getSelectedSong();
    }

    void onWorkspaceRunEvent(@Observes OnWorkspaceRunEvent event) {
        Log.d("IApp", "onWorkspaceRunEvent");
        //        // TODO (mchmalle) figure out the correct timing for creating song and queue
        //        //setSong(new TrackSong("Untitled"));
        //        IProject project = workspace.loadProject("Untitled.xml", "Untitled Project");
        //        ITrackSong song = new TrackSong("Untitled Song");
        //        // will fire the song change event that will call setSong() on us
        //        // to the current song
        //        project.addSong(song);
        //        // dispatch to all current mediators we are ready to update the ui views
        //        eventManager.fire(new OnAttachMediatorEvent());
    }

    void onProjectSongChangeEvent(@Observes OnProjectSongChangeEvent event) {
        ITrackSong song = (ITrackSong)event.getSong();
        ITrackSong oldSong = (ITrackSong)event.getOldSong();
        Log.d("IApp", "onProjectSongChangeEvent [" + song.getData().getName() + "]");
        configureSongChange(song, oldSong);
    }

    private void configureSongChange(ITrackSong song, ITrackSong oldSong) {
        if (mQueue != null) {
            mQueue.removeListener(queueListener);
            if (oldSong != null)
                oldSong.removeSongListener(mQueue);
        }
        mQueue = new PatternQueue();
        mQueue.addListener(queueListener);
        song.addSongListener(mQueue);
    }

    /*
    OnContentChangedEvent
    OnCreateEvent
    OnStartApplication
    OnStartEvent
    OnResumeEvent
    */

    void onWorkspaceProjectChangeEvent(@Observes OnWorkspaceProjectChangeEvent event) {
        Log.d("IApp", "onWorkspaceProjectChangeEvent [" + event.getProject().getName() + "]");

    }

    void onSequencerSeekEvent(@Observes OnSequencerSeekEvent event) {
        if (event.getBeat() == 0) {
            getSelectedSong().rewind();
            return;
        }
        getSelectedSong().seek(event.getBeat());
    }

    public void onCreateEvent(@Observes OnCreateEvent event) {
        Log.d("IApp", "OnCreateEvent");
        //MainLayout layout = (MainLayout)activity.findViewById(R.id.main_layout);
        //layout.setTouchService(touchService);
    }

    public void onStartEvent(@Observes OnStartEvent event) {
        Log.d("IApp", "OnStartEvent");
        //        //        eventManager.fire(new OnStartApplication());
        //        try {
        //            workspace.startAndRun();
        //        } catch (CausticException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }
    }

    public void onDestroyEvent(@Observes OnDestroyEvent event) {
        Log.d("IApp", "OnDestroyEvent");
    }

    public void onContentChangedEvent(@Observes OnContentChangedEvent event) {
        Log.d("IApp", "OnContentChangedEvent");
    }

    public void onNewIntentEvent(@Observes OnNewIntentEvent event) {
        Log.d("IApp", "OnNewIntentEvent");
    }

    public void onPauseEvent(@Observes OnPauseEvent event) {
        Log.d("IApp", "OnPauseEvent");
    }

    public void onRestartEvent(@Observes OnRestartEvent event) {
        Log.d("IApp", "OnRestartEvent");
    }

    public void onResumeEvent(@Observes OnResumeEvent event) {
        Log.d("IApp", "OnResumeEvent");
    }

    //--------------------------------------------------------------------------
    // IApplicationModel API :: Methods
    //--------------------------------------------------------------------------

    @Override
    public ITrack getTrack(int trackIndex) {
        return getSelectedSong().getTrack(trackIndex);
    }

    @Override
    public void loadSong(String absolutePath) {
        //        mProject.removeSong(getSong());

        // all the framework specific stuff is done, this is just the 
        // application acting on the project change now.
        // for NOW I am creating the track song here
        ITrackSong song = new TrackSong("Untitled");
        getProject().addSong(song); // project fires songAdd

        try {
            workspace.getRack().loadSong(absolutePath);
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }

    void onRackStateChangedListener(@Observes OnRackStateChangedEvent event) {
        if (event.getKind() == SongStateChangeKind.RESTORED) {

            for (IPart part : mGenerator.getParts()) {
                getSelectedSong().addTrack(part);
            }

            eventManager.fire(new OnSongRestoreEvent());

            workspace.getRack().getSequencer().clearPatterns();
            workspace.getRack().getOutputPanel().setMode(Mode.PATTERN);
            //workspace.getRack().getOutputPanel().play();
        } else if (event.getKind() == SongStateChangeKind.LOADED) {
            workspace.getRack().restore();
        }
    }

    //--------------------------------------------------------------------------
    // Listeners
    //--------------------------------------------------------------------------

    private QueueListener queueListener = new QueueListener() {
        @Override
        public void onAdded(PatternQueueData data) {
            eventManager.fire(new OnPatternQueueChange(data, PatternQueueChangeKind.ADDED));
        }

        @Override
        public void onRemoved(PatternQueueData data) {
            eventManager.fire(new OnPatternQueueChange(data, PatternQueueChangeKind.REMOVED));
        }
    };
}
