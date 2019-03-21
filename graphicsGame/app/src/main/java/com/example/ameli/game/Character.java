package com.example.ameli.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Character {

    private Bitmap picture;
    private int x;
    private int y;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int moveByY = 10;
    private int moveByX = 10;


    public Character(Bitmap map) {
        picture = map;
        x = 50;
        y = 50;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(picture, x,y, null);
    }

    public void update() {
        if(y > screenHeight - picture.getHeight() || y < 0) {
            moveByY = moveByY * -1;
            System.out.println(y);
            System.out.println(moveByY);
        }
        if(x > screenWidth - picture.getWidth() || x < 0) {
            moveByX = moveByX * -1;
            System.out.println(x);
            System.out.println(moveByX);
        }
        y+= moveByY;
        x+= moveByX;
    }
}
