package com.example.ameli.game;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.List;
import static com.example.ameli.game.MainActivity.curLetter;
import static com.example.ameli.game.MainActivity.doneGame;

public class Graphic {

    private Bitmap picture;
    public static int width;
    public static int height;
    public static float x;
    public static float y;
    public static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean good;
    private boolean done;


    public Graphic(Bitmap map) {
        picture = map;
        width = picture.getWidth();
        height = picture.getHeight();
        x = screenWidth/2;
        y = screenHeight - picture.getHeight();
        good = true;
        done = false;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawPaint(paint);

        paint.setTextSize(80.0f);
        paint.setColor(Color.WHITE);

        if(!good) {
            restart(canvas);
        }
        canvas.drawBitmap(picture, x,y, null);

        if(GameView.word != null) {
            for (Letter c : GameView.word) {
                if(!c.display){
                    paint.setColor(Color.GREEN);
                }
                canvas.drawText(Character.toString(c.c), c.x, c.y, paint);
                paint.setColor(Color.WHITE);
            }
        }
    }

    public void update(float newX, float newY, List<Letter> word) {

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

        Rect r1 = new Rect((int)x, (int)y, (int)x+width, (int)y+width);	//x1,y1,x2,y2
            for(Letter l: word) {
                if(r1.contains ((int) l.x, (int)l.y)) {
                    if (((Character) (l.c)).equals((Character) (word.get(curLetter).c))) {
                        System.out.println("GOOD! You hit: " + l.c + " You were supposed to hit: " + word.get(curLetter).c);
                        if (l.display) {
                            l.display = false;
                            curLetter++;
                            if(curLetter == word.size()) {
                                //We won!
                                //display done gif and get outta here
                                doneGame = true;
                                break;
                            }
                        }
                        good = true;
                        break;
                    }
                    else {
                        //not the same and not already been seen
                        if(l.display) {
                            curLetter = 0;
                            for(Letter let: word){
                                let.display = true;
                            }
                            x = (screenWidth-width)/2;
                            y = screenHeight - height;

                            good = false;

                            //you hit a wrong letter!
                            System.out.println("BAD: You hit: " +l.c+ " You were supposed to hit: " + word.get(curLetter).c);
                            break;

                        }
                    }
                 }

        }

    }

    private void restart(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawPaint(paint);

        paint.setTextSize(80.0f);
        paint.setColor(Color.RED);
        canvas.drawText("WRONG LETTER!", screenWidth, screenHeight/2, paint);

    }

}
