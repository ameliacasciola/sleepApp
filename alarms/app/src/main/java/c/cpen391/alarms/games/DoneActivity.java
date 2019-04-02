package c.cpen391.alarms.games;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.home;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static c.cpen391.alarms.games.MainSpellingActivity.score;

public class DoneActivity extends Activity {
    private Button goHome;
    private Button restart;
    private TextView scoreText;
    private Context context = this;
    protected static CustomSharedPreference mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        mPref = ((CustomApplication)context.getApplicationContext()).getShared();

        goHome = (Button) findViewById(R.id.home_from_words);
        restart = (Button) findViewById(R.id.restart_words);
        scoreText = (TextView) findViewById(R.id.score);

        scoreText.setText("Your score is: " + score);

        updateScoreFunc();

        //set button functionality to take you back to the main page
        goHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, home.class);
                context.startActivity(intent);
            }
        });


        //set button functionality to take you back to the tip exercise
        restart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainSpellingActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void updateScoreFunc() {
        //completion of get eggy with it game, gets points
        // upload to ../scores
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
        Call<ResponseBody> call = service.scorePost(mPref.getUserID(), "The Egg Did It", score);
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
