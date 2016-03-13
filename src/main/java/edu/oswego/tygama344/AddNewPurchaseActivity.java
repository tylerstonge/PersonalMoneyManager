package edu.oswego.tygama344;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddNewPurchaseActivity extends Activity {

	Button cancelButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_purchase);

		// Cancel button returns to MainActivity
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
