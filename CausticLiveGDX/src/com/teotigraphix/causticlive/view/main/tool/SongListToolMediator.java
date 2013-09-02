
package com.teotigraphix.causticlive.view.main.tool;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.badlogic.gdx.utils.Array;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.view.components.tool.SongListToolBar;
import com.teotigraphix.causticlive.view.components.tool.SongListToolBar.OnSongListToolBarListener;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ScrollList;

@Singleton
public class SongListToolMediator extends ToolBarMediator {

    @Inject
    ILibraryModel libraryModel;

    private SongListToolBar toolBar;

    private ScrollList songList;

    public SongListToolMediator() {
    }

    @Override
    public void create(IScreen screen) {
        songList = new ScrollList(screen.getSkin());
        songList.setItems(getSongItems());
        setList(songList);

        toolBar = new SongListToolBar(screen.getSkin());
        toolBar.setOnSongListToolBarListener(new OnSongListToolBarListener() {
            @Override
            public void onLoadTap() {
                int index = songList.getSelectedIndex();
                SongData data = (SongData)songList.getItem(index);
                try {
                    libraryModel.importSong(data.file);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CausticException e) {
                    e.printStackTrace();
                }
            }
        });
        setToolBar(toolBar);
    }

    @Override
    public void onRegister() {
    }

    private Array<Object> getSongItems() {
        File songsDirectory = RuntimeUtils.getCausticSongsDirectory();
        Collection<File> songs = FileUtils.listFiles(songsDirectory, new String[] {
            "caustic"
        }, true);

        final Array<Object> items = new Array<Object>();
        for (File file : songs) {
            items.add(new SongData(file));
        }

        return items;
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
}
