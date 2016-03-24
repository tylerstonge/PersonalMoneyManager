package edu.oswego.tygama344;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.Switch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest extends ActivityInstrumentationTestCase2<SettingsActivity> {
    private SettingsActivity settingsActivity;

    public SettingsActivityTest() {
        super("edu.oswego.tygama344", SettingsActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        settingsActivity = getActivity();
    }

    @Test
    public void testActivityExists() {
        assertNotNull(settingsActivity);
    }

    @UiThreadTest
    public void testOpensSettingsActivity() {
        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(SettingsActivity.class.getName(), null, false);

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withId(R.id.actionSettings)).perform(click());

        SettingsActivity settingsActivity = (SettingsActivity) getInstrumentation().waitForMonitorWithTimeout(am, 5000);
        assertNotNull(settingsActivity);
        settingsActivity.finish();
    }

}
