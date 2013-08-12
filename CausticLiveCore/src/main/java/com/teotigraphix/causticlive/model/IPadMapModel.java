
package com.teotigraphix.causticlive.model;

import java.util.Collection;

import com.teotigraphix.caustic.model.ICaustkModel;

public interface IPadMapModel extends ICaustkModel, IPadMap {

    Collection<PadData> getPads();

}
