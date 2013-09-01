
package com.teotigraphix.causticlive.view.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.teotigraphix.libgdx.ui.GDXToggleButton;

public class PadGrid extends WidgetGroup {

    private Skin skin;

    private int numRows = 4;

    private int numColums = 4;

    private boolean sizeInvalid;

    private OnPadGridListener listener;

    public PadGrid(Skin skin) {
        this.skin = skin;
        createChildren();
    }

    private void createChildren() {
        int index = 0;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColums; j++) {
                final GDXToggleButton button = new GDXToggleButton(index + "", skin);
                button.getProperties().put("index", index);
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        GDXToggleButton button = (GDXToggleButton)actor;
                        listener.onChange((Integer)button.getProperties().get("index"),
                                button.isChecked());
                    }
                });
                button.addListener(new ActorGestureListener() {
                    @Override
                    public void tap(InputEvent event, float x, float y, int count, int button) {
                    }

                    @Override
                    public boolean longPress(Actor actor, float x, float y) {
                        listener.onLongPress((Integer)button.getProperties().get("index"), x, y);
                        return true;
                    }
                });
                addActor(button);
                index++;
            }
        }
    }

    @Override
    public void layout() {
        // called at the end of validate()

        if (sizeInvalid)
            computeSize();

        float gap = 28f;

        float calcX = 0f;
        float calcY = 0f;

        float calcWidth = 115;
        float calcHeight = 115;

        int index = 0;
        Array<Actor> children = getChildren();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColums; j++) {
                Actor child = children.get(index);
                child.setX(calcX);
                child.setY(calcY);
                child.setSize(calcWidth, calcHeight);
                calcX += gap + calcWidth;
                index++;
            }
            calcX = 0;
            calcY += gap + calcHeight;
        }
    }

    private void computeSize() {
    }

    public void setOnPadGridListener(OnPadGridListener l) {
        listener = l;

    }

    public interface OnPadGridListener {
        void onChange(int localIndex, boolean selected);

        void onLongPress(Integer localIndex, float x, float y);

    }
}
