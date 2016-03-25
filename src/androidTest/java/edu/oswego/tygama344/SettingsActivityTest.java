package edu.oswego.tygama344;


import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class SettingsActivityTest extends ActivityInstrumentationTestCase2<SettingsActivity> {
    private SettingsActivity settingsActivity;

    public SettingsActivityTest() {
        super(SettingsActivity.class);
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
    @UiThreadTest
    public void testSubmitDisabledWithEmptyTextFields() {
        final Button button = (Button) settingsActivity.findViewById(R.id.button);
        assertFalse(button.isEnabled());
    }

/*    @Test
    @UiThreadTest
    public void testSubmitEnabledWithFilledTextFields() {
        final Button button = (Button) settingsActivity.findViewById(R.id.button);
        Espresso.onView(ViewMatchers.withId(R.id.payperiod)).perform(ViewActions.typeText("14"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.household)).perform(ViewActions.typeText("3"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.income)).perform(ViewActions.typeText("2000"), ViewActions.closeSoftKeyboard());
        assertTrue(button.isEnabled());
    }*/

    @Test
    @UiThreadTest
    public void testSwitch() {
        onView(ViewMatchers.withId(R.id.stats)).check(matches((isChecked())));
        onView(withId(R.id.stats)).perform(click());
        onView(withId(R.id.stats)).check(matches(not(isChecked())));
    }
}
