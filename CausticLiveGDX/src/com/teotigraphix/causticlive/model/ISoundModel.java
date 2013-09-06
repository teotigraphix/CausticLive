
package com.teotigraphix.causticlive.model;

import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface ISoundModel extends ICaustkModel {

    void loadScene(LibraryScene item);

    LibraryScene getLibraryScene();

}
