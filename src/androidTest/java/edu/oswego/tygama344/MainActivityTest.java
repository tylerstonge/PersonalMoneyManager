package edu.oswego.tygama344;

import android.test.ActivityInstrumentationTestCase2;
import android.database.DataSetObserver;
import android.widget.ListView;
import android.support.test.runner.AndroidJUnit4;
import android.test.UiThreadTest;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    public MainActivityTest() {
        super("edu.oswego.tygama344", MainActivity.class);
    }

    @Test
    public void testActivityExists() {
	MainActivity mainActivity = getActivity();
	assertNotNull(mainActivity);
    }

    @UiThreadTest
    public void testPopulateList() {
	final ListView lv = (ListView) getActivity().findViewById(R.id.historyListView);
	lv.getAdapter().registerDataSetObserver(new DataSetObserver() {
		@Override
		public void onChanged() {
			assertTrue(lv.getCount() > 0);
		}
	});
	getActivity().populateListView(new String[] { "Gas", "Groceries" });
    } 
}
