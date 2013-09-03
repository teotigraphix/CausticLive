
package com.teotigraphix.causticlive.view.components.assign;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.teotigraphix.libgdx.ui.ButtonBar;
import com.teotigraphix.libgdx.ui.ButtonBar.OnButtonBarListener;

public class MachineButtonBar extends Table {

    private Skin skin;

    private ButtonBar buttonBar;

    private OnMachineButtonBarListener listener;

    public MachineButtonBar(Skin skin) {
        super(skin);
        this.skin = skin;
        initialize();
    }

    private void initialize() {
        String[] items = new String[] {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14"
        };
        buttonBar = new ButtonBar(skin, items, false);
        buttonBar.setOnButtonBarListener(new OnButtonBarListener() {
            @Override
            public void onChange(int index) {
                listener.onMachineChange(index);
            }
        });

        add(buttonBar).expandX().height(75f);
    }

    public void setOnMachineButtonBarListener(OnMachineButtonBarListener l) {
        listener = l;
    }

    public interface OnMachineButtonBarListener {
        void onMachineChange(int index);
    }

    public void select(int index) {
        buttonBar.select(index);
    }
}
