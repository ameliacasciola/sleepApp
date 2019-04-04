package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ColorSequenceGame extends AppCompatActivity {
    protected static CustomSharedPreference mPref;

    private boolean isAlarm;

    private boolean completed;
    private static final String CLIENT_ID = "e1cac6772536416882b7ee89591095ea";
    private static final String REDIRECT_URI = "http://localhost:8000/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;

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
        mPref = ((CustomApplication)context.getApplicationContext()).getShared();
        guessedColors = new ArrayList<>();
        chosenColors = new ArrayList<>();
        index = 0;
        score = 10;
        right = false;
        wrongLetter = false;
        completed = false;

        if (getIntent().hasExtra("isAlarm")){
            isAlarm = (boolean) getIntent().getSerializableExtra("isAlarm");
        } else {
            isAlarm = false;
        }

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

    @Override
    public void onBackPressed() {
        if (!completed && isAlarm) {
            Toast.makeText(context.getApplicationContext(), "Complete the game to stop the alarm!", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
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

                    ConnectionParams connectionParams =
                            new ConnectionParams.Builder(CLIENT_ID)
                                    .setRedirectUri(REDIRECT_URI)
                                    .showAuthView(true)
                                    .build();

                    SpotifyAppRemote.connect(context, connectionParams,
                            new Connector.ConnectionListener() {

                                @Override
                                public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                                    mSpotifyAppRemote = spotifyAppRemote;
                                    Log.e("SPOTIFY REMOTE", "Success, Onconnected" + mSpotifyAppRemote.isConnected());

                                    // Now you can start interacting with App Remote
                                    mSpotifyAppRemote.getPlayerApi().pause();
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    Log.e("SPOTIFY REMOTE", "Failure, Onconnected");

                                    // Something went wrong when attempting to connect! Handle errors here
                                }
                            });
                    completed = true;
                    updateScoreFunc();

                    Intent intent = new Intent(context, c.cpen391.alarms.home.class);
                    context.startActivity(intent);
                }
            }, 5000);
        }
    }

    private void updateScoreFunc() {
        //completion of get eggy with it game, gets points
        // upload to ../scores
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
        Call<ResponseBody> call = service.scorePost(mPref.getUserID(), "Color Sequence", score);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("POST", "post submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("POST", "Unable to submit post to API.");
            }
        });

        int temp = mPref.getScore()+score;
        // update profile total_score
        Call<ResponseBody> score = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class)
                .updateProfileScore(temp, mPref.getUserID());

        mPref.setScore(temp);

        score.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Log.e("PATCH", "patch submitted to API." + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("PATCH", "Unable to submit patch to API.");
            }
        });
    }
}
