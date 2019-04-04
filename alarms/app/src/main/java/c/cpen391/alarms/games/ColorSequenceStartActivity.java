package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import c.cpen391.alarms.CustomApplication;
import c.cpen391.alarms.R;

public class ColorSequenceStartActivity extends AppCompatActivity {
    private Button start;
    private Context context = this;
    private boolean isAlarm;
    private Integer volume;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_game_start);

        if (getIntent().hasExtra("isAlarm")){
            isAlarm = (boolean) getIntent().getSerializableExtra("isAlarm");
        } else {
            isAlarm = false;
        }

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

        start = (Button) findViewById(R.id.sequence_start_button);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, ColorSequenceGame.class);
                intent.putExtra("isAlarm", isAlarm);
                context.startActivity(intent);
            }
        });

    }
}
