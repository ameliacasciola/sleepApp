package com.example.ameli.module2tutorials;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LightActivity extends AppCompatActivity {
    private Button home;
    private Button replay;
    private Button morseList;
    private Button guessBtn;
    private static EditText guess;

    private final static long TIME_UNIT = 250L;
    private final static long DOT_DELAY = TIME_UNIT * 2;
    private final static long DASH_DELAY = TIME_UNIT * 3;
    private final static long INTRA_LETTER_DELAY = TIME_UNIT;
    private final static long INTER_LETTER_DELAY = TIME_UNIT * 3;
    private final static long INTER_WORD_DELAY = TIME_UNIT * 7;

    private Button flasher = null;


    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);

        //initialize values from ID
        replay = (Button)findViewById(R.id.replay);
        morseList = (Button)findViewById(R.id.morse_list);
        guessBtn = (Button)findViewById(R.id.guess_btn);

        guess = (EditText) findViewById(R.id.input_letter);

        //set button functionality to take you to the graphics exercise
        guessBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //check if the guess is right
                if (rightLetter()) {
                    //go to done page
                }
                else {
                    //incorrect page
                }

            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             //   LightView.update(canvas);
            }
        });


    }

    //check if guess is correct
    private boolean rightLetter() {
        if(guess.length() == 0) {
            Toast.makeText(context, "Error: Please enter a letter from a-z", Toast.LENGTH_LONG).show();
        }
        //check that it's in a-z
        //check that it's the one we displayed
        String letter = LightView.getCurLetter();

        return false;

    }
}
