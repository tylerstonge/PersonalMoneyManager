package edu.oswego.tygama344;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

public class Calculations {

    public static final int DEFAULT_PAYPERIOD = 0;
    public static final int DEFAULT_HOUSEHOLD = 1;
    public static final int DEFAULT_INCOME = 0;

    MySQLiteHelper db;
    Context context;
    int payperiod;
    int household;
    int income;

    public Calculations(MySQLiteHelper db, Context context) {
        this.db = db;
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.settingsFile), Context.MODE_PRIVATE);
        payperiod = preferences.getInt("payperiod", DEFAULT_PAYPERIOD);
        household = preferences.getInt("household", DEFAULT_HOUSEHOLD);
        income = preferences.getInt("income", DEFAULT_INCOME);
    }

    public void reloadUserSettings() {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.settingsFile), Context.MODE_PRIVATE);
        payperiod = preferences.getInt("payperiod", DEFAULT_PAYPERIOD);
        household = preferences.getInt("household", DEFAULT_HOUSEHOLD);
        income = preferences.getInt("income", DEFAULT_INCOME);
    }

    public int getMonthTotal() {
        int total = 0;
        ArrayList<Purchase> list = db.getCurrentMonthsPurchases();
        for (int i = 0; i < list.size(); i++) {
            total = total + list.get(i).getAmount();
        }
        return total;
    }

    public int calculateNet() {
        int result = getMonthTotal() - income;
        return result;
    }

    public int showCategorySpendings(String category) {
        int total = 0;
        ArrayList<Purchase> list = db.getPurchasesByCategory(category);
        for (int i = 0; i < list.size(); i++) {
            total = total + list.get(i).getAmount();
        }
        return total;
    }



}
