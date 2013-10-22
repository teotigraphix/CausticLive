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

package com.teotigraphix.causticlive;

import com.google.inject.Module;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.screen.ICausticLiveScreen;
import com.teotigraphix.causticlive.screen.MainScreen;
import com.teotigraphix.causticlive.screen.SplashScreen;
import com.teotigraphix.caustk.rack.ISoundGenerator;
import com.teotigraphix.libgdx.application.GDXGame;

@Singleton
public class CausticLiveApp extends GDXGame {

    public CausticLiveApp(ISoundGenerator generator, Module module) {
        super("CausticLive", generator, module);
        addScreen(ICausticLiveScreen.SPLASH_SCREEN, SplashScreen.class);
        addScreen(ICausticLiveScreen.MAIN_SCREEN, MainScreen.class);
    }

    @Override
    public void create() {
        super.create();
        setScreen(ICausticLiveScreen.SPLASH_SCREEN);
    }

}
