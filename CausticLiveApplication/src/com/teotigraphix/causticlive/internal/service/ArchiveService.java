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

import com.google.inject.Inject;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.service.IFileService;
import com.teotigraphix.caustic.song.IWorkspace;
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

    @Inject
    IFileService fileService;

    @Inject
    IWorkspace worksapce;

    public ArchiveService() {
        //Compress c;
        //Decompress d;
    }

    @Override
    public void parse(File source, File target) throws CausticException, IOException {
        // loop through the source directory non recursive
        File[] files = source.listFiles();
        for (File file : files) {
            if (file.isDirectory())
                continue;
            archive(file, target);
        }
    }

    private void archive(File song, File target) throws CausticException {
        // create a temp file
        // save the .caustic file to temp
        // create the manifest XML save it to temp
        //IRackSong rackSong = worksapce.getRack().loadSong(song.getAbsolutePath());

        // machines

        // machine patterns

        // Patch - 
    }

    public static class CausticLiveFile {

        public CausticLiveFile(File dir, String name) {

        }

    }
}
