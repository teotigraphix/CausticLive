
package com.teotigraphix.causticlive.view.components;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.teotigraphix.caustk.sequencer.queue.QueueData.QueueDataState;
import com.teotigraphix.libgdx.ui.GDXToggleButton;

public class PadButton extends GDXToggleButton {

    //----------------------------------
    // state
    //----------------------------------
    // Idle, Queued, Playing, Plsying/Lock (last measure)
    private QueueDataState state;

    public QueueDataState getState() {
        return state;
    }

    public void setState(QueueDataState value) {
        state = value;
        invalidate();
    }

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public PadButton(String text, Skin skin) {
        super(text, skin);
    }

    public PadButton(String text, ButtonStyle style) {
        super(text, style);
    }

    public PadButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

}
