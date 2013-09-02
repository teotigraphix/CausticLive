
package com.teotigraphix.causticlive.model;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface ILibraryModel extends ICaustkModel {

    void importSong(File file) throws IOException, CausticException;

}
