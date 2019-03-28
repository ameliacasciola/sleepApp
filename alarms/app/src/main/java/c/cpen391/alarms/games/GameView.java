package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import c.cpen391.alarms.R;

import static c.cpen391.alarms.games.Graphic.buttonSz;
import static c.cpen391.alarms.games.Graphic.infoX;
import static c.cpen391.alarms.games.Graphic.infoY;
import static c.cpen391.alarms.games.Graphic.newWordX;
import static c.cpen391.alarms.games.Graphic.newWordY;
import static c.cpen391.alarms.games.MainSpellingActivity.score;
import static c.cpen391.alarms.games.MainSpellingActivity.xPos;
import static c.cpen391.alarms.games.MainSpellingActivity.yPos;


public class GameView extends View  {
    private Graphic graphic;
    public static List<Letter> word = new ArrayList<Letter>();
    private MainThread thread;
    public final Context context = getContext();


    public GameView(Context context) {
        super(context);
        graphic = new Graphic(BitmapFactory.decodeResource(getResources(), R.drawable.ball2), context);
        word = getRandomWord();
        graphic.x = graphic.screenWidth/2;
        graphic.y = graphic.screenHeight - 50;
    }

    public void update(float newX, float newY) {
        graphic.update(newX, newY, word);
        invalidate();
    }

    @Override
    public boolean onTouchEvent( MotionEvent event) {
        int Action = event.getAction();

        int XCoord = (int) event.getX();
        int YCoord = (int) event.getY();

        if (Action == MotionEvent.ACTION_DOWN) {
            Rect r1 = new Rect(newWordX, newWordY, newWordX+buttonSz, newWordY+buttonSz);    //x1,y1,x2,y2

            //check if we hit the new word button
            if (r1.contains(XCoord, YCoord)) {
                //reset the word and the ball
                word = getRandomWord();
                graphic.x = graphic.screenWidth/2;
                graphic.y = graphic.screenHeight - 50;
            }

            Rect r2 = new Rect(infoX, infoY, infoX+buttonSz, infoY+buttonSz);    //x1,y1,x2,y2
            //check if we hit the instructions button
            if (r2.contains(XCoord, YCoord)) {
                //display instructions
                Intent intent = new Intent(context, InstrActivity.class);
                context.startActivity(intent);
            }
        }

        update(xPos, yPos);
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        graphic.draw(canvas);
    }

    private List<Letter> getRandomWord() {
        Random rand = new Random();
        int n = rand.nextInt(1000);
        List<Letter> letters = new ArrayList<Letter>();

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("unsortedWords.txt")));
            String s;

            int i=0;
            while ((s = br.readLine()) != null && i<n-1) { // read a line
                i++;
            }
            char[] chars = s.toCharArray();
            for(char c: chars) {
                letters.add(new Letter(c));
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            System.out.println("ERROR!");

        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("ERROR!");
        }
        for(Letter l: letters) {
            System.out.println(l.c);
        }

        System.out.println(score);

        score = letters.size();
        return letters;
    }

}
