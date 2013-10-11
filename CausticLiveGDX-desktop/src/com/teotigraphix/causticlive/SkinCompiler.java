
package com.teotigraphix.causticlive;

import java.io.File;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class SkinCompiler {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Settings settings = new Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
        settings.filterMin = TextureFilter.Linear;
        settings.filterMag = TextureFilter.Linear;
        TexturePacker2.process(settings, new File(
                "C:/Users/Teoti/Documents/git/CausticLive/CausticLiveGDX-android/assets/images")
                .getPath(), "../CausticLiveGDX-android/assets", "game");
    }
}
