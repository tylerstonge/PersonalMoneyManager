package edu.oswego.tygama344;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


public class SettingsActivity extends Activity {
    private Switch mySwitch;

    private EditText payperiod;
    private EditText household;
    private EditText income;
    private Button submitButton;

    boolean payeriodNotEmpty = false;
    boolean householdNotEmpty = false;
    boolean incomeNotEmpty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO store settings into a file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // Find the name and amount EditText boxes
        payperiod = (EditText) findViewById(R.id.payperiod);
        payperiod.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int end, int after) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int end, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                payeriodNotEmpty = (s.length() > 0);
                unlockButton();
            }
        });
        household = (EditText) findViewById(R.id.household);
        household.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int end, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int end, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                householdNotEmpty = (s.length() > 0);
                unlockButton();
            }
        });
        income = (EditText) findViewById(R.id.income);
        income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int end, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int end, int after) { }

            @Override
            public void afterTextChanged(Editable s) {
                incomeNotEmpty = (s.length() > 0);
                unlockButton();
            }
        });

        // Submit button sends data back to MainActivity
        submitButton = (Button) findViewById(R.id.button);
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("payperiod", getAmountFromEditText(payperiod));
                result.putExtra("household", getAmountFromEditText(household));
                result.putExtra("income", getAmountFromEditText(income));
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

        mySwitch = (Switch) findViewById(R.id.stats);

        //set the switch to ON
        mySwitch.setChecked(true);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
//                    switchStatus.setText("Sending");
                }
                else {
//                    switchStatus.setText("Switch is currently OFF");
                }
            }
        });
    }

    public void unlockButton() {
        if (payeriodNotEmpty && householdNotEmpty && incomeNotEmpty) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
    }

    private double getAmountFromEditText(TextView t) {
        return Double.parseDouble(t.getText().toString());
    }

}