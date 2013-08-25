
package com.teotigraphix.causticlive;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.teotigraphix.causitc.sound.AndroidSoundGenerator;
import com.teotigraphix.causticlive.config.CausticLiveModule;
import com.teotigraphix.caustk.controller.ICaustkController;

public class MainActivity extends AndroidApplication {

    ICaustkController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;

        CausticLiveApp listener = new CausticLiveApp(new AndroidSoundGenerator());
        initialize(listener, cfg);
        listener.initialize(new CausticLiveModule());
        controller = listener.getController();
    }

    @Override
    protected void onStart() {
        controller.onStart();
        super.onStart();
    }

    @Override
    protected void onResume() {
        controller.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        controller.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        controller.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        controller.onDestroy();
        super.onDestroy();
    }
}
