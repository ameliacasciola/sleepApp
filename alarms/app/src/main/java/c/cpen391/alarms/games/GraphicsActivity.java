package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
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

import static c.cpen391.alarms.games.GraphicsView.timerDone;

public class GraphicsActivity extends AppCompatActivity {
    private Context context = this;
    private Button goHome;
    private boolean completed;
    private boolean isAlarm;
    private Integer volume;

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

        // Set Volume
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

        goHome = (Button) findViewById(R.id.home_button);
        //goHome.setVisibility(View.GONE);

        //set button functionality to take you back to the tip exercise
        goHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(timerDone) {

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
                else {
                    Toast.makeText(context.getApplicationContext(), "Complete the game to stop the alarm!", Toast.LENGTH_SHORT).show();
                }
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