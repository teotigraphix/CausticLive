
package com.teotigraphix.causticlive;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.teotigraphix.causitc.sound.AndroidSoundGenerator;
import com.teotigraphix.causticlive.config.CausticLiveModule;
import com.teotigraphix.caustk.controller.ICaustkController;

public class MainActivity extends AndroidApplication {

    ICaustkController controller;

    private CausticLiveApp listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;

        listener = new CausticLiveApp(new AndroidSoundGenerator(this, 0x8A7D57E0),
                new CausticLiveModule());
        initialize(listener, cfg);

    }
}
