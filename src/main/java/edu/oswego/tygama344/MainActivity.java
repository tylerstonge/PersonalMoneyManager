package edu.oswego.tygama344;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
    private int threshold;
    private boolean sendstatistics;

    private MySQLiteHelper db;

    private Button addButton;

    private PieChart pChart;
    private BarChart bChart;
    private LineChart lChart;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String[] categories = getResources().getStringArray(R.array.category_month_total);

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

        // Database
        db = new MySQLiteHelper(this);
        Calculations stats = new Calculations(db, this);

        pChart = (PieChart) findViewById(R.id.rel);
        pChart.setUsePercentValues(true);

        // enable hole and configure
        pChart.setDrawHoleEnabled(false);

        // enable rotation of the chart by touch
        pChart.setRotationEnabled(true);
        pChart.setRotationAngle(0);


        // customize legends
        pChart.setDescription("");
        Legend l = pChart.getLegend();
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        //BARCHART
        bChart = (BarChart) findViewById(R.id.bar);
        bChart.setDescription("");
        bChart.setDescriptionColor(Color.WHITE);
        XAxis xAxis = bChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);
        xAxis.setTextColor(Color.WHITE);

        YAxis leftAxis = bChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinValue(0f);

        YAxis rightAxis = bChart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinValue(0f);
        bChart.getLegend().setEnabled(false);


        //LINECHART

        lChart = (LineChart) findViewById(R.id.line);
        lChart.setDescription("");
        lChart.getLegend().setTextColor(Color.WHITE);
        lChart.getAxisRight().setEnabled(false);

        // Populate graphs
        setPieData(stats, categories);
        setBarData();
        setLineData();

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
     * Called when the user revisits this Activity
     */
    @Override
    public void onResume() {
        // Refresh objects
        loadSettings();
        Calculations stats = new Calculations(db, this);
        String[] categories = getResources().getStringArray(R.array.category_month_total);

        // Populate graphs
        setPieData(stats, categories);
        setBarData();
        setLineData();

        // Total month spendings
        TextView monthTotal = (TextView) findViewById(R.id.monthtotal);
        monthTotal.setText("$" + (float) stats.getMonthTotal() / 100.0);

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
                i1.putExtra("threshold", threshold);
                i1.putExtra("sendstatistics", sendstatistics);
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

                // If the total purchases in last 30 days is over threshold, Toast
                ArrayList<Float> monthsPurchases = db.getMonthDailyTotals();
                float total = monthsPurchases.get(monthsPurchases.size() - 1);
                if (threshold > 0 && total > (threshold / 100f)) {
                    Toast.makeText(this, "You have exceeded monthly threshold.", Toast.LENGTH_LONG).show();
                }

                // Update ratio on external server
                if (sendstatistics) {
                    Server s = new Server();
                    s.putTotalRatio(this.userid, db.getPastMonthTotalRatio(this.household));
                }
            }
        } else if (requestCode == SAVE_SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get values from the activity
                int payperiod = data.getIntExtra("payperiod", Calculations.DEFAULT_PAYPERIOD);
                int household = data.getIntExtra("household", Calculations.DEFAULT_HOUSEHOLD);
                int income = data.getIntExtra("income", Calculations.DEFAULT_INCOME);
                int threshold = data.getIntExtra("threshold", Calculations.DEFAULT_THRESHOLD);
                boolean sendstatistics = data.getBooleanExtra("sendstatistics", true);

                // Save the new values to the settings file.
                SharedPreferences pref = getSharedPreferences(getString(R.string.settingsFile),
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("payperiod", payperiod);
                editor.putInt("household", household);
                editor.putInt("income", income);
                editor.putInt("threshold", threshold);
                editor.putBoolean("sendstatistics", sendstatistics);
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
        threshold = preferences.getInt("threshold", Calculations.DEFAULT_THRESHOLD);
        userid = preferences.getString("userid", null);
        sendstatistics = preferences.getBoolean("sendstatistics", true);
    }

    private void setPieData(Calculations calc, String[] categories) {
        int[] yData = new int[5];
        String[] xData = new String[5];

        for (int i = 0; i < categories.length; i++) {
            yData[i] = calc.showCategorySpendings(categories[i]);
            xData[i] = categories[i];
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < yData.length; i++) {
            if (yData[i] > 0)
                yVals1.add(new Entry(yData[i], i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++) {
            if (yData[i] > 0)
                xVals.add(xData[i]);
        }

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
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

        pChart.setData(data);

        // undo all highlights
        pChart.highlightValues(null);

        // update pie chart
        pChart.invalidate();
    }

    private void setBarData() {
        float serverRatio = new Server().getTotalRatio();
        float userRatio = db.getPastMonthTotalRatio(household);
        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        yVals.add(new BarEntry(serverRatio, 0));
        yVals.add(new BarEntry(userRatio, 1));
        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("Average Spending");
        xVals.add("Your Spending");
        BarDataSet set1 = new BarDataSet(yVals, "ratio");

        set1.setBarSpacePercent(35f);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        bChart.setData(data);
    }

    private void setLineData() {
        // Data
        ArrayList<Float> dailyTotals = db.getMonthDailyTotals();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        float max = dailyTotals.get(dailyTotals.size() - 1);

        // Make sure Limit is set to current threshold value all the time
        LimitLine ll1 = new LimitLine((float) threshold / 100f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTextColor(Color.WHITE);

        LimitLine ll = new LimitLine((30f / payperiod) * (income / 100f), "Monthly Income");
        ll.setLineWidth(4f);
        ll.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll.setTextSize(10f);
        ll.setTextColor(Color.WHITE);
        ll.setLineColor(Color.GREEN);

        YAxis left = lChart.getAxisLeft();
        left.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        left.addLimitLine(ll1);
        left.addLimitLine(ll);
        left.setAxisMaxValue(max + (0.05f * max));
        left.setAxisMinValue(0f);
        left.setDrawZeroLine(false);
        left.setTextColor(Color.WHITE);


        for (int i = 0; i < dailyTotals.size(); i++) {
            yVals.add(new BarEntry(dailyTotals.get(i), i));
            xVals.add("");
        }

        LineDataSet set1 = new LineDataSet(yVals, "Daily spendings");
        set1.setValueTextColor(Color.WHITE);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        lChart.setData(data);
    }

}
