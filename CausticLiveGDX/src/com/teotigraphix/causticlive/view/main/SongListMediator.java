
package com.teotigraphix.causticlive.view.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.GDXButton;

public class SongListMediator extends MediatorBase {

    @Inject
    ILibraryModel libraryModel;

    public SongListMediator() {
    }

    @Override
    public void create(IScreen screen) {

        File songsDirectory = RuntimeUtils.getCausticSongsDirectory();
        Collection<File> songs = FileUtils.listFiles(songsDirectory, new String[] {
            "caustic"
        }, true);

        final ArrayList<SongData> dp = new ArrayList<SongListMediator.SongData>();
        for (File file : songs) {
            dp.add(new SongData(file));
        }

        final List list = new List(dp.toArray(new SongData[] {}), screen.getSkin());
        //        list.addListener(new ChangeListener() {
        //            @Override
        //            public void changed(ChangeEvent event, Actor actor) {
        //
        //            }
        //        });
        ScrollPane scrollPane2 = new ScrollPane(list, screen.getSkin());
        scrollPane2.setPosition(65f, 235f);
        scrollPane2.setSize(330f, 400f);
        //scrollPane2.setOverscroll(false, false);

        screen.getStage().addActor(scrollPane2);

        GDXButton button = new GDXButton("Load", screen.getSkin());
        button.setPosition(65f, 200f);
        button.setSize(100f, 40f);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                int index = list.getSelectedIndex();
                SongData data = dp.get(index);
                try {
                    libraryModel.importSong(data.file);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CausticException e) {
                    e.printStackTrace();
                }
            }
        });
        screen.getStage().addActor(button);
    }

    public static class SongData {

        private File file;

        public SongData(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return file.getName().replace(".caustic", "");
        }

    }

    @Override
    public void onRegister() {

    }

}
