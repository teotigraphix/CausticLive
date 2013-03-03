
package com.teotigraphix.causticlive.internal.service;

import roboguice.service.RoboService;
import android.content.Intent;
import android.os.IBinder;

import com.google.inject.Inject;
import com.teotigraphix.caustic.song.IWorkspace;

public class MyService extends RoboService {

    @Inject
    IWorkspace workspace;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
