
package com.teotigraphix.causticlive.model;

import java.util.Collection;

import com.teotigraphix.causticlive.model.vo.PadData;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface IPadMapModel extends ICaustkModel, IPadMap {

    Collection<PadData> getPads();

}
