package com.example.tipsplitapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private EditText et_billTotal, et_numPeople;
    TextView tv_tipAmount, tv_totalTip, tv_calculatedSplit;
    Button calculateButton, clearButton;

    RadioGroup rg_tipButtons;
    RadioButton tipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_billTotal = findViewById(R.id.et_billTotal);
        et_numPeople = findViewById(R.id.et_numPeople);
        tv_tipAmount = findViewById(R.id.tipAmount);
        tv_totalTip = findViewById(R.id.totalTip);
        tv_calculatedSplit = findViewById(R.id.calculatedSplit);

        //calculating button
        calculateButton = findViewById(R.id.calculateButton);

        //tip buttons grouping
        rg_tipButtons = findViewById(R.id.rg_tips);

        //Clear/reset button to clear all text inputs and reset radio buttons
        clearButton = findViewById(R.id.clearButton);

        //preventTipListener();
        rTipListener();
        calcListener();
        clearListener();
    }

    //these two methods allow for the textview to be retained after rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("result", tv_calculatedSplit.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        tv_calculatedSplit.setText(savedInstanceState.getString("result"));
    }

    public double tipListener(){ //gets the value of the tip button selected
        int rId = rg_tipButtons.getCheckedRadioButtonId();
        tipBtn = findViewById(rId);

        String tip = tipBtn.getText().toString();
        tip = tip.substring(0, tip.length()-1);

        double tipVal = Double.parseDouble(tip);
        return tipVal/100.0;
    }

    /* TODO find a way to uncheck the radio group when the edittext is empty
    public void preventTipListener(){
        et_billTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (rg_tipButtons.getCheckedRadioButtonId() != -1){
                    rg_tipButtons.clearCheck();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
     */


    public void clearListener(){ //button to reset/clear all views
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                et_billTotal.getText().clear();
                et_numPeople.getText().clear();
                rg_tipButtons.clearCheck();
                tv_calculatedSplit.setText("");
                tv_tipAmount.setText("");
                tv_totalTip.setText("");
            }
        });
    }

    public void rTipListener(){ //listener to allow live updates to the textviews regarding tips
        RadioGroup rg = (RadioGroup) findViewById(R.id.rg_tips);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                String billTotal = et_billTotal.getText().toString();
                if (billTotal.isEmpty())
                    return;
                double billTotalVal = Double.parseDouble(billTotal);
                double tip = tipListener(); //gets tip rate
                double tipNum = billTotalVal*tip;
                double totalTip = tipNum+billTotalVal;

                String tipTemp = String.format("%.2f", tipNum);
                String totalTemp = String.format("%.2f", totalTip);

                StringBuilder sb = new StringBuilder(tipTemp);
                sb.insert(0, "$");
                StringBuilder sb2= new StringBuilder(totalTemp);
                sb2.insert(0,"$");

                tv_tipAmount.setText(sb);
                tv_totalTip.setText(sb2);
            }
        });
    }

    public void calcListener() {//listener for the "GO" button to calculate the final split costs
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String billTotal = et_billTotal.getText().toString();
                if (billTotal.isEmpty())
                    return;
                double billTotalVal = Double.parseDouble(billTotal);

                String people = et_numPeople.getText().toString();
                if (people.isEmpty())
                    return;
                int numPeople = Integer.parseInt(people);

                double tipRate = tipListener();

                double tip_res = billTotalVal*tipRate;
                double billPlusTip = billTotalVal+tip_res;
                double res = Math.ceil((billPlusTip/numPeople) * 100.0) / 100.0;

                String resTemp = String.format("%.2f", res);
                String tipTemp = String.format("%.2f", tip_res);
                String totalTemp = String.format("%.2f", billPlusTip);

                StringBuilder sb3 = new StringBuilder(resTemp);
                sb3.insert(0,"$");
                StringBuilder sb1 = new StringBuilder(tipTemp);
                sb1.insert(0, "$");
                StringBuilder sb2= new StringBuilder(totalTemp);
                sb2.insert(0,"$");

                tv_tipAmount.setText(sb1);
                tv_totalTip.setText(sb2);
                tv_calculatedSplit.setText(sb3);
            }
        });
    }

}