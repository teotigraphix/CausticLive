
package com.teotigraphix.causticlive.test;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.teotigraphix.android.service.ITouchService;
import com.teotigraphix.causticlive.MainActivity;
import com.teotigraphix.causticlive.R;

public class TestFoo extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;

    public TestFoo() {
        super(MainActivity.class);

    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();

        // ActivityUnitTestCase - This class provides isolated testing of a single activity. 

        //MoreAsserts;
        //ViewAsserts;

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMainLayoutGetsRegistered() {
        RoboInjector injector = RoboGuice.getInjector(activity);
        ITouchService service = injector.getInstance(ITouchService.class);

        View view = activity.findViewById(R.id.main_layout);
        assertNotNull(view);
        assertTrue(service.hasTouchListener(view));
    }

    public void testInit() {

        // get a view

        // onDestory()
        //activity.finish();

        // restarts the Activity - onResume()
        //activity = getActivity();
    }
}
