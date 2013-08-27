
package com.teotigraphix.causticlive.view;

import org.androidtransfuse.event.EventObserver;

import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.IPadModel;
import com.teotigraphix.causticlive.model.IPadModel.OnPadModelSelectedDataChange;
import com.teotigraphix.causticlive.view.components.BankBar;
import com.teotigraphix.causticlive.view.components.BankBar.OnBankBarListener;
import com.teotigraphix.libgdx.controller.MediatorBase;
import com.teotigraphix.libgdx.screen.IScreen;

public class BankBarMediator extends MediatorBase {

    @Inject
    IPadModel padModel;

    private BankBar bankBar;

    public BankBarMediator() {
    }

    @Override
    protected void registerObservers() {
        super.registerObservers();
        register(padModel.getDispatcher(), OnPadModelSelectedDataChange.class,
                new EventObserver<OnPadModelSelectedDataChange>() {
                    @Override
                    public void trigger(OnPadModelSelectedDataChange object) {
                        int bank = object.getData().getBank();
                        bankBar.select(bank);
                    }
                });
    }

    @Override
    public void create(IScreen screen) {
        bankBar = new BankBar(screen.getSkin());
        bankBar.setOnBankBarListener(new OnBankBarListener() {
            @Override
            public void onChange(int index) {
                padModel.select(index);
            }
        });
        bankBar.setPosition(540f, 365f);
        bankBar.setSize(50, 270);
        screen.getStage().addActor(bankBar);
    }

    @Override
    public void onRegister() {
    }

}
