package com.example.ameli.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Character {

    private Bitmap picture;
    private int x;
    private int y;
//    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
//    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public Character(Bitmap map) {
        picture = map;
        x = 50;
        y = 50;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(picture, x,y, null);
    }

    public void update() {
        y+=5;
        x+=5;
    }
}
