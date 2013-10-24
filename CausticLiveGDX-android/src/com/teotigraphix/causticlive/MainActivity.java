
package com.teotigraphix.causticlive;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.teotigraphix.causticlive.config.CausticLiveModule;
import com.teotigraphix.caustk.rack.generator.AndroidSoundGenerator;

public class MainActivity extends AndroidApplication {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;

        CausticLiveApp listener = new CausticLiveApp(new AndroidSoundGenerator(this, 0x8A7D57E0),
                new CausticLiveModule());
        initialize(listener, cfg);
    }
}
