package edu.oswego.tygama344;

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;

import junit.framework.TestResult;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by leo on 3/10/16.
 */
@RunWith(AndroidJUnit4.class)
public class AddNewPurchaseActivityTest  extends ActivityInstrumentationTestCase2<AddNewPurchaseActivity> {


    public AddNewPurchaseActivityTest() {
        super("edu.oswego.tygama344", AddNewPurchaseActivity.class);
    }

    @Test
    public void testActivityExists(){
        AddNewPurchaseActivity purchaseActivity = getActivity();
        assertNotNull(purchaseActivity);
    }

    @UiThreadTest
    public void testViewContainsCancelButton(){
        final Button cancelButton = (Button) getActivity().findViewById(R.id.cancelButton);
        assertNotNull(cancelButton);
    }

    @UiThreadTest
    public void testViewContainsSubmitButton(){
        final Button submitButton = (Button) getActivity().findViewById(R.id.submitButton);
        assertNotNull(submitButton);
    }
}
