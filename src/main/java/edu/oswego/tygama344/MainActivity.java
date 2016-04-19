package edu.oswego.tygama344;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends Activity {

    static final int ADD_NEW_PURCHASE_REQUEST = 1;
    static final int SAVE_SETTINGS_REQUEST = 2;

    // Current app settings
    int payperiod;
    int household;
    int income;

    MySQLiteHelper db;

    Button addButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Load stored settings if exist
        loadSettings();

        // Database
        db = new MySQLiteHelper(this);

        // Button listener
        addButton = (Button) findViewById(R.id.addNewButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddNewPurchaseActivity.class);
                startActivityForResult(i, ADD_NEW_PURCHASE_REQUEST);
            }
        });

        //category pull-down list select the category
        final Spinner categoryTotal = (Spinner) findViewById(R.id.categoryTotal);

        categoryTotal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryTotal = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + category, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Called when the user revisits this Activity
     */
    @Override
    public void onResume() {
        Calculations stats = new Calculations(db, this);
        String[] categories = getResources().getStringArray(R.array.category_month_total);

        // Total month spendings
        TextView monthTotal = (TextView) findViewById(R.id.monthtotal);
        monthTotal.setText(stats.getMonthTotal() + "");

        // Total in category
        TextView totalcategory = (TextView) findViewById(R.id.totalcategory);
        totalcategory.setText(stats.showCategorySpendings(categories[0]) + "");
        super.onResume();
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
                i1.putExtra("payperiod", payperiod);
                i1.putExtra("household", household);
                i1.putExtra("income", income);
                startActivityForResult(i1, SAVE_SETTINGS_REQUEST);
                return true;
            case R.id.actionPurchaseHistory:
                Intent i2 = new Intent(getApplicationContext(), PurchaseHistoryActivity.class);
                startActivity(i2);
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
                // Create purchase object
                String name = data.getStringExtra("name");
                double amount = data.getDoubleExtra("amount", 0.0);
                String category = data.getStringExtra("category");
                // Store the new purchase object
                db.insertPurchase(new Purchase(name, (int) (amount * (100)), category));
            }
        } else if (requestCode == SAVE_SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get values from the activity
                int payperiod = data.getIntExtra("payperiod", Calculations.DEFAULT_PAYPERIOD);
                int household = data.getIntExtra("household", Calculations.DEFAULT_HOUSEHOLD);
                int income = data.getIntExtra("income", Calculations.DEFAULT_INCOME);

                // Save the new values to the settings file.
                SharedPreferences pref = getSharedPreferences(getString(R.string.settingsFile),
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("payperiod", payperiod);
                editor.putInt("household", household);
                editor.putInt("income", income);
                editor.commit();
                loadSettings();
            }
        }
    }

    /**
     * attempts to retrieve user saved settings
     */
    public void loadSettings() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.settingsFile),
                Context.MODE_PRIVATE);
        payperiod = preferences.getInt("payperiod", Calculations.DEFAULT_PAYPERIOD);
        household = preferences.getInt("household", Calculations.DEFAULT_HOUSEHOLD);
        income = preferences.getInt("income", Calculations.DEFAULT_INCOME);
    }
}
