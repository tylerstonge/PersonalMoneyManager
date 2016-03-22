package edu.oswego.tygama344;

import android.provider.BaseColumns;

public final class PurchaseContract {
	
	public PurchaseContract() { 
		// Empty contructor
	}

	public static abstract class PurchaseEntry implements BaseColumns {
		public static final String TABLE_NAME = "purchases";
		public static final String COLUMN_ID = "_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_AMOUNT = "amount";
		public static final String CREATE = "create table " + TABLE_NAME + "(" + 
			COLUMN_ID + " integer primary key autoincrement, " + 
			COLUMN_NAME + " text not null, " + 
			COLUMN_AMOUNT + " float not null);";
	}
}