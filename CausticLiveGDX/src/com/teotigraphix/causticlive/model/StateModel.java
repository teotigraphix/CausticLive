
package com.teotigraphix.causticlive.model;

import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.state.SequencerModelState;
import com.teotigraphix.libgdx.model.CaustkModelBase;
import com.teotigraphix.libgdx.model.IApplicationModel;

@Singleton
public class StateModel extends CaustkModelBase implements IStateModel {

    @Inject
    IApplicationModel applicationModel;

    protected final CausticLiveApplicationState getState() {
        return applicationModel.getState(CausticLiveApplicationState.class);
    }

    @Override
    public final SequencerModelState getSequencerModelState() {
        return getState().getSequencerModelState();
    }

    @Override
    public UUID getSelectedSceneId() {
        return getState().getSelectedSceneId();
    }

    @Override
    public void setSelectedSceneId(UUID value) {
        getState().setSelectedSceneId(value);
    }

    public StateModel() {
    }

}
