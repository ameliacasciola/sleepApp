package c.cpen391.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import c.cpen391.alarms.games.GraphicsActivity;
import c.cpen391.alarms.games.JumpingJacksGame;
import c.cpen391.alarms.games.MainSpellingActivity;
import c.cpen391.alarms.games.WalkingStepsGame;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CLIENT_ID = "e1cac6772536416882b7ee89591095ea";
    private static final String REDIRECT_URI = "http://localhost:8000/callback/";
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    public void onReceive(final Context context, Intent intent) {
        String game = (String) intent.getSerializableExtra("Game");
        final String track_uri = (String) intent.getSerializableExtra("URI");
        Integer volume = (Integer) intent.getSerializableExtra("Volume");

        Toast.makeText(context, game, Toast.LENGTH_SHORT).show();

        if (game.equals("Eggs-cercise")){
            Intent newIntent = new Intent(context, WalkingStepsGame.class);
            newIntent.putExtra("isAlarm", true);
            newIntent.putExtra("Volume", volume);
            context.startActivity(newIntent);
        } else if(game.equals("Bubble Pop")){
            Intent bubbleIntent = new Intent(context, GraphicsActivity.class);
            bubbleIntent.putExtra("isAlarm", true);
            bubbleIntent.putExtra("Volume", volume);
            context.startActivity(bubbleIntent);
        } else if (game.equals("Eggcellent Spelling")){
            Intent spellIntent = new Intent(context, MainSpellingActivity.class);
            spellIntent.putExtra("isAlarm", true);
            spellIntent.putExtra("Volume", volume);
            context.startActivity(spellIntent);
        } else if (game.equals("Jumping Jacks")){
            Intent jumpIntent = new Intent(context, JumpingJacksGame.class);
            jumpIntent.putExtra("isAlarm", true);
            jumpIntent.putExtra("Volume", volume);
            context.startActivity(jumpIntent);
        }

        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(context, connectionParams,
                new Connector.ConnectionListener() {

                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.e("SPOTIFY REMOTE", "Success, Onconnected" + mSpotifyAppRemote.isConnected());

                        // Now you can start interacting with App Remote
                        mSpotifyAppRemote.getPlayerApi().play(track_uri);
                        mSpotifyAppRemote.getPlayerApi().setRepeat(1);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("SPOTIFY REMOTE", "Failure, Onconnected");

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }


}
