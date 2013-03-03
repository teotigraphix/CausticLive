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

package com.teotigraphix.causticlive.internal.application;

import java.io.File;
import java.io.IOException;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;
import android.test.ActivityInstrumentationTestCase2;

import com.teotigraphix.android.components.support.MainLayout;
import com.teotigraphix.android.service.ITouchService;
import com.teotigraphix.caustic.activity.IApplicationConfig;
import com.teotigraphix.caustic.controller.IApplicationController;
import com.teotigraphix.caustic.controller.IApplicationPreferences;
import com.teotigraphix.caustic.controller.OSCMessage;
import com.teotigraphix.caustic.core.CausticException;
import com.teotigraphix.caustic.internal.command.project.LoadProjectCommand;
import com.teotigraphix.caustic.internal.command.project.RestoreProjectCommand;
import com.teotigraphix.caustic.internal.command.project.SaveProjectAsCommand;
import com.teotigraphix.caustic.internal.command.project.SaveProjectCommand;
import com.teotigraphix.caustic.internal.command.startup.RegisterMainLayoutCommand;
import com.teotigraphix.caustic.internal.command.workspace.StartupWorkspaceCommand;
import com.teotigraphix.caustic.internal.command.workspace.WorkspaceSaveQuickCommand;
import com.teotigraphix.caustic.internal.router.Router;
import com.teotigraphix.caustic.internal.song.Workspace;
import com.teotigraphix.caustic.song.IProject;
import com.teotigraphix.caustic.song.IWorkspace;
import com.teotigraphix.causticlive.MainActivity;
import com.teotigraphix.causticlive.R;
import com.teotigraphix.causticlive.config.CausticLiveProjectData;
import com.teotigraphix.common.utils.RuntimeUtils;

