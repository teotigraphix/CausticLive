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

package com.teotigraphix.causticlive.service;

import java.io.File;
import java.io.IOException;

import com.teotigraphix.caustic.core.CausticException;

/**
 * @author Michael Schmalle
 */
public interface IArchiveService {

    /**
     * Parses the <code>source</code> directory for all <code>.caustic</code>
     * files and will transform them into <code>.clive</code> archives.
     * <p>
     * This method is not recursive, it is up to the client to recurse and
     * manage sub directories.
     * 
     * @param source The source directory where .caustic files will be
     *            transformed.
     * @param target The target directory where the archived .clive files will
     *            be output.
     * @throws CausticException
     * @throws IOException
     */
    void parse(File source, File target) throws CausticException, IOException;

}
