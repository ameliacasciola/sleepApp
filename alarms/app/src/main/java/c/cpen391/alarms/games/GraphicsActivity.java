package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import c.cpen391.alarms.R;
import c.cpen391.alarms.home;

public class GraphicsActivity extends AppCompatActivity {
    private Context context = this;
    private Button goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_outer);

        goHome = (Button) findViewById(R.id.home_button);

        //set button functionality to take you back to the tip exercise
        goHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, home.class);
                context.startActivity(intent);
            }
        });

    }
}