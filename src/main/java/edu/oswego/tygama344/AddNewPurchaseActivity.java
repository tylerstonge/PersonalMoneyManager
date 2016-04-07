package edu.oswego.tygama344;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddNewPurchaseActivity extends Activity {

	Button cancelButton;
	Button submitButton;
	EditText nameEditText;
	EditText amountEditText;

	boolean nameNotEmpty = false;
	boolean amountNotEmpty = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_purchase);

		// Find the name and amount EditText boxes
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		nameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int end, int after) { }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int end, int after) { }

			@Override
			public void afterTextChanged(Editable s) {
				nameNotEmpty = (s.length() > 0);
				unlockButton();
			}
		});
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		amountEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int end, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int end, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				amountNotEmpty = (s.length() > 0);
				unlockButton();
			}
		});


		

		//category pull dowm list select the category
		final Spinner categoryList = (Spinner) findViewById(R.id.categoryList);

		categoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String category = parent.getItemAtPosition(position).toString();
				//Toast.makeText(parent.getContext(), "Selected: " + category, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// Cancel button returns to MainActivity
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED, null);
				finish();
			}
		});

		// Submit button sends data back to MainActivity
		submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setEnabled(false);
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent result = new Intent();
				result.putExtra("name", getNameFromEditText());
				result.putExtra("amount", getAmountFromEditText());
				result.putExtra("category", categoryList.getSelectedItem().toString());
				setResult(Activity.RESULT_OK, result);
				finish();
			}
		});
	}

	public void unlockButton() {
		if (amountNotEmpty && nameNotEmpty) {
			submitButton.setEnabled(true);
		} else {
			submitButton.setEnabled(false);
		}
	}

	private String getNameFromEditText() {
		return nameEditText.getText().toString();	
	}

	private double getAmountFromEditText() {
		return Double.parseDouble(amountEditText.getText().toString());
	}
}
