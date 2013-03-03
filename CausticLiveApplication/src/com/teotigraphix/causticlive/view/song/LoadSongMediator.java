
package com.teotigraphix.causticlive.view.song;

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

/**
 * @see com.teotigraphix.caustic.view.song.LoadSongMediator
 */
public class LoadSongMediator extends Mediator implements ISongChooserListener {

    private static final String MESSAGE_LOADING_SONG = "Loading. Please wait...";

    @Inject
    Activity context;

    @InjectView(R.id.button_load_song)
    Button loadSongButton;

    ProgressDialog dialog;

    @Override
    protected void onAttachMediator() {
        Log.d("LoadSongMediator", "onAttachMediator()");

        loadSongButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog chooser = CausticUtils.buildSongChooser(context, LoadSongMediator.this);
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
