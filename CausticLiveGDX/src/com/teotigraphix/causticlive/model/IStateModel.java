
package com.teotigraphix.causticlive.model;

import java.util.UUID;

import com.teotigraphix.causticlive.model.state.SequencerModelState;

public interface IStateModel {

    SequencerModelState getSequencerModelState();

    UUID getSelectedSceneId();

    void setSelectedSceneId(UUID id);

}
