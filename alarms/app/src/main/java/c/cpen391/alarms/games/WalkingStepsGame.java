package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.KeyEvent;
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

public class WalkingStepsGame extends AppCompatActivity implements SensorEventListener{
    protected static CustomSharedPreference mPref;
    private SensorManager sensorManager;
    private TextView tv_steps;
    private TextView tv_steps_faster;
    boolean running = false;
    private int step_count;
    private int init_count;
    private Context context = this;
    private boolean first = true;
    private boolean isAlarm;
    private Button home;

    private static final String CLIENT_ID = "e1cac6772536416882b7ee89591095ea";
    private static final String REDIRECT_URI = "http://localhost:8000/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;
    private boolean completed;
    private Integer volume;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        completed = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps_game_done);
        mPref = ((CustomApplication)getApplicationContext()).getShared();

        // Set Volume
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (getIntent().hasExtra("Volume")){
            volume = (Integer) getIntent().getSerializableExtra("Volume");
            int mapped_volume = (((volume + 1) *15 )/10);
            audio.setStreamVolume(audio.STREAM_MUSIC,
                    mapped_volume,
                    0);
        } else {
            audio.setStreamVolume(audio.STREAM_MUSIC,
                    10,
                    0);
        }

        tv_steps = (TextView) findViewById(R.id.tv_steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (getIntent().hasExtra("isAlarm")){
             isAlarm = (boolean) getIntent().getSerializableExtra("isAlarm");
        } else {
            isAlarm = false;
        }
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

            tv_steps.setText(String.valueOf(step_count));

            if(step_count > 3 || step_count == 3) {
                setContentView(R.layout.steps_game_faster);

                tv_steps_faster = (TextView) findViewById(R.id.tv_steps_faster);
                tv_steps_faster.setText(String.valueOf(step_count));
            }

            if(step_count > 10 || step_count == 10){
                setContentView(R.layout.steps_game_done);
                home = (Button) findViewById(R.id.homebutton);

                running = false;

                home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, c.cpen391.alarms.home.class);
                        context.startActivity(intent);
                    }
                });


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
                completed = true;
            }
        }

    }


    private void updateScoreFunc() {
        //completion of steping game, gets 100 points
        // upload to ../scores
        SleepAPI service = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class);
        Call<ResponseBody> call = service.scorePost(mPref.getUserID(), "Egg Run", 100);
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

    @Override
    public void onBackPressed() {
        if (!completed && isAlarm) {
            Toast.makeText(context.getApplicationContext(), "Complete the game to stop the alarm!", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

}

