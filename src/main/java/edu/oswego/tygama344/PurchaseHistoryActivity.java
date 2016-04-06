package edu.oswego.tygama344;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class PurchaseHistoryActivity extends Activity {

    static final int CONTEXT_REMOVE_PURCHASE = 1;

    MySQLiteHelper db;
    PurchaseAdapter adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_history);

        lv = (ListView) findViewById(R.id.historyListView);

        // Populate list with information from database
        db = new MySQLiteHelper(this);
        adapter = new PurchaseAdapter(this, R.layout.item_purchase, db.getAllPurchases());
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String title = (adapter.getItem(info.position)).getName();
        menu.setHeaderTitle(title);
        menu.add(Menu.NONE, CONTEXT_REMOVE_PURCHASE, CONTEXT_REMOVE_PURCHASE, "Remove");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case CONTEXT_REMOVE_PURCHASE:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                int id = (adapter.getItem(info.position)).getId();
                db.removePurchase(id);
                adapter.replaceList(db.getAllPurchases());
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.replaceList(db.getAllPurchases());
    }
}
