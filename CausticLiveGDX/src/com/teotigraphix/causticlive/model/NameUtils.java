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

import java.util.UUID;

import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.sequencer.queue.QueueData;

public class NameUtils {

    public static String dataDisplayName(QueueData data) {
        String name = data.getName();
        if (name == null) {
            if (data.getViewChannelIndex() == -1)
                return "Unassigned";
            String result = data.getViewChannel().getTone().getName();
            UUID phraseId = data.getPhrase().getPhraseId();
            if (phraseId != null) {
                LibraryPhrase libraryPhrase = data.getRack().getController().getLibraryManager()
                        .getSelectedLibrary().findPhraseById(phraseId);
                if (libraryPhrase.getMetadataInfo().hasName())
                    return libraryPhrase.getMetadataInfo().getName();
            }
            // result = result + ":" + PatternUtils.toString(getPhrase());
            return result;
        }
        return name;
    }

}
