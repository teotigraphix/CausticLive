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
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.inject.Inject;
import com.teotigraphix.caustic.view.Mediator;
import com.teotigraphix.causticlive.BrowserActivity;
import com.teotigraphix.causticlive.R;

@ContextSingleton
public class ControlBarMediator extends Mediator {

    @Inject
    Activity context;

    @InjectView(R.id.button_open_library)
    Button openLibraryButton;

    @Override
    protected void onAttachMediator() {
        Log.d("ControlBarMediator", "onAttachMediator()");
        openLibraryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrowserActivity.class);
                context.startActivity(intent);
            }
        });
    }

}
