package edu.oswego.tygama344;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.database.DataSetObserver;
import android.widget.ListView;
import android.widget.Button;
import android.support.test.runner.AndroidJUnit4;
import android.app.Instrumentation.ActivityMonitor;
import android.app.Activity;
import android.test.UiThreadTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

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
        super("edu.oswego.tygama344", MainActivity.class);
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

    @UiThreadTest
    public void testViewContainsAddNewButton() {
	final Button addNewButton = (Button) mainActivity.findViewById(R.id.addNewButton);
	assertNotNull(addNewButton);
    }

    @UiThreadTest
    public void testPopulateList() {
	final ListView lv = (ListView) mainActivity.findViewById(R.id.historyListView);
	lv.getAdapter().registerDataSetObserver(new DataSetObserver() {
		@Override
		public void onChanged() {
			assertTrue(lv.getCount() > 0);
		}
	});
	getActivity().populateListView(new String[] { "Gas", "Groceries" });
    }
}
