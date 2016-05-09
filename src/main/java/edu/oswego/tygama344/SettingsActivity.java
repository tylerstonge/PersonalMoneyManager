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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;


public class SettingsActivity extends Activity {
    private Switch mySwitch;

    private EditText payperiod;
    private EditText household;
    private EditText income;
    private EditText threshold;
    private Button submitButton;

    boolean payeriodNotEmpty = false;
    boolean householdNotEmpty = false;
    boolean incomeNotEmpty = false;
    boolean sendstatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Bundle extras = getIntent().getExtras();

        int payperiodValue = extras.getInt("payperiod");
        int householdValue = extras.getInt("household");
        int incomeValue = extras.getInt("income");
        int thresholdValue = extras.getInt("threshold");
        sendstatistics = extras.getBoolean("sendstatistics");

        // Find the name and amount EditText boxes
        payperiod = (EditText) findViewById(R.id.payperiod);
        payperiod.setText(String.valueOf(payperiodValue));
        payperiod.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int end, int after) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int end, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                payeriodNotEmpty = (s.length() > 0);
//                unlockButton();
            }
        });

        //TODO make the button always enabled

        household = (EditText) findViewById(R.id.household);
        household.setText(String.valueOf(householdValue));
        household.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int end, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int end, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                householdNotEmpty = (s.length() > 0);
                unlockButton();
            }
        });

        income = (EditText) findViewById(R.id.income);
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("");
        ((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
        income.setText(nf.format(incomeValue / 100f).trim().replace(",", ""));

        income.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int end, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int end, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                incomeNotEmpty = (s.length() > 0);
            }
        });

        threshold = (EditText) findViewById(R.id.threshold);
        threshold.setText(nf.format(thresholdValue / 100f).trim().replace(",", ""));
        threshold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int end, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int end, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                householdNotEmpty = (s.length() > 0);
            }
        });

        // Submit button sends data back to MainActivity
        submitButton = (Button) findViewById(R.id.button);
        submitButton.setEnabled(true);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("payperiod", Integer.parseInt(payperiod.getText().toString()));
                result.putExtra("household", Integer.parseInt(household.getText().toString()));
                result.putExtra("income", (int) (Float.parseFloat(income.getText().toString()) * 100f));
                result.putExtra("threshold", (int) (Float.parseFloat(threshold.getText().toString()) * 100f));
                result.putExtra("sendstatistics", sendstatistics);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

        mySwitch = (Switch) findViewById(R.id.stats);
        // Load switch from old user preferences
        mySwitch.setChecked(sendstatistics);
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendstatistics = isChecked;
            }
        });
    }

    public void unlockButton() {
        if (((household.getText().toString().length()) > 0) && (Integer.parseInt(household.getText().toString()) >= 1)) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
    }
}