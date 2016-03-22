package edu.oswego.tygama344;

import android.content.Context;
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
		db.execSQL(PurchaseContract.PurchaseEntry.CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PurchaseContract.PurchaseEntry.TABLE_NAME);
		onCreate(db);
	}

	public boolean insertPurchase(String name, float amount) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put(PurchaseContract.PurchaseEntry.COLUMN_NAME, name);
		vals.put(PurchaseContract.PurchaseEntry.COLUMN_AMOUNT, amount);
		db.insert(PurchaseContract.PurchaseEntry.TABLE_NAME, vals);
		return true;
	}

	public ArrayList<Purchase> getAllPurchases() {
		ArrayList purchases = new ArrayList<Purchase>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + PurchaseContract.PurchaseEntry.TABLE_NAME, null);
		cursor.moveToFirst();

		while (cursor.isAfterLast() == false) {
			String name = cursor.getString(cursor.getColumnIndex(PurchaseContract.PurchaseEntry.COLUMN_NAME));
			float amount = cursor.getFloat(cursor.getColumnIndex(PurchaseContract.PurchaseEntry.COLUMN_AMOUNT));
			purchases.add(new Purchase(name, amount);
		}
		return purchases;
	}
}
