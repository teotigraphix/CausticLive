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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.inject.Inject;
import com.teotigraphix.caustic.dialog.CausticUtils;
import com.teotigraphix.caustic.dialog.CausticUtils.ISongChooserListener;
import com.teotigraphix.caustic.view.Mediator;
import com.teotigraphix.causticlive.R;
import com.teotigraphix.common.utils.RuntimeUtils;

@ContextSingleton
public class ControlBarMediator extends Mediator implements ISongChooserListener {

    private static final String MESSAGE_LOADING_SONG = "Loading. Please wait...";

    @Inject
    Activity context;

    //@Inject
    //IApplicationModel model;

    @InjectView(R.id.button_load_song)
    Button loadSongButton;

    ProgressDialog dialog;

    @Override
    protected void onAttachMediator() {
        Log.d("ControlBarMediator", "onAttachMediator()");
        loadSongButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog chooser = CausticUtils.buildSongChooser(context,
                        ControlBarMediator.this);
                chooser.show();
            }
        });
    }

    @Override
    public void onSongSelect(String songName) {
        new LoadSongTask().execute(returnSongPath(songName));
    }

    private class LoadSongTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            createLoadingDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            //model.loadSong(params[0]);
            return params[0];
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private final String returnSongPath(String songName) {
        //        return RuntimeUtils.getDirectory("caustic/songs").getAbsolutePath() + "/"
        //                + model.getSongNames().get(item) + ".caustic";
        return RuntimeUtils.getDirectory("caustic/songs").getAbsolutePath() + "/" + songName
                + ".caustic";
    }

    private void createLoadingDialog() {
        dialog = ProgressDialog.show(context, "", MESSAGE_LOADING_SONG, true);
        dialog.show();
    }
}
