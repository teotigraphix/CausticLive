
package com.teotigraphix.causticlive.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class ComponentFactory {

    public static Label createLabel(Skin skin, String text, String fontName, Color color) {
        Label label = new Label(text, new LabelStyle(skin.getFont(fontName), color));
        return label;
    }

}
