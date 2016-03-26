package edu.oswego.tygama344;


import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class edu.oswego.tygama344.MainActivityTest \
 * edu.oswego.tygama344.tests/android.test.InstrumentationTestRunner
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
    }

    @Test
    public void testActivityExists() {
        assertNotNull(mainActivity);
    }

    @Test
    public void testOpensAddNewPurchaseActivity() {
        ActivityMonitor am = getInstrumentation().addMonitor(AddNewPurchaseActivity.class.getName(), null, false);

        final Button button = (Button) mainActivity.findViewById(R.id.addNewButton);
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        AddNewPurchaseActivity addNewPurchaseActivity = (AddNewPurchaseActivity) getInstrumentation().waitForMonitorWithTimeout(am, 5000);
        assertNotNull(addNewPurchaseActivity);
        addNewPurchaseActivity.finish();
    }

    @Test
    public void testOpensSettingsActivity() {
        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Settings")).perform(click());

        SettingsActivity settingsActivity = (SettingsActivity) getInstrumentation().waitForMonitorWithTimeout(am, 5000);
        assertNotNull(settingsActivity);
        settingsActivity.finish();
    }

    @Test
    public void testViewContainsAddNewButton() {
        final Button addNewButton = (Button) mainActivity.findViewById(R.id.addNewButton);
        assertNotNull(addNewButton);
    }
}
