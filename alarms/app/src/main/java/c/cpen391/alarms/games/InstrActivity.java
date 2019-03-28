package c.cpen391.alarms.games;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import c.cpen391.alarms.R;

public class InstrActivity extends Activity {

    private Button back;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        back = (Button) findViewById(R.id.back_to_game);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainSpellingActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
