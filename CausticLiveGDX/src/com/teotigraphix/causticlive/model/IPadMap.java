package com.teotigraphix.causticlive.model;

import java.util.List;

import com.teotigraphix.causticlive.model.vo.PadData;

public interface IPadMap {

    PadData getPad(int bank, int localIndex);

    List<PadData> getPads(int bank);
}
