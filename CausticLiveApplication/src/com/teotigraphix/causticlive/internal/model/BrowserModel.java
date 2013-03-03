
package com.teotigraphix.causticlive.internal.model;

import roboguice.inject.ContextSingleton;
import android.app.Activity;
import android.util.Log;

import com.google.inject.Provider;
import com.teotigraphix.caustic.activity.ICausticConfiguration;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.core.ICausticEngine;
import com.teotigraphix.caustic.rack.IRack;
import com.teotigraphix.causticlive.model.IBrowserModel;

@ContextSingleton
public class BrowserModel implements IBrowserModel {

    private static final String TAG = "BrowserModel";

    Provider<Activity> context;

    ICausticConfiguration configuration;

    private ICausticEngine engine;

    private IRack rack;

    // Need a light weight Rack impl, can I just create another causticcore instance?

    @Override
    public void loadSong(String absolutPath) {
        try {
            rack.loadSong(absolutPath);
            //rack.getOutputPanel().setMode(Mode.SONG);
            //rack.getOutputPanel().play();
        } catch (CausticException e) {
            Log.e(TAG, "rack.loadSong(absolutPath)", e);
        }
    }

    public BrowserModel() {
        Log.d(TAG, "BrowserModel()");
    }

}
