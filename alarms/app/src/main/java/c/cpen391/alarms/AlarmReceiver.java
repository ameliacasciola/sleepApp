package c.cpen391.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import c.cpen391.alarms.games.GraphicsActivity;
import c.cpen391.alarms.games.JumpingJacksGame;
import c.cpen391.alarms.games.MainSpellingActivity;
import c.cpen391.alarms.games.WalkingStepsGame;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        String game = (String) intent.getSerializableExtra("Game");
        Toast.makeText(context, game, Toast.LENGTH_SHORT).show();

        switch(game){
            case "Eggs-cercise":
                Intent newIntent = new Intent(context, WalkingStepsGame.class);
                context.startActivity(newIntent);
            case "Bubble Pop":
                Intent bubbleIntent = new Intent(context, GraphicsActivity.class);
                context.startActivity(bubbleIntent);
            case "Eggcellent Spelling":
                Intent spellIntent = new Intent(context, MainSpellingActivity.class);
                context.startActivity(spellIntent);
            case "Jumping Jacks":
                Intent jumpIntent = new Intent(context, JumpingJacksGame.class);
                context.startActivity(jumpIntent);
        }
    }
}
