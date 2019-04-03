package com.example.ameli.module2tutorials;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class LightView extends View  {
    private static int lightDelay = 1000;
    private static int pauseDelay = 500;
    private Context context;

    private int numLetters;
    private String[] letters;
    private static String curLetter;
    private MainThread thread;

    public LightView(Context context) {
        super(context);
        init(context);
    }

    public LightView(Context context, AttributeSet attributes) {
        super(context, attributes);
        init(context);
    }

    public LightView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        //update to hold the whole alphabet
        this.numLetters = 1;
        this.letters = new String[]{"1010"};
        this.context = context;
        this.curLetter = getRandomLetter();

//        canvas = new Canvas();
//        getHolder().addCallback(this);
//        thread = new MainThread(getHolder(), this);
//        setFocusable(true);
        invalidate();
    }

    private String getRandomLetter() {
        Random r = new Random();
        return letters[r.nextInt()% numLetters];
    }

    public static String getCurLetter() {
        return curLetter;
    }

    public static void update(Canvas canvas) {

        Paint paint = new Paint();  // create a new “paint brush” to draw on canvas

        for (char c : curLetter.toCharArray()) {
            System.out.println(c);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            if (c == '1') {
                // PAUSE 1 SECONDS
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            paint.setColor(Color.BLACK);
            canvas.drawPaint(paint);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

//    @Override
//    public void draw(Canvas canvas) {
//        super.draw(canvas);
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        thread.setRunning(true);
//        thread.start();
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }
}
