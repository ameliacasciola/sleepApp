package com.example.ameli.game;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity implements SensorEventListener {

    public static float xPos;
    public static float yPos;
    public static int curLetter;
    public static int score;

    public boolean ready = false;
    public static boolean doneGame;
    private SensorManager sensorManager;
    GameView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make sure orientation stays as portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //set the view to be our custom game view
        view = new GameView(this);
        setContentView(view);

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
                xPos =  - 10*sensorEvent.values[0];
                yPos =  10*sensorEvent.values[1];
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
        sensorManager.unregisterListener(this);
        super.onStop();
    }

}