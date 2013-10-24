
package com.teotigraphix.causticlive.view.admin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.scenes.scene2d.ui.AlertDialog.OnAlertDialogListener;
import com.badlogic.gdx.scenes.scene2d.ui.ListDialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.view.admin.components.ImportCausticFilePane;
import com.teotigraphix.causticlive.view.admin.components.ImportCausticFilePane.OnImportCausticFilePaneListener;
import com.teotigraphix.causticlive.view.admin.components.LoadProjectPane;
import com.teotigraphix.causticlive.view.admin.components.LoadProjectPane.OnLoadProjectPaneListener;
import com.teotigraphix.causticlive.view.admin.components.NewProjectPane;
import com.teotigraphix.causticlive.view.admin.components.NewProjectPane.OnNewProjectPaneListener;
import com.teotigraphix.causticlive.view.admin.components.SaveProjectPane;
import com.teotigraphix.causticlive.view.admin.components.SaveProjectPane.OnSaveProjectPaneListener;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.utils.RuntimeUtils;
import com.teotigraphix.libgdx.controller.ScreenMediatorChild;
import com.teotigraphix.libgdx.model.IApplicationModel;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Pane;
import com.teotigraphix.libgdx.ui.PaneStack;
import com.teotigraphix.libgdx.ui.caustk.DialogFactory;

@Singleton
public class SongPaneMediator extends ScreenMediatorChild {

    @Inject
    IApplicationModel applicationModel;

    @Inject
    ILibraryModel libraryModel;

    private PaneStack paneStack;

    public SongPaneMediator() {
    }

    @Override
    public void onCreate(IScreen screen, WidgetGroup parent) {
        super.onCreate(screen, parent);

        setupPane(screen.getSkin(), (Pane)parent);
    }

    private Pane setupPane(Skin skin, Pane pane) {

        paneStack = new PaneStack(skin, Align.bottom);
        paneStack.setSelectedIndex(0);
        pane.add(paneStack).expand().fill();

        NewProjectPane newPane = new NewProjectPane(skin, "New");
        newPane.setOnNewProjectPaneListener(new OnNewProjectPaneListener() {
            @Override
            public void onCreateTap() {
                createNewProjectDialog();
            }
        });
        paneStack.addPane(newPane);

        LoadProjectPane loadPane = new LoadProjectPane(skin, "Load");
        loadPane.setOnLoadProjectPaneListener(new OnLoadProjectPaneListener() {
            @Override
            public void onLoadTap() {
                DialogFactory.createAndShowLoadProjectDialog(applicationModel);
            }
        });
        paneStack.addPane(loadPane);

        SaveProjectPane savePane = new SaveProjectPane(skin, "Save");
        savePane.setOnSaveProjectPaneListener(new OnSaveProjectPaneListener() {
            @Override
            public void onSaveTap() {
                try {
                    getController().getApplication().save();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        paneStack.addPane(savePane);

        ImportCausticFilePane importPane = new ImportCausticFilePane(skin, "Import");
        importPane.setOnImportCausticFilePaneListener(new OnImportCausticFilePaneListener() {
            @Override
            public void onImportTap() {
                createAndImportDialog(applicationModel, libraryModel);
            }
        });
        paneStack.addPane(importPane);

        return pane;
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

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
