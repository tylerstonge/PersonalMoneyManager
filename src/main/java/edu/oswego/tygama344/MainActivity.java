package edu.oswego.tygama344;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

    static final int ADD_NEW_PURCHASE_REQUEST = 1;
    static final int CONTEXT_REMOVE_PURCHASE = 1;

    MySQLiteHelper db;
    PurchaseAdapter adapter;
    ListView lv;

    Button addButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lv = (ListView) findViewById(R.id.historyListView);

        // Populate list with information from database
        db = new MySQLiteHelper(this);
        adapter = new PurchaseAdapter(this, R.layout.item_purchase, db.getAllPurchases());
        lv.setAdapter(adapter);
        registerForContextMenu(lv);

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
                startActivity(i1);
                return true;
            case R.id.actionInfo:
                Intent i2 = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String title = (adapter.getItem(info.position)).getName();
        menu.setHeaderTitle(title);
        menu.add(Menu.NONE, CONTEXT_REMOVE_PURCHASE, CONTEXT_REMOVE_PURCHASE, "Remove");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case CONTEXT_REMOVE_PURCHASE:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                int id = (adapter.getItem(info.position)).getId();
                db.removePurchase(id);
                adapter.replaceList(db.getAllPurchases());
                return true;
        }
        return super.onContextItemSelected(item);
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
                // Create purchase object
                String name = data.getStringExtra("name");
                double amount = data.getDoubleExtra("amount", 0.0);
                // Store the new purchase object
                db.insertPurchase(new Purchase(name, amount));
                adapter.replaceList(db.getAllPurchases());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.replaceList(db.getAllPurchases());
    }
}
