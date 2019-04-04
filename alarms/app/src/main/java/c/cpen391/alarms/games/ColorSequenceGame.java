package c.cpen391.alarms.games;

import androidx.appcompat.app.AppCompatActivity;
import c.cpen391.alarms.R;
import c.cpen391.alarms.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.os.SystemClock.sleep;

public class ColorSequenceGame extends AppCompatActivity {

    private Context context = this;

    private boolean wrongLetter;

    private int[] colors = new int[]{R.drawable.red_circle,R.drawable.orange_circle,
            R.drawable.green_circle,R.drawable.blue_circle,R.drawable.purple_circle};

    //colors displayed
    private List<Integer> chosenColors;

    //colors guessed
    private List<Integer> guessedColors;
    private int index;
    private int score;
    private boolean right;

    private Button red;
    private Button orange;
    private Button green;
    private Button blue;
    private Button purple;

    private ImageView imgSetDynamic;
    private final Handler handler = new Handler();

    private final Runnable r0 = new Runnable() {
        public void run() {
            guessedColors = new ArrayList<>();
            chosenColors = new ArrayList<>();
            index = 0;
            right = false;
            wrongLetter = false;
            switchColors();
            handler.postDelayed(r1, 1000);
        }
    };
    private final Runnable r1 = new Runnable() {
        public void run() {
            switchColors();
            handler.postDelayed(r2, 1000);
        }
    };
    private final Runnable r2 = new Runnable() {
        public void run() {
            switchColors();
            handler.postDelayed(r3, 1000);
        }
    };
    private final Runnable r3 = new Runnable() {
        public void run() {
            switchColors();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_game_main);
        guessedColors = new ArrayList<>();
        chosenColors = new ArrayList<>();
        index = 0;
        score = 10;
        right = false;
        wrongLetter = false;

        red = (Button) findViewById(R.id.red);
        orange = (Button) findViewById(R.id.orange);
        green = (Button) findViewById(R.id.green);
        blue = (Button) findViewById(R.id.blue);
        purple = (Button) findViewById(R.id.purple);

        imgSetDynamic = (ImageView) findViewById(R.id.imageView1);
        handler.postDelayed(r0, 1000);

        red.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chosenColors.size() == 4) {
                    guessedColors.add(R.drawable.red_circle);
                    if (guessedColors.size() == chosenColors.size()) {
                        check();
                    }
                }
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chosenColors.size() == 4) {
                    guessedColors.add(R.drawable.orange_circle);
                    if (guessedColors.size() == chosenColors.size()) {
                        check();
                    }
                }
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chosenColors.size() == 4) {
                    guessedColors.add(R.drawable.green_circle);
                    if (guessedColors.size() == chosenColors.size()) {
                        check();
                    }
                }
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chosenColors.size() == 4) {
                    guessedColors.add(R.drawable.blue_circle);
                    if (guessedColors.size() == chosenColors.size()) {
                        check();
                    }
                }
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(chosenColors.size() == 4) {
                    guessedColors.add(R.drawable.purple_circle);
                    if (guessedColors.size() == chosenColors.size()) {
                        check();
                    }
                }
            }
        });
    }

    protected void switchColors() {
        Random r = new Random();
        int nextColor = Math.abs(r.nextInt()%5);
        if(index != 0) {
            if (chosenColors.get(index-1)==colors[nextColor]) {
                if(nextColor != 4)
                    nextColor++;
                else
                    nextColor--;
            }
        }
        index++;
        chosenColors.add(colors[nextColor]);
        imgSetDynamic.setImageResource(colors[nextColor]);
    }

    private void check() {

        imgSetDynamic.setImageResource(R.drawable.white_circle);
        System.out.println("right is: " + right);
        for(int i=0; i<guessedColors.size();i++) {

            if (!(guessedColors.get(i).equals(chosenColors.get(i)))) {
                //they guessed something wrong!!
                score--;
                wrongLetter = true;
            }
        }
        if(wrongLetter) {
            Toast.makeText(context, "Try again!", Toast.LENGTH_SHORT).show();
            handler.postDelayed(r0, 1000);
        }
        else {
            right = true;
            //they got it right! Go to done screen
            setContentView(R.layout.sequence_game_done);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(context, c.cpen391.alarms.home.class);
                    context.startActivity(intent);
                }
            }, 2000);
        }
    }
}
