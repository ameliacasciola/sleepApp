package com.example.ameli.module2tutorials;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GraphicsActivity extends AppCompatActivity {
    private Context context = this;
    private Button toTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_outer);

        toTip = (Button) findViewById(R.id.go_to_tip_button);

        //set button functionality to take you back to the tip exercise
        toTip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

    }
}