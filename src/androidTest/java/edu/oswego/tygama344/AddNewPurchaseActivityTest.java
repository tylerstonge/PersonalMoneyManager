package edu.oswego.tygama344;

import android.support.annotation.UiThread;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class AddNewPurchaseActivityTest extends ActivityInstrumentationTestCase2<AddNewPurchaseActivity> {

    private AddNewPurchaseActivity addNewPurchaseActivity;

    public AddNewPurchaseActivityTest() {
        super("edu.oswego.tygama344", AddNewPurchaseActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        addNewPurchaseActivity = getActivity();
    }

    @Test
    public void testActivityExists() {
        assertNotNull(addNewPurchaseActivity);
    }

    @Test
    public void testReturnToMainOnCancel() {
        final Button button = (Button) addNewPurchaseActivity.findViewById(R.id.cancelButton);
        addNewPurchaseActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.performClick();
            }
        });

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            // Do nothing
        }
        assertTrue(addNewPurchaseActivity.isFinishing());
    }

    @UiThreadTest
    public void testAmountEditTextOnlyAcceptsNumbers() {
        // Test inserting the character "A", which should not show up in EditText
        Espresso.onView(ViewMatchers.withId(R.id.amountEditText)).perform(ViewActions.typeText("A"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.amountEditText)).check(ViewAssertions.matches(ViewMatchers.withText("")));
        // Test inserting the number "1", which should show up in the EditText
        Espresso.onView(ViewMatchers.withId(R.id.amountEditText)).perform(ViewActions.typeText("1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.amountEditText)).check(ViewAssertions.matches(ViewMatchers.withText("1")));
    }

    @UiThreadTest
    public void testSubmitDisabledWithEmptyTextFields() {
        final Button button = (Button) addNewPurchaseActivity.findViewById(R.id.submitButton);
        assertFalse(button.isEnabled());
    }

    @UiThreadTest
    public void testSubmitEnabledWithFilledTextFields() {
        final Button button = (Button) addNewPurchaseActivity.findViewById(R.id.submitButton);
        Espresso.onView(ViewMatchers.withId(R.id.amountEditText)).perform(ViewActions.typeText("100.00"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.nameEditText)).perform(ViewActions.typeText("Groceries"), ViewActions.closeSoftKeyboard());
        assertTrue(button.isEnabled());
    }

    @UiThreadTest
    public void testViewContainsCancelButton() {
        final Button cancelButton = (Button) addNewPurchaseActivity.findViewById(R.id.cancelButton);
        assertNotNull(cancelButton);
    }

    @UiThreadTest
    public void testViewContainsSubmitButton() {
        final Button submitButton = (Button) addNewPurchaseActivity.findViewById(R.id.submitButton);
        assertNotNull(submitButton);
    }

    @UiThreadTest
    public void testViewContainsNameEditText() {
        final EditText nameEditText = (EditText) addNewPurchaseActivity.findViewById(R.id.nameEditText);
        assertNotNull(nameEditText);
    }

    @UiThreadTest
    public void testViewContainsAmountEditText() {
        final EditText amountEditText = (EditText) addNewPurchaseActivity.findViewById(R.id.amountEditText);
        assertNotNull(amountEditText);
    }
}
