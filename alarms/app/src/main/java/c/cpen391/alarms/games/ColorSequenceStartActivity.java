package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import c.cpen391.alarms.R;

public class ColorSequenceStartActivity extends AppCompatActivity {
    private Button start;
    private Context context = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sequence_game_start);

        start = (Button) findViewById(R.id.sequence_start_button);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, ColorSequenceGame.class);
                context.startActivity(intent);
            }
        });

    }
}
