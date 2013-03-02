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

package com.teotigraphix.causticlive.view;

import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.inject.Inject;
import com.teotigraphix.caustic.controller.ISequencerController;
import com.teotigraphix.caustic.output.IOutputPanel.Mode;
import com.teotigraphix.caustic.view.Mediator;
import com.teotigraphix.causticlive.R;

@ContextSingleton
public class TransportControlMediator extends Mediator {

    @Inject
    ISequencerController controller;

    @InjectView(R.id.button_rewind_song)
    Button rewindSongButton;

    @InjectView(R.id.button_play_song)
    Button playSongButton;

    @InjectView(R.id.button_stop_song)
    Button stopSongButton;

    @Override
    protected void onAttachMediator() {
        Log.d("TransportControlMediator", "onAttachMediator()");
        rewindSongButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.seek(0);
            }
        });

        playSongButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.play(true, Mode.PATTERN);
            }
        });

        stopSongButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.play(false);
            }
        });
    }
}
