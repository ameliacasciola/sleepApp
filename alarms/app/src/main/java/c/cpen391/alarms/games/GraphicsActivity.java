package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import c.cpen391.alarms.R;
import c.cpen391.alarms.home;

public class GraphicsActivity extends AppCompatActivity {
    private Context context = this;
    private Button goHome;
    private boolean completed;
    private boolean isAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_outer);
        completed = false;
        if (getIntent().hasExtra("isAlarm")){
            isAlarm = (boolean) getIntent().getSerializableExtra("isAlarm");
        } else {
            isAlarm = false;
        }

        goHome = (Button) findViewById(R.id.home_button);

        //set button functionality to take you back to the tip exercise
        goHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String CLIENT_ID = "e1cac6772536416882b7ee89591095ea";
                final String REDIRECT_URI = "http://localhost:8000/callback/";

                ConnectionParams connectionParams =
                        new ConnectionParams.Builder(CLIENT_ID)
                                .setRedirectUri(REDIRECT_URI)
                                .showAuthView(true)
                                .build();

                SpotifyAppRemote.connect(context, connectionParams,
                        new Connector.ConnectionListener() {

                            SpotifyAppRemote mSpotifyAppRemote;

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

                completed = true;
                Intent intent = new Intent(context, home.class);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (!completed && isAlarm) {
            Toast.makeText(context.getApplicationContext(), "Complete the game to stop the alarm!", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}