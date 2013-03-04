
package com.teotigraphix.causticlive.internal.service;

import roboguice.service.RoboService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CausticService extends RoboService {

    private static final String TAG = "CausticService";

    //@Inject
    //IWorkspace workspace;

    //@Inject
    //ICausticEngineCore core;

    // 1) After OnResume()
    @Override
    public void onCreate() {
        super.onCreate();
        //core.onCreate(null);
        Log.d(TAG, "onCreate()");
    }

    // 2)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand()");
        //core.onStart();
        //core.onResume();
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        //core.onPause();
        //core.onStop();
        //core.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "onBind()");
        return null;
    }

}
