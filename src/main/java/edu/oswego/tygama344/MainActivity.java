package edu.oswego.tygama344;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends Activity {
    
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
	String[] items = new String[] { "Gas", "Power", "Groceries" };
	populateListView(items);
	
	button = (Button) findViewById(R.id.addNewButton);
	button.setOnClickListener(new View.OnClickListener(){
		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(),AddNewPurchaseActivity.class);
			startActivity(i);
		}
	});
    }

    /**
     *	Sets the information the ListView will display
     *
     *	@param items the String[] of items to display
     */
    public void populateListView(String[] items) {
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items);
	lv.setAdapter(adapter);
    }
}
