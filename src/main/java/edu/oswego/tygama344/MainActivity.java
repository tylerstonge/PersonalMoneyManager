package edu.oswego.tygama344;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.content.Intent;

import java.util.ArrayList;

public class MainActivity extends Activity {

    static final int ADD_NEW_PURCHASE_REQUEST = 1;

    PurchaseAdapter adapter;
    ArrayList<Purchase> items;
    
    ListView lv;	
    Button button;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	// Get a handle to the ListView 
	lv = (ListView) findViewById(R.id.historyListView);

	// Dummy values to populate listview
	items = new ArrayList<Purchase>();
	populateListView(items);
	
	// Button listener
	button = (Button) findViewById(R.id.addNewButton);
	button.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(), AddNewPurchaseActivity.class);
			startActivityForResult(i, ADD_NEW_PURCHASE_REQUEST);
		}
	});
    }

    /**
     *	Receives data from any activities started from MainActivity for result
     *
     *	@param requestCode the id which determines which activity is calling the method 
     *	@param resultCode either RESULT_OK or RESULT_CANCELED; whether the activity closed with a result or not
     *	@param data the data to be received
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (requestCode == ADD_NEW_PURCHASE_REQUEST) {
		if (resultCode == RESULT_OK) {
			String name = data.getStringExtra("name");
			double amount = data.getDoubleExtra("amount", 0.0);
			Purchase purchase = new Purchase(name, amount);
			items.add(purchase);
			adapter.notifyDataSetChanged();
		}
	}
    }

    /**
     *	Sets the information the ListView will display
     *
     *	@param items An ArrayList of items to display
     */
    public void populateListView(ArrayList<Purchase> items) {
	adapter = new PurchaseAdapter(this, R.layout.item_purchase, items);
	lv.setAdapter(adapter);
    }
}
