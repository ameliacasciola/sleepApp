package c.cpen391.alarms.games;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class MainSpellingActivity extends Activity implements SensorEventListener {

    public static float xPos;
    public static float yPos;
    public static int curLetter;
    public static int score;
    private boolean completed;
    public boolean ready = false;
    public static boolean doneGame;
    private SensorManager sensorManager;
    GameView view;
    private boolean isAlarm;

    private static final String CLIENT_ID = "e1cac6772536416882b7ee89591095ea";
    private static final String REDIRECT_URI = "http://localhost:8000/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make sure orientation stays as portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        completed = false;
        //set the view to be our custom game view
        view = new GameView(this);
        setContentView(view);

        if (getIntent().hasExtra("isAlarm")){
            isAlarm = (boolean) getIntent().getSerializableExtra("isAlarm");
        } else {
            isAlarm = false;
        }

        //set up sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Graphic.x = Graphic.screenWidth/2;
        Graphic.y = Graphic.screenHeight - 50;

        //we haven't started collecting letters yet
        curLetter = 0;

        //ready to start taking in sensor input
        doneGame = false;
        ready = true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        {
            //wait till setup complete
            while(!ready);

            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // get the new xPosition and yPosition based on the tilt
                xPos =  - 30*sensorEvent.values[0];
                yPos =  30*sensorEvent.values[1];
                //System.out.println("Updating with values of Y: "+ yPos + "and X: " + xPos);

                view.update(xPos, yPos);
            }

            if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //don't do anything if accuracy changes
    }

    @Override
    protected void onResume()
    {
        while(!ready);
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getApplicationContext(), connectionParams,
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
        sensorManager.unregisterListener(this);
        completed = true;
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (!completed && isAlarm) {
            Toast.makeText(getApplicationContext(), "Complete the game to stop the alarm!", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

}