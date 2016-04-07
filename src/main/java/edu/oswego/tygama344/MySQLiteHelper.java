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
            return createPurchaseFromCursor(cursor);
        }
        return null;
    }

    public ArrayList<Purchase> getCurrentMonthsPurchases() {
        ArrayList<Purchase> purchases = new ArrayList<Purchase>();
        SQLiteDatabase db = this.getReadableDatabase();
        Calendar c = Calendar.getInstance();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.PurchaseEntry.TABLE_NAME + " WHERE strftime(%m%Y," + DatabaseContract.PurchaseEntry.COLUMN_DATE + ")=?",
                new String[]{c.get(Calendar.MONTH) + c.get(Calendar.YEAR) + ""});
        while (!cursor.isAfterLast()) {
            purchases.add(createPurchaseFromCursor(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return purchases;
    }

    public ArrayList<Purchase> getPurchasesByCategory(String category) {
        ArrayList<Purchase> purchases = new ArrayList<Purchase>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.PurchaseEntry.TABLE_NAME + " WHERE " + DatabaseContract.PurchaseEntry.COLUMN_CATEGORY + "=?",
                new String[]{category});
        while (!cursor.isAfterLast()) {
            purchases.add(createPurchaseFromCursor(cursor));
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
            purchases.add(createPurchaseFromCursor(cursor));
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

    public boolean removePurchase(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(DatabaseContract.PurchaseEntry.TABLE_NAME, DatabaseContract.PurchaseEntry._ID + "=? ",
                new String[]{Integer.toString(id)});
        return rows == 1;
    }

    private Purchase createPurchaseFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.PurchaseEntry._ID));
        String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_NAME));
        int amount = cursor.getInt(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_AMOUNT));
        String category = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_CATEGORY));
        String StringDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_DATE));
        Date purchaseDate = new Date(Long.parseLong(StringDate) * 1000);
        return new Purchase(id, name, amount, purchaseDate, category);
    }
}
