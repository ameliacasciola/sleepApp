package c.cpen391.alarms.games;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.core.content.ContextCompat;
import android.widget.Toast;

import java.util.List;

import c.cpen391.alarms.R;

import static c.cpen391.alarms.games.MainSpellingActivity.curLetter;
import static c.cpen391.alarms.games.MainSpellingActivity.doneGame;
import static c.cpen391.alarms.games.MainSpellingActivity.score;

public class Graphic {

    private Bitmap picture;
    public static int width;
    public static int height;
    public static float x;
    public static float y;
    public static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static boolean good;
    Context context;
    private Bitmap instrButton;
    private Bitmap newWord;
    public static int infoX = screenWidth - 150;
    public static int infoY = screenHeight - 350;
    public static int newWordX = screenWidth - 150;
    public static int newWordY = screenHeight - 200;
    public static int buttonSz = 80;


    public Graphic(Bitmap map, Context context) {
        this.context = context;

        picture = Bitmap.createScaledBitmap(map, 200, 200, false);

        Bitmap b0 = BitmapFactory.decodeResource(context.getResources(),R.drawable.word);
        newWord = Bitmap.createScaledBitmap(b0, 120, 120, false);

        Bitmap b1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.instructions);
        instrButton = Bitmap.createScaledBitmap(b1, 120, 120, false);

        width = picture.getWidth();
        height = picture.getHeight();
        x = screenWidth/2;
        y = screenHeight - picture.getHeight();
        good = true;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawPaint(paint);

        paint.setTextSize(100.0f);
        paint.setColor(ContextCompat.getColor(context, R.color.pink));

        if(!good) {
            Toast.makeText(context, "Whoops, you hit a wrong letter!", Toast.LENGTH_LONG).show();
            good = true;
        }
        //canvas.drawBitmap(picture, x,y, null);

        if(GameView.word != null) {
            for (Letter c : GameView.word) {
                if(!c.display){
                    paint.setColor(Color.GREEN);
                }
                canvas.drawText(Character.toString(c.c), c.x, c.y, paint);
                paint.setColor(ContextCompat.getColor(context, R.color.pink));
            }
        }
        //add a button to pick a new word
        canvas.drawBitmap(newWord, newWordX, newWordY , null);

        //add a button to get instructions
        canvas.drawBitmap(instrButton, infoX, infoY, null);

        //display score

        //draw the ball
        canvas.drawBitmap(picture, x,y, null);
    }

    public void update(float newX, float newY, List<Letter> word) {
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        if(y+newY <= screenHeight - picture.getHeight() && y+newY >= 0) {
            y += newY;
        }
        else if (y+newY > screenHeight - picture.getHeight()){
            y = screenHeight - picture.getHeight();
        }
        else if(y+newY < 0) {
            y = 0;
        }
        if(x+newX <= screenWidth - picture.getWidth() && x+newX >= 0) {
            x += newX;
        }
        else if (x+newX > screenWidth - picture.getWidth()){
            x = screenWidth - picture.getWidth();
        }
        else if(x+newX < 0) {
            x = 0;
        }

        Rect r1 = new Rect((int)x, (int)y, (int)x+width, (int)y+height);	//x1,y1,x2,y2
        if(!doneGame) {
            for (Letter l : word) {
                if (r1.contains((int) l.x, (int) l.y)) {
                    if (((Character) (l.c)).equals((Character) (word.get(curLetter).c))) {
                        System.out.println("GOOD! You hit: " + l.c + " You were supposed to hit: " + word.get(curLetter).c);
                        if (l.display) {
                            l.display = false;
                            curLetter++;
                            if (curLetter == word.size()) {
                                //We won!
                                //display done gif and get outta here
                                doneGame = true;
                                Intent intent = new Intent(context, DoneActivity.class);
                                context.startActivity(intent);
                                break;
                            }
                        }
                        good = true;
                        break;
                    } else {
                        //not the same and not already been seen
                        if (l.display) {
                            curLetter = 0;
                            for (Letter let : word) {
                                let.display = true;
                            }
                            x = (screenWidth - width) / 2;
                            y = screenHeight - height;

                            if(score>0)
                                score--;
                            System.out.println(score);

                            good = false;

                            //you hit a wrong letter!
                            System.out.println("BAD: You hit: " + l.c + " You were supposed to hit: " + word.get(curLetter).c);
                            break;

                        }
                    }
                }

            }
        }

    }

}