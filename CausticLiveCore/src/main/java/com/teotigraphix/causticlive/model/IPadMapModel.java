
package com.teotigraphix.causticlive.model;

import java.util.List;

import com.teotigraphix.caustic.model.ICaustkModel;

public interface IPadMapModel extends ICaustkModel {

    PadData getPad(int bank, int localIndex);

    List<PadData> getPads(int bank);

}
