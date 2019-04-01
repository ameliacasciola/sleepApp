package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps_game_main);

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
                setContentView(R.layout.steps_game_faster);

                tv_steps_faster = (TextView) findViewById(R.id.tv_steps_faster);
                tv_steps_faster.setText(String.valueOf(step_count));
            }

            if(step_count > 10 || step_count == 10){
                setContentView(R.layout.steps_game_done);
                home = (Button) findViewById(R.id.home);

                try {
                    //set time in mili
                    Thread.sleep(5000);

                }catch (Exception e){
                    e.printStackTrace();
                }

                running = false;

                //completion of steping game, gets 100 points
                // upload to ../scores
                mPref = ((CustomApplication)context).getShared();
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

                // update profile total_score
                Call<ResponseBody> score = SleepClientInstance.getRetrofitInstance().create(SleepAPI.class)
                        .updateProfileScore(mPref.getScore()+100, mPref.getUserID());

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
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

//    public void popfinishmessage(){
//        popupmessage popUpMessage = new popupmessage();
//        popUpMessage.show(getSupportFragmentManager(), "Finish message");
//    }
}

