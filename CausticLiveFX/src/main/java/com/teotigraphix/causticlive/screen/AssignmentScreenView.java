package com.teotigraphix.causticlive.screen;

import javafx.scene.layout.Pane;

import com.google.inject.Inject;
import com.teotigraphix.caustic.screen.DesktopScreenView;
import com.teotigraphix.caustic.screen.IScreenView;
import com.teotigraphix.causticlive.config.ApplicationConstants;
import com.teotigraphix.causticlive.mediator.assignment.AssignmentControlsMediator;

public class AssignmentScreenView extends DesktopScreenView implements IScreenView {

    @Inject
    AssignmentControlsMediator assignmentControlsMediator;
    
    public AssignmentScreenView() {
    }

    @Override
    protected String getResource() {
        return ApplicationConstants.SCREEN_ASSIGNMENT_VIEW;
    }

    @Override
    public void create(Pane root) {
        root.setVisible(false);
        assignmentControlsMediator.create(root);
    }

}
