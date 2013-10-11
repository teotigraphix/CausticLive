////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.causticlive.model;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.utils.Array;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface ILibraryModel extends ICaustkModel {

    void importSong(File file) throws IOException, CausticException;

    Array<LibraryScene> getScenes();

    public static class OnLibraryModelLibraryChange {
    }

    void restoreState() throws CausticException;

    void assignTone(int toneIndex, QueueData queueData);

}
