
package com.teotigraphix.causticlive.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;

public class SongListMediator extends MediatorBase {

    @Inject
    ILibraryModel libraryModel;

    public SongListMediator() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void create(IScreen screen) {
        // TODO Auto-generated method stub
        super.create(screen);
        File songsDirectory = RuntimeUtils.getCausticSongsDirectory();
        Collection<File> songs = FileUtils.listFiles(songsDirectory, new String[] {
            "caustic"
        }, true);

        final ArrayList<SongData> dp = new ArrayList<SongListMediator.SongData>();
        for (File file : songs) {
            dp.add(new SongData(file));
        }

        final List list = new List(dp.toArray(new SongData[] {}), screen.getSkin());
        list.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
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
        ScrollPane scrollPane2 = new ScrollPane(list, screen.getSkin());
        scrollPane2.setPosition(65f, 235f);
        scrollPane2.setSize(330f, 400f);

        screen.getStage().addActor(scrollPane2);

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
