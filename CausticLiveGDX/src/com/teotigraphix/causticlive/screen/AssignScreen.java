
package com.teotigraphix.causticlive.screen;

import com.google.inject.Inject;
import com.teotigraphix.causticlive.view.assign.BackButtonMediator;
import com.teotigraphix.causticlive.view.assign.ToneSelectBoxMediator;
import com.teotigraphix.causticlive.view.assign.PhraseListMediator;
import com.teotigraphix.libgdx.application.IGame;
import com.teotigraphix.libgdx.screen.ScreenBase;

public class AssignScreen extends ScreenBase {

    @Inject
    BackButtonMediator backButtonMediator;

    @Inject
    ToneSelectBoxMediator machineButtonBarMediator;

    @Inject
    PhraseListMediator phraseListMediator;

    //    @Inject
    //    PatchListMediator patchListMediator;

    public AssignScreen() {
    }

    @Override
    public void initialize(IGame game) {
        super.initialize(game);
        SkinRegistry.register(getSkin());
        addMediator(backButtonMediator);
        addMediator(machineButtonBarMediator);
        addMediator(phraseListMediator);
        //        addMediator(patchListMediator);
    }

}
