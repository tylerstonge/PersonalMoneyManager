package edu.oswego.tygama344;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private MySQLiteHelper db;

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(MySQLiteHelper.DATABASE_NAME);
        db = new MySQLiteHelper(getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void testCanGetWritableDatabase() {
        SQLiteDatabase writable = db.getWritableDatabase();
        assertTrue(writable.isOpen());
    }

    @Test
    public void testInsertPurchase() {
        int before = db.getPurchasesCount();
        db.insertPurchase("Groceries", 90f);
        assertEquals(before + 1, db.getPurchasesCount());
    }

    @Test
    public void testInsertPurchaseObject() {
        int before = db.getPurchasesCount();
        db.insertPurchase(new Purchase("Groceries", 90f));
        assertEquals(before + 1, db.getPurchasesCount());
    }

    @Test
    public void testGetAllPurchases() {
        // initial size of Table
        int before = db.getPurchasesCount();

        // Store purchases
        db.insertPurchase(new Purchase("Gas", 30f));
        db.insertPurchase(new Purchase("Groceries", 24f));
        db.insertPurchase(new Purchase("Netflix", 8f));

        // Get all entries
        ArrayList<Purchase> purchases = db.getAllPurchases();

        // ArrayList should have before+3 entries
        assertEquals(purchases.size(), 3 + before);
    }

    @Test
    public void testRemovePurchase() {
        // Insert one purchase to guarantee database is not empty
        db.insertPurchase("Electricity", 65f);

        // Get all purchases and choose first
        ArrayList<Purchase> purchases = db.getAllPurchases();
        Purchase p = purchases.get(0);

        // Delete first element from database
        db.removePurchase(p.getId());

        // Attempting to retrieve the deleted element should return null
        assertNull(db.getPurchase(p.getId()));
    }

}