public class ApplicationCommandTests extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String APPLICATION = "application";

    private MainActivity activity;

    private RoboInjector injector;

    private Router router;

    private Workspace workspace;

    public ApplicationCommandTests() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // XXX HACK!
        IApplicationConfig.Test.TEST_MODE = true;

        activity = getActivity();
        injector = RoboGuice.getInjector(activity);

        workspace = (Workspace)injector.getInstance(IWorkspace.class);

        router = injector.getInstance(Router.class);
        router.setName("controller");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // controller/application/register_main_layout [resourceId]
    /**
     * Registers the MainLayout instance with the touch service.
     */
    public void testRegisterMainLayout_Command() throws CausticException {
        addCommand(IApplicationController.REGISTER_MAIN_LAYOUT, RegisterMainLayoutCommand.class);

        router.sendCommand(message(IApplicationController.REGISTER_MAIN_LAYOUT, R.id.main_layout));

        MainLayout view = (MainLayout)activity.findViewById(R.id.main_layout);
        assertNotNull(view);
        assertSame(injector.getInstance(ITouchService.class), view.getTouchService());
    }

    // controller/application/startup_workspace
    /**
     * Starts and runs the workspace independently.
     */
    public void testStartupWorkspace_Command() throws CausticException {

        assertFalse(workspace.isRunning());

        //assertSame(activity, workspace.getActivity());
        assertNotNull(workspace.getGenerator());
        assertNotNull(workspace.getRack());
        assertNotNull(workspace.getPreferences());

        assertNull(workspace.getApplicationRoot());
        assertNull(workspace.getProject());
        assertNull(workspace.getProperties());

        addCommand(IApplicationController.START_WORKSPACE, StartupWorkspaceCommand.class);
        router.sendCommand(message(IApplicationController.START_WORKSPACE));

        assertNotNull(workspace.getApplicationRoot());
        assertNotNull(workspace.getProperties());
        assertNotNull(workspace.getPreferences());

        // project is not loaded until loadProject()
        assertNull(workspace.getProject());

        assertTrue(workspace.isRunning());
    }

    // - need to test a project path that dosn't exists 'project.isNew'
    // - need to test a project path that exists temp (xml) and it loads
    //   and checks, remember we are in functional unit tests right here
    //   so the whole load project needs to be tested, could break this up
    //   in separate methods

    public void testLoadProject_Command_NonExists() throws CausticException {
        addCommand(IApplicationController.START_WORKSPACE, StartupWorkspaceCommand.class);
        addCommand(IApplicationController.LOAD_PROJECT, LoadProjectCommand.class);

        // the workspace needs to be started before we can access project specific
        router.sendCommand(message(IApplicationController.START_WORKSPACE));

        // test non existent project that will be put into memory
        File newFile = workspace.getFileService().getProjectFile("FooBar.xml");
        assertFalse(newFile.exists());

        router.sendCommand(message(IApplicationController.LOAD_PROJECT, newFile.getAbsolutePath()));

        IProject project = workspace.getProject();
        assertNotNull(project);
        assertTrue(project.getData() instanceof CausticLiveProjectData);
        assertTrue(project.getData().isNewProject());

        assertEquals("FooBar", project.getFileName());
        assertEquals(newFile, project.getFile());
        assertSame(workspace, project.getWorkspace());

        assertSame(workspace.getProject(), project);
    }

    public void testLoadProject_Command_Exists() throws CausticException, IOException {
        // XXX if you want to test the sate of the project XML, do that as a unit
        // test, the verifies that the process of loading an existing project
        // file works correctly, does not check that state is being restored right
        addCommand(IApplicationController.START_WORKSPACE, StartupWorkspaceCommand.class);
        addCommand(IApplicationController.LOAD_PROJECT, LoadProjectCommand.class);

        // the workspace needs to be started before we can access project specific
        router.sendCommand(message(IApplicationController.START_WORKSPACE));

        // test existent project that will be put into memory
        File newFile = createMockProjectFile(workspace, "FooBar.xml");

        router.sendCommand(message(IApplicationController.LOAD_PROJECT, newFile.getAbsolutePath()));

        IProject project = workspace.getProject();
        assertNotNull(project);
        assertFalse(project.isRestored());
        assertFalse(project.getData().isNewProject());
        assertTrue(project.getData().isSavedProject());
        assertEquals("Untitled Project", project.getName());

        assertTrue(newFile.delete());
    }

    public void testRestoreProject_Command() throws CausticException, IOException {
        addCommand(IApplicationController.START_WORKSPACE, StartupWorkspaceCommand.class);
        addCommand(IApplicationController.LOAD_PROJECT, LoadProjectCommand.class);
        addCommand(IApplicationController.RESTORE_PROJECT, RestoreProjectCommand.class);

        router.sendCommand(message(IApplicationController.START_WORKSPACE));

        File newFile = createMockProjectFile(workspace, "FooBar.xml");
        router.sendCommand(message(IApplicationController.LOAD_PROJECT, newFile.getAbsolutePath()));
        router.sendCommand(message(IApplicationController.RESTORE_PROJECT));

        IProject project = workspace.getProject();
        assertTrue(project.isRestored());
        // assert that we are indeed restoring from XML
        assertEquals("Foo Project", project.getName());

        assertTrue(newFile.delete());
    }

    public void testSaveProject_Command() throws CausticException, IOException {
        addCommand(IApplicationController.START_WORKSPACE, StartupWorkspaceCommand.class);
        addCommand(IApplicationController.LOAD_PROJECT, LoadProjectCommand.class);
        addCommand(IApplicationController.RESTORE_PROJECT, RestoreProjectCommand.class);
        addCommand(IApplicationController.SAVE_PROJECT, SaveProjectCommand.class);

        router.sendCommand(message(IApplicationController.START_WORKSPACE));

        File newFile = createMockProjectFile(workspace, "FooBar.xml");
        router.sendCommand(message(IApplicationController.LOAD_PROJECT, newFile.getAbsolutePath()));
        router.sendCommand(message(IApplicationController.RESTORE_PROJECT));

        IProject project = workspace.getProject();
        assertTrue(project.isRestored());
        assertEquals("Foo Project", project.getName());

        // change the name
        project.setName("Changed Name");
        // save the project
        router.sendCommand(message(IApplicationController.SAVE_PROJECT));

        router.sendCommand(message(IApplicationController.LOAD_PROJECT, newFile.getAbsolutePath()));
        router.sendCommand(message(IApplicationController.RESTORE_PROJECT));
        // this old project is stale and 'closed'
        assertFalse(project.isValid());

        project = workspace.getProject();
        // the instance of the project when loaded
        assertTrue(project.isValid());
        // we have saved the new state to disk and restored it successfully
        assertEquals("Changed Name", project.getName());

        assertTrue(newFile.delete());
    }

    public void testSaveProjectAs_Command() throws CausticException, IOException {
        // the saveAs will just replace the internal File pointer in the project and
        // call save(), the Project instance is the same instance
        addCommand(IApplicationController.START_WORKSPACE, StartupWorkspaceCommand.class);
        addCommand(IApplicationController.LOAD_PROJECT, LoadProjectCommand.class);
        addCommand(IApplicationController.RESTORE_PROJECT, RestoreProjectCommand.class);
        addCommand(IApplicationController.SAVE_PROJECT_AS, SaveProjectAsCommand.class);

        router.sendCommand(message(IApplicationController.START_WORKSPACE));

        File newFile = createMockProjectFile(workspace, "FooBar.xml");
        router.sendCommand(message(IApplicationController.LOAD_PROJECT, newFile.getAbsolutePath()));
        IProject project = workspace.getProject();

        router.sendCommand(message(IApplicationController.RESTORE_PROJECT));
        assertEquals("FooBar", project.getFileName());

        project = workspace.getProject();
        router.sendCommand(message(IApplicationController.SAVE_PROJECT_AS, "FooGoo.xml"));
        assertEquals("FooGoo", project.getFileName());

        File savedFile = workspace.getFileService().getProjectFile("FooGoo.xml");
        assertTrue(savedFile.exists());
        assertTrue(newFile.exists());

        assertTrue(newFile.delete());
        assertTrue(savedFile.delete());
    }

    public void testWorkspaceQuickSave_Command() throws CausticException, IOException {
        // the saveAs will just replace the internal File pointer in the project and
        // call save(), the Project instance is the same instance
        IApplicationPreferences preferences = injector.getInstance(IApplicationPreferences.class);

        addCommand(IApplicationController.START_WORKSPACE, StartupWorkspaceCommand.class);
        addCommand(IApplicationController.LOAD_PROJECT, LoadProjectCommand.class);
        addCommand(IApplicationController.WORKSPACE_SAVE_QUICK, WorkspaceSaveQuickCommand.class);

        assertNull(preferences.getQuickSaveFile());
        assertNull(preferences.getLastProjectFile());

        // runtime.boot() sets the lastProjectFile and qucikSaveFile
        router.sendCommand(message(IApplicationController.START_WORKSPACE));

        File newFile = createMockProjectFile(workspace, "FooBar.xml");
        router.sendCommand(message(IApplicationController.LOAD_PROJECT, newFile.getAbsolutePath()));
        assertNotNull(preferences.getQuickSaveFile());

        router.sendCommand(message(IApplicationController.WORKSPACE_SAVE_QUICK));
        assertTrue(preferences.getQuickSaveFile().exists());

        assertTrue(newFile.delete());
        assertTrue(preferences.getQuickSaveFile().delete());
    }

    public void testWorkspaceShutdown_Command() throws CausticException, IOException {
        // TODO impl test
    }

    private File createMockProjectFile(IWorkspace workspace, String fileName) throws IOException {
        File file = workspace.getFileService().getProjectFile(fileName);
        String data = "<project name=\"Foo Project\" version=\"0.1\"></project>";
        RuntimeUtils.saveFile(file, data);
        assertTrue(file.exists());
        return file;
    }

    OSCMessage message(String control, Object... args) {
        return OSCMessage.create(APPLICATION, control, args);
    }

    protected void addCommand(String control, Class<?> command) {
        addCommand(APPLICATION, control, command);
    }

    protected void addCommand(String device, String control, Class<?> command) {
        String message = "/" + router.getName() + "/" + device + "/" + control;
        router.put(message, command);
    }
}
