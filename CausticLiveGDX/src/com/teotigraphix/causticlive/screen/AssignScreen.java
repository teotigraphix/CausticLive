
package com.teotigraphix.causticlive.screen;

import com.google.inject.Inject;
import com.teotigraphix.causticlive.view.assign.BackButtonMediator;
import com.teotigraphix.causticlive.view.assign.MachineButtonBarMediator;
import com.teotigraphix.causticlive.view.assign.PhraseListMediator;
import com.teotigraphix.libgdx.application.IGame;
import com.teotigraphix.libgdx.screen.ScreenBase;

public class AssignScreen extends ScreenBase {

    @Inject
    BackButtonMediator backButtonMediator;

    @Inject
    MachineButtonBarMediator machineButtonBarMediator;

    @Inject
    PhraseListMediator phraseListMediator;

    public AssignScreen() {
    }

    @Override
    public void initialize(IGame game) {
        super.initialize(game);
        addMediator(backButtonMediator);
        addMediator(machineButtonBarMediator);
        addMediator(phraseListMediator);
    }

}
