package com.example.ameli.module2tutorials;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText billText;
    private TextView totalToPayText;
    private TextView totalTipText;
    private TextView totalPerPersonText;
    private TextView tipText;
    private TextView splitText;
    private Button calculate;
    private Button tipPlus;
    private Button tipMinus;
    private Button splitPlus;
    private Button splitMinus;

    private String billString;
    private double bill;
    private int tip = 10;
    private int numSplit = 1;

    private double totalToPay;
    private double totalTip;
    private double totalPerPerson;

    private Context context = this;
    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize values from ID
        billText = (EditText) findViewById(R.id.bill);
        totalToPayText = (TextView) findViewById(R.id.total_to_pay);
        totalTipText = (TextView) findViewById(R.id.total_tip);
        totalPerPersonText = (TextView) findViewById(R.id.total_per_person);
        tipText = (TextView) findViewById(R.id.tip);
        splitText = (TextView) findViewById(R.id.split);

        calculate = (Button) findViewById(R.id.calculate_button);
        tipPlus = (Button) findViewById(R.id.tip_plus);
        tipMinus = (Button) findViewById(R.id.tip_minus);
        splitPlus = (Button) findViewById(R.id.split_plus);
        splitMinus = (Button) findViewById(R.id.split_minus);


        //Set button functionality to increase and decrease tip value
        // and split number on button click
        tipPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tip++;
                tipText.setText(Integer.toString(tip));
            }
        });
        tipMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(tip > 0) {
                    tip--;
                    tipText.setText(Integer.toString(tip));
                }

                else {
                    Toast.makeText(context, "Error: Tip must be a positive value!", Toast.LENGTH_LONG).show();
                }
            }
        });
        splitPlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numSplit++;
                splitText.setText(Integer.toString(numSplit));
            }
        });
        splitMinus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(numSplit > 1) {
                    numSplit--;
                    splitText.setText(Integer.toString(numSplit));
                }

                else {
                    Toast.makeText(context, "Error: Bill must be paid by at least 1 person!", Toast.LENGTH_LONG).show();
                }
            }
        });

        //when calculate button is pressed, perform calculations and display the answers
        calculate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                billString = billText.getText().toString();
                try {
                    //do calculations
                    bill = Double.parseDouble(billString);
                    totalTip = 0.01 * tip * bill;
                    totalToPay = bill + totalTip;
                    totalPerPerson = totalToPay / numSplit;

                    //set to round to 2 decimal places
                    df.setRoundingMode(RoundingMode.CEILING);

                    //display calculated values
                    totalPerPersonText.setText("Total Per Person: " + df.format(totalPerPerson));
                    totalTipText.setText("Total Tip: " + df.format(totalTip));
                    totalToPayText.setText("Total To Pay: " + df.format(totalToPay));
                }
                catch(NumberFormatException e) {
                    Toast.makeText(context, "Error: Please enter a valid bill value", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
