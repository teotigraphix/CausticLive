
package com.teotigraphix.causticlive.mediator;

import java.io.File;
import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.mediator.MediatorBase;
import com.teotigraphix.causticlive.model.IPadMapModel;
import com.teotigraphix.causticlive.model.ISoundModel;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.project.Project;

@Singleton
public class ApplicationMediator extends MediatorBase {

    private Integer numStarts;
    
    @Inject
    ISoundModel soundModel;
   
    @Inject
    IPadMapModel padMapModel;
    
    public ApplicationMediator() {
    }

    @Override
    protected void onProjectLoad() {
        Project project = getController().getProjectManager().getProject();
        numStarts = project.getInteger("numStarts");
        if (numStarts == null)
            numStarts = 0;
        numStarts++;
    }

    @Override
    protected void onProjectCreate() {
        getController().getSoundSource().clearAndReset();
    }

    @Override
    protected void onProjectSave() {
        saveCausticFile();
    }

    private void saveCausticFile() {
        // save the project's caustic file
        Project project = getController().getProjectManager().getProject();
        
        project.put("numStarts", numStarts);
        
        File file = getProjectSongFile(project);
        CtkDebug.view("ApplicationMediator saving " + file);
        try {
            getController().getSoundSource().saveSongAs(file);
        } catch (IOException e) {
            CtkDebug.err("Error saving .caustic file", e);
        }
    }

    public static File getProjectSongFile(Project project) {
        File file = project.getResource("songs/" + project.getName() + ".caustic");
        return file;
    }

    @Override
    public void onRegister() {
    }

}
