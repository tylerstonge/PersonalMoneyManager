package edu.oswego.tygama344;

import android.provider.BaseColumns;

import java.sql.Date;

public final class DatabaseContract {

    public DatabaseContract() {
        // Empty contructor
    }

    public static abstract class PurchaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "purchases";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DATE = "purchasedate";

        public static final String COLUMN_CATEGORY = "category";

        public static final String CREATE = "create table " + TABLE_NAME + "(" +
                _ID + " integer primary key autoincrement, " +
                COLUMN_NAME + " text not null, " +
                COLUMN_AMOUNT + " integer not null, " +
                COLUMN_DATE + " bigint not null default (strftime('%s', 'now')), " +
                COLUMN_CATEGORY + " text not null)";
    }
}