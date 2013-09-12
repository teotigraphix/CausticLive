
package com.teotigraphix.causticlive.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.teotigraphix.causticlive.view.components.PadButton.PadButtonStyle;
import com.teotigraphix.libgdx.ui.OldSelectButton.SelectButtonStyle;

public class SkinRegistry {

    public static void register(Skin skin) {

        skin.add("green", new Color(0, 1, 0, 1));
        skin.add("white", new Color(1, 1, 1, 1));
        skin.add("red", new Color(1, 0, 0, 1));
        skin.add("black", new Color(0, 0, 0, 1));

        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("skin/default.fnt"), false);
        skin.add("default-font", bitmapFont);

        LabelStyle labelStyle = new LabelStyle(skin.getFont("default-font"), skin.getColor("white"));
        skin.add("default", labelStyle);

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("pad_up");
        textButtonStyle.down = skin.getDrawable("pad_selected");
        textButtonStyle.checked = skin.getDrawable("pad_selected");
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.fontColor = skin.getColor("white");
        skin.add("default", textButtonStyle);

        ListStyle listStyle = new ListStyle();
        listStyle.selection = skin.getDrawable("list_select_bg");
        listStyle.font = skin.getFont("default-font");
        listStyle.fontColorSelected = skin.getColor("white");
        listStyle.fontColorUnselected = skin.getColor("white");
        skin.add("default", listStyle);

        ScrollPaneStyle scrollPaneStyle = new ScrollPaneStyle();
        scrollPaneStyle.vScroll = skin.getDrawable("list_select_bg");
        scrollPaneStyle.hScroll = skin.getDrawable("list_select_bg");
        scrollPaneStyle.vScrollKnob = skin.getDrawable("list_select_bg");
        scrollPaneStyle.hScrollKnob = skin.getDrawable("list_select_bg");
        scrollPaneStyle.background = skin.getDrawable("list_select_bg");
        skin.add("default", scrollPaneStyle);

        WindowStyle windowStyle = new WindowStyle();
        windowStyle.background = skin.getDrawable("default-rect");
        windowStyle.titleFont = skin.getFont("default-font");
        windowStyle.titleFontColor = skin.getColor("white");

        SelectButtonStyle selectButtonStyle = new SelectButtonStyle();
        selectButtonStyle.up = skin.getDrawable("pad_up");
        selectButtonStyle.down = skin.getDrawable("pad_selected");
        selectButtonStyle.checked = skin.getDrawable("pad_selected");
        selectButtonStyle.font = skin.getFont("default-font");
        selectButtonStyle.fontColor = skin.getColor("white");
        selectButtonStyle.checkedFontColor = skin.getColor("red");
        selectButtonStyle.disabledFontColor = skin.getColor("white");
        skin.add("default", selectButtonStyle);

        //
        PadButtonStyle padButtonStyle = new PadButtonStyle();
        padButtonStyle.up = skin.getDrawable("pad_up");
        padButtonStyle.down = skin.getDrawable("pad_selected");
        padButtonStyle.checked = skin.getDrawable("pad_selected");
        padButtonStyle.font = skin.getFont("default-font");
        padButtonStyle.fontColor = skin.getColor("white");

        padButtonStyle.queueOverlay = skin.getDrawable("overlay_queued");
        padButtonStyle.playOverlay = skin.getDrawable("overlay_play");
        padButtonStyle.lockOverlay = skin.getDrawable("pad_selected");

        skin.add("default", padButtonStyle);
    }
}
