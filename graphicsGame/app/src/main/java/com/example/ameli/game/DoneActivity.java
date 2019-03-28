package com.example.ameli.game;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.ameli.game.MainActivity.score;


public class DoneActivity  extends Activity {
    private Button goHome;
    private Button restart;
    private TextView scoreText;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        goHome = (Button) findViewById(R.id.home_from_words);
        restart = (Button) findViewById(R.id.restart_words);
        scoreText = (TextView) findViewById(R.id.score);

        scoreText.setText("Your score is: " + score);

        //set button functionality to take you back to the main page
//        goHome.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(context, home.class);
//                context.startActivity(intent);
//            }
//        });


        //set button functionality to take you back to the tip exercise
        restart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });


    }

}
