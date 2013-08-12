package com.teotigraphix.causticlive.model;

import java.util.List;

public interface IPadMap {

    PadData getPad(int bank, int localIndex);

    List<PadData> getPads(int bank);
}
