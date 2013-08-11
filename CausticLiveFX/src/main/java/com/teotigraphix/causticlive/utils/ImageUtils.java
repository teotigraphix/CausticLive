
package com.teotigraphix.causticlive.utils;

import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.teotigraphix.caustk.tone.Tone;

public final class ImageUtils {

    public static void assignMachineIcon(Tone tone, ToggleButton button) {
        final ImageView imageView = new ImageView(getMachineImage(tone));
        imageView.setFitWidth(100);
        imageView.setFitHeight(70);
        button.setGraphic(imageView);
        button.setContentDisplay(ContentDisplay.TOP);
    }

    public static Image getMachineImage(Tone tone) {
        String name = tone.getToneType().name();
        Image result = new Image(ImageUtils.class.getClass().getResourceAsStream(
                "/images/" + name + "Assign.png"));
        return result;
    }

}
