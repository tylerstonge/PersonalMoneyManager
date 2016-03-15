package edu.oswego.tygama344;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewPurchaseActivity extends Activity {

	Button cancelButton;
	Button submitButton;
	EditText nameEditText;
	EditText amountEditText;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_purchase);

		// Find the name and amount EditText boxes
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		amountEditText = (EditText) findViewById(R.id.amountEditText);

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
		submitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent result = new Intent();
				result.putExtra("name", getNameFromEditText());
				result.putExtra("amount", getAmountFromEditText());
				setResult(Activity.RESULT_OK, result);
				finish();
			}
		});
	}

	private String getNameFromEditText() {
		return nameEditText.getText().toString();	
	}

	private double getAmountFromEditText() {
		return Double.parseDouble(amountEditText.getText().toString());
	}
}
