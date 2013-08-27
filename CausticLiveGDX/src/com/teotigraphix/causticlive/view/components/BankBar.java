
package com.teotigraphix.causticlive.view.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.libgdx.ui.ToggleButton;

public class BankBar extends Table {

    List<ToggleButton> buttons = new ArrayList<ToggleButton>();

    private Skin skin;

    private ButtonGroup group;

    String[] banks = {
            "A", "B", "C", "D"
    };

    public BankBar(Skin skin) {
        super(skin);
        this.skin = skin;
        group = new ButtonGroup();
        createChildren();
    }

    private OnBankBarListener listener;

    private void createChildren() {

        for (int i = 0; i < 4; i++) {
            final int index = i;
            final ToggleButton button = new ToggleButton("", skin);
            add(button).fill().expand().minHeight(0).prefHeight(999);
            button.setText(banks[i]);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (button.isChecked()) {
                        if (listener != null)
                            listener.onChange(index);
                    }
                }
            });
            row();
            buttons.add(button);
            group.add(button);
        }
    }

    public void setOnBankBarListener(OnBankBarListener l) {
        listener = l;
    }

    public interface OnBankBarListener {
        void onChange(int index);
    }

    public void select(int index) {
        ToggleButton actor = (ToggleButton)getChildren().get(index);
        actor.setChecked(true);
    }

}
