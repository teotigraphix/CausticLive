
package com.teotigraphix.causticlive.internal.model;

import roboguice.inject.ContextSingleton;
import android.util.Log;

import com.google.inject.Inject;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.output.IOutputPanel.Mode;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.causticlive.model.IBrowserModel;

@ContextSingleton
public class BrowserModel implements IBrowserModel {

    private static final String TAG = "BrowserModel";

    @Inject
    IWorkspace workspace;

    @Override
    public void loadSong(String absolutPath) {
        try {
            workspace.getRack().loadSong(absolutPath);
            workspace.getRack().getOutputPanel().setMode(Mode.SONG);
            workspace.getRack().getOutputPanel().play();
        } catch (CausticException e) {
            Log.e(TAG, "rack.loadSong(absolutPath)", e);
        }
    }

    public BrowserModel() {
        Log.d(TAG, "BrowserModel()");
    }

}
