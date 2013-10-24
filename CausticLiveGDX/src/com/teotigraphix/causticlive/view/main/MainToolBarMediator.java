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

package com.teotigraphix.causticlive.view.main;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog.OnAlertDialogListener;
import com.badlogic.gdx.scenes.scene2d.ui.ListDialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.view.UI;
import com.teotigraphix.causticlive.view.UI.Component;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.OverlayButton;
import com.teotigraphix.libgdx.ui.caustk.DialogFactory;

public class MainToolBarMediator extends ScreenMediator {

    @Inject
    IApplicationModel applicationModel;

    @Inject
    ILibraryModel libraryModel;

    private OverlayButton loadButton;

    private OverlayButton createButton;

    private OverlayButton writeButton;

    private OverlayButton importButton;

    public MainToolBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);

        Table table = UI.createComponent(screen, Component.MainToolBar);
        table.defaults().space(5f);
        table.setBackground("toolbar_background");

        loadButton = createLoadButton(screen.getSkin());
        table.add(loadButton).size(75f, 30f);

        createButton = createCreateButton(screen.getSkin());
        table.add(createButton).size(75f, 30f);

        writeButton = createWriteButton(screen.getSkin());
        table.add(writeButton).size(75f, 30f);

        importButton = createImportButton(screen.getSkin());
        table.add(importButton).size(75f, 30f);

        table.add().expand();
    }

    private OverlayButton createLoadButton(Skin skin) {
        OverlayButton button = new OverlayButton("Load", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                DialogFactory.createAndShowLoadProjectDialog(applicationModel);
            }
        });
        return button;
    }

    private OverlayButton createCreateButton(Skin skin) {
        OverlayButton button = new OverlayButton("Create", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                createNewProjectDialog();
            }
        });
        return button;
    }

    private OverlayButton createWriteButton(Skin skin) {
        OverlayButton button = new OverlayButton("Write", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                try {
                    // XXX Needs to be a command the app mediator handles
                    getController().getApplication().save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return button;
    }

    private OverlayButton createImportButton(Skin skin) {
        OverlayButton button = new OverlayButton("Import", skin);
        button.setToggle(false);
        button.addListener(new ActorGestureListener() {
            @Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                createAndImportDialog(applicationModel, libraryModel);
            }
        });
        return button;
    }

    protected void createNewProjectDialog() {
        Gdx.input.getTextInput(new TextInputListener() {
            @Override
            public void input(String text) {
                try {
                    getController().getApplication().save();
                    applicationModel.createNewProject(text);
                } catch (CausticException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void canceled() {

            }
        }, "Project Name", "");
    }

    public static void createAndImportDialog(final IApplicationModel applicationModel,
            final ILibraryModel libraryModel) {
        final String[] songs = loadFileList();
        final ListDialog dialog = applicationModel.getDialogManager().createListDialog(
                applicationModel.getCurrentScreen(), "Select .caustic file", songs, 400f, 400f);

        dialog.padTop(25f);
        dialog.padLeft(2f);
        dialog.padRight(2f);
        dialog.padBottom(2f);
        dialog.setOnAlertDialogListener(new OnAlertDialogListener() {
            @Override
            public void onOk() {
                int index = dialog.getSelectedIndex();
                String songName = songs[index];
                File file = RuntimeUtils.getCausticSongFile(songName);
                try {
                    libraryModel.importSong(file);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (CausticException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
            }
        });
        dialog.show(applicationModel.getCurrentScreen().getStage());
    }

    private static String[] loadFileList() {
        File songsDirectory = RuntimeUtils.getCausticSongsDirectory();
        String[] files = null;
        if (songsDirectory.exists()) {
            // create a file filter that will ignore directories and files
            // that don't end in .caustic
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String fileName) {
                    return fileName.contains(".caustic");
                }
            };
            // list out all the files in the directory using the filter
            files = songsDirectory.list(filter);
        } else {
            files = new String[0];
        }

        // use the Collections sort, have to create a List first, then sort
        ArrayList<String> fileList = new ArrayList<String>(Arrays.asList(files));
        Collections.sort(fileList);
        // pass back the sorted file names to the array after sort
        return fileList.toArray(new String[] {});
    }
}
