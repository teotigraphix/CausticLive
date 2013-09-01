
package com.teotigraphix.causticlive.view.main;

import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ButtonBar;
import com.teotigraphix.libgdx.ui.ButtonBar.OnButtonBarListener;

public class BankBarMediator extends MediatorBase {

    private ButtonBar view;

    private String[] items = {
            "A", "B", "C", "D"
    };

    public BankBarMediator() {
    }

    @Override
    public void create(IScreen screen) {
        view = new ButtonBar(screen.getSkin(), items, true);
        view.setOnButtonBarListener(new OnButtonBarListener() {
            @Override
            public void onChange(int index) {
                // TODO Auto-generated method stub

            }
        });
        view.setPosition(540f, 365f);
        view.setSize(50f, 250f);
        screen.getStage().addActor(view);
    }

    @Override
    public void onRegister() {
    }

}
