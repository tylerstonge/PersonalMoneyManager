package edu.oswego.tygama344;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Test
    public void testOpensAddNewPurchaseActivity() {
        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(AddNewPurchaseActivity.class.getName(), null, false);

        final Button button = (Button) settingsActivity.findViewById(R.id.actionSettings);
        settingsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        SettingsActivity settingsActivity = (SettingsActivity) getInstrumentation().waitForMonitorWithTimeout(am, 5000);
        assertNotNull(settingsActivity);
        settingsActivity.finish();
    }

}
