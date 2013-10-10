
package com.teotigraphix.causticlive.view.main.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.ui.OverlayButton.OnPadButtonListener;

public class PadButton extends TextButton {

    //--------------------------------------------------------------------------
    // Public Property :: API
    //--------------------------------------------------------------------------

    //----------------------------------
    // properties
    //----------------------------------

    Map<String, Object> properties;

    public final Map<String, Object> getProperties() {
        if (properties == null)
            properties = new HashMap<String, Object>();
        return properties;
    }

    //----------------------------------
    // queueData
    //----------------------------------

    private QueueData queueData;

    public QueueData getData() {
        return queueData;
    }

    public void setData(QueueData value) {
        queueData = value;
    }

    public boolean isSelected() {
        return false;
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public PadButton(String text, Skin skin) {
        super(text, skin);
    }

    public PadButton(String text, TextButtonStyle style) {
        super(text, style);
    }

    public PadButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    @SuppressWarnings("unused")
    private OnPadButtonListener listener;

    public void setOnPadButtonListener(OnPadButtonListener l) {
        this.listener = l;
    }

}
