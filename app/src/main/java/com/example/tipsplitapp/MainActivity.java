package com.example.tipsplitapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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

        calcListener();
        clearListener();

    }

    public double tipListener(){
        int rId = rg_tipButtons.getCheckedRadioButtonId();
        tipBtn = findViewById(rId);

        String tip = tipBtn.getText().toString();
        tip = tip.substring(0, tip.length()-1);

        double tipVal = Double.parseDouble(tip);
        return tipVal/100.0;
    }

    public void clearListener(){
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

    //TODO: Find a way to update the tip textviews upon radio buttons update

    public void calcListener() {
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
                double res = billPlusTip/numPeople;

                tv_totalTip.setText(String.format("%.2f", billPlusTip));
                tv_tipAmount.setText(String.format("%.2f", tip_res));
                tv_calculatedSplit.setText(String.format("%.2f", res));
            }
        });
    }

}