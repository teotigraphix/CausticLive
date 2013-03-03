
package com.teotigraphix.causticlive.internal.controller;

import roboguice.activity.event.OnActivityResultEvent;
import roboguice.activity.event.OnContentChangedEvent;
import roboguice.activity.event.OnCreateEvent;
import roboguice.activity.event.OnDestroyEvent;
import roboguice.activity.event.OnNewIntentEvent;
import roboguice.activity.event.OnPauseEvent;
import roboguice.activity.event.OnRestartEvent;
import roboguice.activity.event.OnResumeEvent;
import roboguice.activity.event.OnStartEvent;
import roboguice.activity.event.OnStopEvent;
import roboguice.event.Observes;
import android.app.Activity;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.controller.IApplicationPreferences;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.router.IRouter;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.caustic.view.Mediator.OnAttachMediatorEvent;
import com.teotigraphix.causticlive.R;
import com.teotigraphix.causticlive.view.ControlBarMediator;

/*
> Startup from dashboard | Startup after Back button from Recent menu
OnContentChangedEvent
!!! OnCreateEvent
OnStartEvent
OnResumeEvent

> Back button
OnPauseEvent
OnStopEvent
!!! OnDestroyEvent

> Recent Menu Out of App
OnPauseEvent
OnStopEvent

> Recent Menu In to App
!!! OnRestartEvent
OnStartEvent
OnResumeEvent


[OnCreateEvent|OnRestartEvent]
OnStartEvent
OnResumeEvent

[OnDestroyEvent|Recent Menu]
OnPauseEvent
OnStopEvent


*/

// should be ApplicationInstrumentation, the facade template for all majors states and phases
public class ApplicationHandlers {

    @Inject
    Activity activity;

    @Inject
    IWorkspace workspace;

    @Inject
    IRouter router;

    @Inject
    IApplicationController controller;

    @Inject
    IApplicationPreferences preferences;

    @Inject
    ControlBarMediator controlBarMediator;

    private static final String TAG = "ApplicationHandlers";

    void onCreateEvent(@Observes OnCreateEvent event) throws CausticException {
        Log.d(TAG, "OnCreateEvent");

        // startup the workspace since create is always a fresh startup
        // controller.sendCommand(IApplicationController.START_WORKSPACE);
        // for now this cannot be run from a command because it sets up the app root
        workspace.startAndRun();
        // register all module client commands [OnRegisterRouterCommandsEvent]
        router.initialize();
        // load the last project
        controller.sendCommand(IApplicationController.LOAD_PROJECT,
                preferences.getLastProjectFile());
    }

    void onContentChangedEvent(@Observes OnContentChangedEvent event) {
        Log.d(TAG, "OnContentChangedEvent");
        // register the layout
        controller.sendCommand(IApplicationController.REGISTER_MAIN_LAYOUT, R.id.main_layout);
        // OnSetupMediatorEvent ?
        // all models, sound system and rack are restored, notify the mediators
        workspace.getEventManager().fire(new OnAttachMediatorEvent());
    }

    void onStartEvent(@Observes OnStartEvent event) {
        Log.d(TAG, "OnStartEvent");
    }

    void onStopEvent(@Observes OnStopEvent event) {
        Log.d(TAG, "onStopEvent");
    }

    void onDestroyEvent(@Observes OnDestroyEvent event) {
        Log.d(TAG, "OnDestroyEvent");
    }

    void onNewIntentEvent(@Observes OnNewIntentEvent event) {
        Log.d(TAG, "OnNewIntentEvent");
    }

    void onPauseEvent(@Observes OnPauseEvent event) {
        Log.d(TAG, "OnPauseEvent");
    }

    void onRestartEvent(@Observes OnRestartEvent event) {
        Log.d(TAG, "OnRestartEvent");
    }

    void onResumeEvent(@Observes OnResumeEvent event) {
        Log.d(TAG, "OnResumeEvent");
    }

    void onActivityResultEvent(@Observes OnActivityResultEvent event) {
        Log.d(TAG, "onActivityResultEvent");
    }
}
