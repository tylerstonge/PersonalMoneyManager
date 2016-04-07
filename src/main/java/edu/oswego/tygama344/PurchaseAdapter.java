package edu.oswego.tygama344;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PurchaseAdapter extends ArrayAdapter<Purchase> {

    Context context;
    int resource;
    ArrayList<Purchase> data = null;

    public PurchaseAdapter(Context context, int resource, ArrayList<Purchase> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(resource, parent, false);

        TextView nameTextView = (TextView) row.findViewById(R.id.nameTextView);
        TextView amountTextView = (TextView) row.findViewById(R.id.amountTextView);
        TextView dateTextView = (TextView) row.findViewById(R.id.dateView);
        TextView categoryTextView = (TextView) row.findViewById(R.id.categoryTextView);

        nameTextView.setText(data.get(position).getName());
        amountTextView.setText(data.get(position).getDollars());
        dateTextView.setText(data.get(position).getDate().toString());
        categoryTextView.setText(data.get(position).getCategory());

        return row;
    }

    public void replaceList(ArrayList<Purchase> list) {
        this.clear();
        this.addAll(list);
        this.notifyDataSetChanged();
    }
}
