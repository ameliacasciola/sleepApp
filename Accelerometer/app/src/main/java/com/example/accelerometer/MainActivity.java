 package com.example.accelerometer;

import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity implements SensorEventListener{

    SensorManager sensorManager;

    TextView tv_steps;

    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_steps = (TextView) findViewById(R.id.tv_steps);

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
             Toast.makeText(this, "Sensor not found!", Toast.LENGTH_SHORT).show();
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
        if(running){
            tv_steps.setText(String.valueOf(event.values[0]));
        }

        if(event.values[0] == 100){
            Toast.makeText(this, "100 steps done!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
     public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}
