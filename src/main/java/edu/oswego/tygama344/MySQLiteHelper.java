package edu.oswego.tygama344;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "pmm.db";
    public static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.PurchaseEntry.CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.PurchaseEntry.TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPurchase(String name, int amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(DatabaseContract.PurchaseEntry.COLUMN_NAME, name);
        vals.put(DatabaseContract.PurchaseEntry.COLUMN_AMOUNT, amount);
        vals.put(DatabaseContract.PurchaseEntry.COLUMN_CATEGORY, category);
        db.insert(DatabaseContract.PurchaseEntry.TABLE_NAME, null, vals);
        return true;
    }

    public boolean insertPurchase(Purchase purchase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(DatabaseContract.PurchaseEntry.COLUMN_NAME, purchase.getName());
        vals.put(DatabaseContract.PurchaseEntry.COLUMN_AMOUNT, purchase.getAmount());
        vals.put(DatabaseContract.PurchaseEntry.COLUMN_CATEGORY, purchase.getCategory());
        db.insert(DatabaseContract.PurchaseEntry.TABLE_NAME, null, vals);
        return true;
    }

    public Purchase getPurchase(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(DatabaseContract.PurchaseEntry.TABLE_NAME,
                new String[]{DatabaseContract.PurchaseEntry.COLUMN_NAME, DatabaseContract.PurchaseEntry.COLUMN_AMOUNT},
                DatabaseContract.PurchaseEntry._ID + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            int i = cursor.getInt(cursor.getColumnIndex(DatabaseContract.PurchaseEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_AMOUNT));
            String category = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_CATEGORY));
            String StringDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_DATE));
            Date purchaseDate = new Date(Long.parseLong(StringDate) * 1000);
            cursor.close();
            return new Purchase(i, name, amount, purchaseDate, category);
        }
        cursor.close();
        return null;
    }

    public ArrayList<Purchase> getCurrentMonthsPurchases() {
        ArrayList<Purchase> purchases = new ArrayList<Purchase>();
        ArrayList<Purchase> all = getAllPurchases();
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getDate().compareTo(c.getTime()) < 0 && all.get(i).getDate().compareTo(c.getTime()) > -2592000) {
                purchases.add(all.get(i));
            }
        }
        return purchases;
    }

    public ArrayList<Purchase> getPurchasesByCategory(String category) {
        ArrayList<Purchase> purchases = new ArrayList<Purchase>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.PurchaseEntry.TABLE_NAME + " WHERE " + DatabaseContract.PurchaseEntry.COLUMN_CATEGORY + "=?",
                new String[]{category});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.PurchaseEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_AMOUNT));
            String c = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_CATEGORY));
            String StringDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_DATE));
            Date purchaseDate = new Date(Long.parseLong(StringDate) * 1000);
            purchases.add(new Purchase(id, name, amount, purchaseDate, c));
            cursor.moveToNext();
        }
        cursor.close();
        return purchases;
    }

    public ArrayList<Purchase> getAllPurchases() {
        ArrayList<Purchase> purchases = new ArrayList<Purchase>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.PurchaseEntry.TABLE_NAME, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.PurchaseEntry._ID));
            String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_AMOUNT));
            String category = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_CATEGORY));
            String StringDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_DATE));
            Date purchaseDate = new Date(Long.parseLong(StringDate) * 1000);
            purchases.add(new Purchase(id, name, amount, purchaseDate, category));
            cursor.moveToNext();
        }
        cursor.close();
        return purchases;
    }

    public int getPurchasesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.PurchaseEntry.TABLE_NAME, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public float getPastMonthTotalRatio(int household) {
        ArrayList<Purchase> purchases = getCurrentMonthsPurchases();
        float total = 0f;
        for (Purchase p : purchases) {
            total += (float) p.getAmount();
        }
        total = total / (float) household;
        return total / 100f;
    }

    public ArrayList<Float> getMonthDailyTotals() {
        ArrayList<Float> result = new ArrayList<Float>();
        ArrayList<Purchase> purchases = getCurrentMonthsPurchases();

        // Set date to now
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        // Set the date to 30 days ago and zero out irrelevant other info (hours, minutes, seconds, milliseconds)
        c.add(Calendar.DAY_OF_MONTH, -29);
        c = zeroMeasurementsLessThanDay(c);

        // Create object to hold each purchase date
        Calendar purchaseDate = Calendar.getInstance();

        // Loop over purchases day by day for 30 days, accumulating totals
        float total = 0f;
        for (int i = 0; i < 30; i++) {
            for (Purchase p : purchases) {
                purchaseDate.setTime(p.getDate());
                purchaseDate = zeroMeasurementsLessThanDay(purchaseDate);

                // If Day && Month && Year is the same as index, add to total.
                if (c.equals(purchaseDate)) {
                    total += p.getAmount() / 100f;
                }
            }
            // Add days entry, reset total to 0, then increment to next day
            result.add(total);
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        return result;
    }

    private Calendar zeroMeasurementsLessThanDay(Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    public boolean removePurchase(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(DatabaseContract.PurchaseEntry.TABLE_NAME, DatabaseContract.PurchaseEntry._ID + "=? ",
                new String[]{Integer.toString(id)});
        return rows == 1;
    }
}
