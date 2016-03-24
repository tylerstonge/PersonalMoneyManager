package edu.oswego.tygama344;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    static final int ADD_NEW_PURCHASE_REQUEST = 1;
    static final int SETTINGS_REQUEST = 2;

    PurchaseAdapter adapter;
    ArrayList<Purchase> items;

    ListView lv;
    Button addButton;

    /**
     * Called when the activity is first created.
     */
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
        addButton = (Button) findViewById(R.id.addNewButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddNewPurchaseActivity.class);
                startActivityForResult(i, ADD_NEW_PURCHASE_REQUEST);
            }
        });
    }

    /**
     * Override method to add items to ActionBar menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_activity_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Button selected from ActionBar menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Process options
        switch (item.getItemId()) {
            case R.id.actionSettings:
                Intent i1 = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(i1, SETTINGS_REQUEST);
                return true;
            case R.id.actionInfo:
                Intent i2 = new Intent(getApplicationContext(), InfoActivity.class);
                startActivityForResult(i2, SETTINGS_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Receives data from any activities started from MainActivity for result
     *
     * @param requestCode the id which determines which activity is calling the method
     * @param resultCode  either RESULT_OK or RESULT_CANCELED; whether the activity closed with a result or not
     * @param data        the data to be received
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
     * Sets the information the ListView will display
     *
     * @param items An ArrayList of items to display
     */
    public void populateListView(ArrayList<Purchase> items) {
        adapter = new PurchaseAdapter(this, R.layout.item_purchase, items);
        lv.setAdapter(adapter);
    }
}
