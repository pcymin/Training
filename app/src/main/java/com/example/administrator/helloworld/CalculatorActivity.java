package com.example.administrator.helloworld;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.prefs.Preferences;

public class CalculatorActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private EditText etLoanAmount, etDownPayment, etTerm, etAnnualInterestRate;
    private TextView tvMonthlyPayment, tvTotalRepayment, tvTotalInterest,
            tvAverageMonthlyInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        etLoanAmount = (EditText) findViewById(R.id.loan_amount);
        etDownPayment = (EditText) findViewById(R.id.down_payment);
        etTerm = (EditText) findViewById(R.id.term);
        etAnnualInterestRate = (EditText) findViewById(R.id.annual_interest_rate);
        tvMonthlyPayment = (TextView) findViewById(R.id.monthly_repayment);
        tvTotalRepayment = (TextView) findViewById(R.id.total_repayment);
        tvTotalInterest = (TextView) findViewById(R.id.total_interest);
        tvAverageMonthlyInterest = (TextView)
                findViewById(R.id.average_monthly_interest);

        sp = getPreferences(MODE_PRIVATE);
        etLoanAmount.setText(sp.getString("LA","0.00"));
        etDownPayment.setText(sp.getString("DP","0.00"));
        etTerm.setText(sp.getString("TM","0"));
        etAnnualInterestRate.setText(sp.getString("IR","0.00"));
        calculate();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_calculate:
                calculate();
                break;
            case R.id.button_reset:
                reset();
                break;
        }

        Log.d("Check","Button clicked!");
    }

    private void calculate() {
        String amount = etLoanAmount.getText().toString();
        String downPayment = etDownPayment.getText().toString();
        String interestRate = etAnnualInterestRate.getText().toString();
        String term = etTerm.getText().toString();
        double loanAmount = Double.parseDouble(amount) -
                Double.parseDouble(downPayment);
        double interest = Double.parseDouble(interestRate) / 12 / 100;
        double noOfMonth = (Integer.parseInt(term) * 12);
        if (noOfMonth > 0) {
            double monthlyRepayment = loanAmount *
                    (interest+(interest/(java.lang.Math.pow((1+interest),
                            noOfMonth)-1)));
            double totalRepayment = monthlyRepayment * noOfMonth;
            double totalInterest = totalRepayment - loanAmount;
            double monthlyInterest = totalInterest / noOfMonth;
            tvMonthlyPayment.setText(String.format("%.2f", monthlyRepayment));
            tvTotalRepayment.setText(String.format("%.2f",totalRepayment));
            tvTotalInterest.setText(String.format("%.2f",totalInterest));
            tvAverageMonthlyInterest.setText(String.format("%.2f",monthlyInterest));
        }

        //Editor editor = sp.edit();
        //editor.putFloat("LA", );
        sp.edit().putString("LA", amount)
                 .putString("DP",downPayment)
                 .putString("TM",term)
                 .putString("IR", interestRate).commit();

    }

    private void reset() {
        etLoanAmount.setText("");
        etDownPayment.setText("");
        etTerm.setText("");
        etAnnualInterestRate.setText("");
        tvMonthlyPayment.setText(R.string.default_result);
        tvTotalRepayment.setText(R.string.default_result);
        tvTotalInterest.setText(R.string.default_result);
        tvAverageMonthlyInterest.setText(R.string.default_result);
    }
}
