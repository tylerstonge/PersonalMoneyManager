package edu.oswego.tygama344;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "purchase.db";
	private static final int DATABASE_VERSION = 1;

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

	public boolean insertPurchase(String name, float amount) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put(DatabaseContract.PurchaseEntry.COLUMN_NAME, name);
		vals.put(DatabaseContract.PurchaseEntry.COLUMN_AMOUNT, amount);
		db.insert(DatabaseContract.PurchaseEntry.TABLE_NAME, null, vals);
		return true;
	}

	public ArrayList<Purchase> getAllPurchases() {
		ArrayList purchases = new ArrayList<Purchase>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + DatabaseContract.PurchaseEntry.TABLE_NAME, null);
		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_NAME));
			float amount = cursor.getFloat(cursor.getColumnIndex(DatabaseContract.PurchaseEntry.COLUMN_AMOUNT));
			purchases.add(new Purchase(name, amount));
		}
		return purchases;
	}
}
