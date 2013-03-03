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

package com.teotigraphix.causticlive.internal.service;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.causticlive.service.IArchiveService;

/*

What is this service going to do?

- Save a zip file with a .caustic and XML manifest entry.
- Use an add, remove, get API

- Ability to parse a directory of .caustic files and transform them into
  .clive format
  - parse(File source, File target)

CausticLiveFile
  - mainfest:IMemento - a loaded memento of the manifest
  - file:File - absolute path to the unpacked file

*/

public class ArchiveService implements IArchiveService {

    public ArchiveService() {
        // TODO Auto-generated constructor stub
    }

    public void parse(File source, File target) throws CausticException, IOException {

    }

    public static class CausticLiveFile extends File {

        public CausticLiveFile(File dir, String name) {
            super(dir, name);

        }

    }
}
