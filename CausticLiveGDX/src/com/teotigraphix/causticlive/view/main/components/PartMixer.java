
package com.teotigraphix.causticlive.view.main.components;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.teotigraphix.libgdx.ui.ButtonBar;
import com.teotigraphix.libgdx.ui.ButtonBar.OnButtonBarListener;
import com.teotigraphix.libgdx.ui.ControlTable;

public class PartMixer extends ControlTable {

    private String[] items = new String[] {
            "Volume", "Pan", "Reverb", "Delay", "Stereo"
    };

    private ButtonBar buttonBar;

    public PartMixer(Skin skin) {
        super(skin);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    private List<Slider> sliders = new ArrayList<Slider>();

    private OnPartMixerListener listener;

    private int selectedIndex;

    protected boolean blockEvents;

    @Override
    protected void createChildren() {
        super.createChildren();

        // column 1 volume, pan, reverb, delay, stereo

        buttonBar = new ButtonBar(getSkin(), items, true, "default");
        buttonBar.setOnButtonBarListener(new OnButtonBarListener() {
            @Override
            public void onChange(int index) {
                blockEvents = true;
                selectedIndex = index;
                listener.onButtonChange(index);
                blockEvents = false;
            }
        });
        add(buttonBar);

        for (int i = 0; i < 6; i++) {
            final int index = i;
            final Slider slider = new Slider(0, 2, 0.01f, true, getSkin());
            slider.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (blockEvents)
                        return;
                    int buttonIndex = selectedIndex;
                    int sliderIdnex = index;
                    float sliderValue = slider.getValue();
                    listener.onSliderChange(buttonIndex, sliderIdnex, sliderValue);
                }
            });
            add(slider).width(40f).expandY().fillY();
            sliders.add(slider);
        }

        setWidth(getPrefWidth() + 100);
        setHeight(getPrefHeight() + 100);
    }

    public Slider getSliderAt(int index) {
        return sliders.get(index);
    }

    public void setOnPartMixerListener(OnPartMixerListener l) {
        this.listener = l;
    }

    public interface OnPartMixerListener {
        void onButtonChange(int index);

        void onSliderChange(int buttonIndex, int sliderIndex, float sliderValue);
    }
}
