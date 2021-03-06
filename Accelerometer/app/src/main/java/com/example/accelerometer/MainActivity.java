 package com.example.accelerometer;

import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private TextView tv_steps;
    private TextView tv_steps_faster;
    boolean running = false;
    private int step_count;
    private int init_count;
    private Context context = this;
    private boolean first = true;
    private Button home;

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
                setContentView(R.layout.activity_faster);
                tv_steps_faster = (TextView) findViewById(R.id.tv_steps_faster);
                tv_steps_faster.setText(String.valueOf(step_count));
            }

            if(step_count > 6 || step_count == 6){
                Toast.makeText(this, "100 steps done!", Toast.LENGTH_LONG).show();
                setContentView(R.layout.activity_done);
                home = (Button) findViewById(R.id.home);

                popfinishmessage();
                running = false;
            }
        }

    }

    @Override
     public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    public void popfinishmessage(){
        popupmessage popUpMessage = new popupmessage();
        popUpMessage.show(getSupportFragmentManager(), "Finish message");
    }
}
