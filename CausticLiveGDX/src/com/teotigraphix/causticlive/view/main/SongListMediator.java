
package com.teotigraphix.causticlive.view.main;

import java.io.File;

import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.libgdx.controller.CaustkMediator;
import com.teotigraphix.libgdx.screen.IScreen;

public class SongListMediator extends CaustkMediator {

    @Inject
    ILibraryModel libraryModel;

    public SongListMediator() {
    }

    @Override
    public void create(IScreen screen) {

        //
        //        final ScrollList scrollList = new ScrollList(screen.getSkin(), dp);
        //        scrollList.setPosition(65f, 235f);
        //        scrollList.setSize(330f, 400f);
        //
        //        screen.getStage().addActor(scrollList);
        //
        //        GDXButton button = new GDXButton("Load", screen.getSkin());
        //        button.setPosition(65f, 200f);
        //        button.setSize(100f, 40f);
        //        button.addListener(new ActorGestureListener() {
        //            @Override
        //            public void tap(InputEvent event, float x, float y, int count, int button) {
        //                int index = scrollList.getSelectedIndex();
        //                SongData data = (SongData)scrollList.getItem(index);
        //                try {
        //                    libraryModel.importSong(data.file);
        //                } catch (IOException e) {
        //                    e.printStackTrace();
        //                } catch (CausticException e) {
        //                    e.printStackTrace();
        //                }
        //            }
        //        });
        //        screen.getStage().addActor(button);
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
