package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.List;

import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.CustomSharedPreference;
import c.cpen391.alarms.R;
import c.cpen391.alarms.api.SleepAPI;
import c.cpen391.alarms.api.SleepClientInstance;
import c.cpen391.alarms.models.Alarm;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static c.cpen391.alarms.games.MainSpellingActivity.score;

public class SquatGame extends AppCompatActivity implements SensorEventListener{
    protected static CustomSharedPreference mPref;
    private SensorManager sensorManager;
    private TextView squat_steps;
    boolean running = false;
    private int step_count;
    private int init_count;
    private Context context = this;
    private boolean first = true;
    private Button home;

    private static final String CLIENT_ID = "e1cac6772536416882b7ee89591095ea";
    private static final String REDIRECT_URI = "http://localhost:8000/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.squat_game_main);
        mPref = ((CustomApplication)getApplicationContext()).getShared();

        squat_steps = (TextView) findViewById(R.id.squat_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null){
            sensorManager.registerListener(this,countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;

        //if you unregister the hardware will stop detecting steps
        //sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(first){
            init_count = (int)event.values[0];
            first = false;
        }

        if(running){
            step_count = (int)event.values[0] - init_count;

            squat_steps.setText(String.valueOf(step_count));

            if(step_count > 20 || step_count == 20){
                setContentView(R.layout.steps_game_done);
                home = (Button) findViewById(R.id.home);

                running = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(context, c.cpen391.alarms.home.class);
                        context.startActivity(intent);
                    }
                }, 5000);

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

                updateScoreFunc();
            }
        }

    }


    private void updateScoreFunc() {
        //completion of steping game, gets 100 points
        // upload to ../scores
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
        Call<ResponseBody> call = service.scorePost(mPref.getUserID(), "Squat Game", 20);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(context, "Upload Score No Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Error Upload Score", Toast.LENGTH_SHORT).show();
            }
        });

        int temp = mPref.getScore()+100;
        // update profile total_score
        Call<ResponseBody> score = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class)
                .updateProfileScore(temp, mPref.getUserID());

        mPref.setScore(temp);

        score.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(context.getApplicationContext(), "Update Profile Score No Response", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, c.cpen391.alarms.home.class);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "Erro Update Score", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

}

