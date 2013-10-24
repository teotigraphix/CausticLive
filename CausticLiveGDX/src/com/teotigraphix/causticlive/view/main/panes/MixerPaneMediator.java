
package com.teotigraphix.causticlive.view.main.panes;

import com.badlogic.gdx.scenes.scene2d.ui.PopUp;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.google.inject.Inject;
import com.teotigraphix.causticlive.view.main.components.PartMixer;
import com.teotigraphix.causticlive.view.main.components.PartMixer.OnPartMixerListener;
import com.teotigraphix.caustk.rack.mixer.SoundMixerChannel;
import com.teotigraphix.libgdx.controller.ScreenMediatorChild;
import com.teotigraphix.libgdx.dialog.IDialogManager;
import com.teotigraphix.libgdx.scene2d.IScreenProvider;
import com.teotigraphix.libgdx.screen.IScreen;
import com.teotigraphix.libgdx.ui.Knob;
import com.teotigraphix.libgdx.ui.Pane;
import com.teotigraphix.libgdx.ui.TextSlider;

public class MixerPaneMediator extends ScreenMediatorChild {

    @Inject
    IDialogManager dialogManager;

    @Inject
    IScreenProvider screenProvider;

    private PartMixer partMixer;

    private PopUp mixerDialog;

    private boolean updating;

    public MixerPaneMediator() {
    }

    @Override
    public void onCreate(IScreen screen, WidgetGroup parent) {
        super.onCreate(screen, parent);

        setupPane(screen.getSkin(), (Pane)parent);
    }

    private void setupPane(Skin skin, Pane pane) {
        createPartMixer();
        pane.add(partMixer).fill().expand();
    }

    protected void createPartMixer() {
        partMixer = new PartMixer(screenProvider.getScreen().getSkin());
        partMixer.setOnPartMixerListener(new OnPartMixerListener() {
            @Override
            public void onSliderChange(int buttonIndex, int sliderIndex, float sliderValue) {
                if (updating)
                    return;
                SoundMixerChannel channel = getController().getRack().getSoundMixer()
                        .getChannel(sliderIndex);
                switch (buttonIndex) {
                    case 0:
                        channel.setVolume(sliderValue);
                        break;

                    case 1:
                        channel.setReverbSend(sliderValue);
                        break;

                    case 2:
                        channel.setDelaySend(sliderValue);
                        break;

                    case 3:
                        channel.setStereoWidth(sliderValue);
                        break;
                }
            }

            @Override
            public void onButtonChange(int index) {
                updateSliders(index);
            }

            @Override
            public void onKnobChange(int buttonIndex, int sliderIndex, float value) {
                if (updating)
                    return;
                SoundMixerChannel channel = getController().getRack().getSoundMixer()
                        .getChannel(sliderIndex);
                channel.setPan(value);
            }

            @Override
            public void onMuteChange(int buttonIndex, int sliderIndex, boolean selected) {
                if (updating)
                    return;
                SoundMixerChannel channel = getController().getRack().getSoundMixer()
                        .getChannel(sliderIndex);
                channel.setMute(selected);
            }

            @Override
            public void onSoloChange(int buttonIndex, int sliderIndex, boolean selected) {
            }
        });

        mixerDialog = dialogManager
                .createPopUp(screenProvider.getScreen(), "Part Mixer", partMixer);
        mixerDialog.pad(5f);
        mixerDialog.padTop(25f);

        mixerDialog.getContentTable().add(partMixer).fill().expand();

        mixerDialog.setPosition(5f, 105f);
        mixerDialog.setExplicitSize(335f, 240f);
        mixerDialog.validate();

        updateSliders(0);
    }

    protected void updateSliders(int index) {
        updating = true;
        for (int i = 0; i < 6; i++) {
            SoundMixerChannel channel = getController().getRack().getSoundMixer().getChannel(i);
            TextSlider slider = partMixer.getSliderAt(i);
            Knob panKnob = partMixer.getKnobAt(i);
            panKnob.setValue(channel.getPan());

            if (channel != null) {
                switch (index) {
                    case 0:
                        getController().getLogger().log("ToolBarMediator", "Volume");
                        slider.setRange(0f, 2f);
                        slider.setValue(channel.getVolume());
                        break;
                    case 1:
                        getController().getLogger().log("ToolBarMediator", "Reverb");
                        slider.setRange(0f, 1f);
                        slider.setValue(channel.getReverbSend());
                        break;
                    case 2:
                        getController().getLogger().log("ToolBarMediator", "Delay");
                        slider.setRange(0f, 1f);
                        slider.setValue(channel.getDelaySend());
                        break;
                    case 3:
                        getController().getLogger().log("ToolBarMediator", "Width");
                        slider.setRange(0f, 1f);
                        slider.setValue(channel.getStereoWidth());
                        break;
                }
            }
        }
        updating = false;
    }

}
