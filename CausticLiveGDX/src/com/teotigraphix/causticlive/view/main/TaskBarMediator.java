
package com.teotigraphix.causticlive.view.main;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.causticlive.model.ISequencerModel.PadState;
import com.teotigraphix.causticlive.screen.SkinRegistry;
import com.teotigraphix.libgdx.controller.ScreenMediator;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.ButtonBar;
import com.teotigraphix.libgdx.ui.ButtonBar.OnButtonBarListener;

public class TaskBarMediator extends ScreenMediator {

    @Inject
    ISequencerModel sequencerModel;

    private Skin skin;

    private Table tableLeft;

    private Table tableRight;

    private String[] items = {
            "Perform", "Assign"
    };

    private ButtonBar stateButtonBar;

    public TaskBarMediator() {
    }

    @Override
    public void onCreate(IScreen screen) {
        super.onCreate(screen);
        skin = screen.getSkin();

        final float width = SkinRegistry.getWidth();
        final float halfWidth = width / 2;

        tableLeft = createTableLeft();
        tableLeft.setSize(halfWidth, 35f);
        tableLeft.setPosition(0f, 405f);
        screen.getStage().addActor(tableLeft);

        tableRight = createTableRight();
        tableRight.setSize(halfWidth, 35f);
        tableRight.setPosition(halfWidth, 405f);
        screen.getStage().addActor(tableRight);

    }

    private Table createTableLeft() {
        Table table = new Table();
        table.setBackground(skin.getDrawable("toolbar_background"));
        return table;
    }

    private Table createTableRight() {
        Table table = new Table();
        table.align(Align.left);
        table.padLeft(5f);
        createStateButtonBar(table);

        return table;
    }

    private void createStateButtonBar(Table table) {
        stateButtonBar = new ButtonBar(skin, items, false, "default");
        stateButtonBar.setOnButtonBarListener(new OnButtonBarListener() {
            @Override
            public void onChange(int index) {
                switch (index) {
                    case 0:
                        sequencerModel.setPadState(PadState.Perform);
                        break;

                    case 1:
                        sequencerModel.setPadState(PadState.Assign);
                        break;
                }
            }
        });
        stateButtonBar.defaults().space(10f);
        table.add(stateButtonBar).width(190f).height(30f);
    }

    @Override
    public void onShow(IScreen screen) {
        super.onShow(screen);
        stateButtonBar.select(sequencerModel.getPadState().getValue(), true);
    }

    @Override
    public void onAttach(IScreen screen) {
        super.onAttach(screen);
        //
        //        register(sequencerModel, OnSequencerModelPropertyChange.class,
        //                new EventObserver<OnSequencerModelPropertyChange>() {
        //                    @Override
        //                    public void trigger(OnSequencerModelPropertyChange object) {
        //                        switch (object.getKind()) {
        //                            case PadState:
        //                                switch (sequencerModel.getPadState()) {
        //                                    case Perform:
        //                                        stateButtonBar.select(0, true);
        //                                        break;
        //
        //                                    case Assign:
        //                                        stateButtonBar.select(1, true);
        //                                        break;
        //                                }
        //
        //                                break;
        //
        //                            case Bank:
        //                                break;
        //
        //                            case ActiveData:
        //                                break;
        //                        }
        //                    }
        //                });
    }
}
