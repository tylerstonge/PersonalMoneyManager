package edu.oswego.tygama344;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private static final int ADD_NEW_PURCHASE_REQUEST = 1;
    private static final int SAVE_SETTINGS_REQUEST = 2;


    // Current app settings
    private String userid;
    private int payperiod;
    private int household;
    private int income;

    private MySQLiteHelper db;

    private Button addButton;

    private PieChart mChart;
    public int[] yData = new int[5];
    public String[] xData = new String[5];



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        String[] categories = getResources().getStringArray(R.array.category_month_total);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        RelativeLayout chart = (RelativeLayout) findViewById(R.id.rel);
        mChart = new PieChart(this);
        chart.addView(mChart, 1200, 1200);
        chart.setBackgroundColor(Color.parseColor("#000000"));
        mChart.setUsePercentValues(true);

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);


        // customize legends
        Legend l = mChart.getLegend();
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        // Load stored settings if exist
        loadSettings();

        // Request userid if this is not set
        if (userid == null) {
            Server s = new Server();
            userid = s.requestUserId();
            SharedPreferences pref = getSharedPreferences(getString(R.string.settingsFile),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("userid", s.requestUserId());
            editor.apply();
        }
        Log.e("THIS IS THE USERID", userid);

        // Database
        db = new MySQLiteHelper(this);
        Calculations stats = new Calculations(db, this);
        prepareData(stats, categories);
        addData();


        // Button listener
        addButton = (Button) findViewById(R.id.addNewButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddNewPurchaseActivity.class);
                startActivityForResult(i, ADD_NEW_PURCHASE_REQUEST);
            }
        });

        //category pull-down list select the category
//        final Spinner categoryTotal = (Spinner) findViewById(R.id.categoryTotal);

//        categoryTotal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String categoryTotal = parent.getItemAtPosition(position).toString();
//                //Toast.makeText(parent.getContext(), "Selected: " + category, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    /**
     * Called when the user revisits this Activity
     */
    @Override
    public void onResume() {
        Calculations stats = new Calculations(db, this);
        String[] categories = getResources().getStringArray(R.array.category_month_total);
        prepareData(stats, categories);
        addData();
        // Total month spendings
        TextView monthTotal = (TextView) findViewById(R.id.monthtotal);
        monthTotal.setText((float)stats.getMonthTotal()/ 100.0 + "");

        // Total in category
//        TextView totalcategory = (TextView) findViewById(R.id.totalcategory);
//        totalcategory.setText(stats.showCategorySpendings(categories[0]) + "");
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
                editor.apply();
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
        userid = preferences.getString("userid", null);
    }

    private void prepareData(Calculations calc, String[] categories) {
        int n = calc.getMonthTotal();
        String s = categories[0];
        for (int i = 0; i < categories.length; i++) {
            yData[i] = calc.showCategorySpendings(categories[i]);
            xData[i] = categories[i];
        }

//        yData[0] = 100;
//        yData[1] = 80;
//        xData[0] = "Groceries";
//        xData[1] = "Gas";
    }

    private void addData() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new Entry(yData[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "Category Spendings");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();
    }
}
