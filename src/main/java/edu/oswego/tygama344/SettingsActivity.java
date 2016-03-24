package edu.oswego.tygama344;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;

import static edu.oswego.tygama344.R.id.submitButton;


public class SettingsActivity extends Activity {
    private TextView switchStatus;
    private Switch mySwitch;

    private EditText payperiod;
    private EditText household;
    private EditText income;
    private Button submitButton;

    boolean payeriodNotEmpty = false;
    boolean householdNotEmpty = false;
    boolean incomeNotEmpty = false;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        switchStatus = (TextView) findViewById(R.id.switchStatus);
        mySwitch = (Switch) findViewById(R.id.stats);

        //set the switch to ON
        mySwitch.setChecked(true);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    switchStatus.setText("Switch is currently ON");
                } else {
                    switchStatus.setText("Switch is currently OFF");
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