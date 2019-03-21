package c.cpen391.alarms;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Color.rgb;


public class GraphicsView extends View {
    private List<Circle> circles;
    private List<Circle> popped;
    private Timer timer;
    private int max_x;	// get width and height of the view in pixel
    private int max_y;

    private int maxSize = 300;
    private boolean started;
    private boolean timerDone;

    private int curScore;
    private int timeLeft;

    public GraphicsView(Context context) {
        super(context);
        circles = new ArrayList<Circle>();
        popped = new ArrayList<Circle>();

        int delay = 1000;
        int period = 1000;

        curScore = 0;
        timeLeft = 32;
        started = false;
        timerDone = false;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                setInterval();
            }
        }, delay, period);
    }

    public GraphicsView(Context context, AttributeSet set) {
        super(context);
        circles = new ArrayList<Circle>();
        popped = new ArrayList<Circle>();

        int delay = 1000;
        int period = 1000;

        curScore = 0;
        timeLeft = 32;
        started = false;
        timerDone = false;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                setInterval();
            }
        }, delay, period);
    }

    @Override
    public boolean onTouchEvent( MotionEvent event) {
        int Action = event.getAction();

        int XCoord = (int) event.getX();
        int YCoord = (int) event.getY();

        if (Action == MotionEvent.ACTION_DOWN) {
            boolean hitACircle = false;
            for (Circle c : circles) {
                Rect r1 = new Rect((c.x - c.radius), (c.y - c.radius), (c.x + c.radius), (c.y + c.radius));    //x1,y1,x2,y2

                //check if we're tapping on a circle we already
                if (r1.contains(XCoord, YCoord)) {
                    // touch was inside circle
                    c.incrRadius();

                    if(c.radius >= maxSize) {
                        //make the circle "explode"
                        popped.add(c);
                        curScore ++;
                        //make the circle go away
                        circles.remove(c);
                    }

                    hitACircle = true;
                    break;
                }
            }

            if (!hitACircle) {
                circles.add(new Circle(XCoord, YCoord, randomColor()));
            }
        }

        invalidate();
        return true;
    }


    @Override
    public void onDraw (Canvas canvas) {
        Paint paint = new Paint();        // create a new “paint brush” to draw on canvas
        max_x = getWidth() - 1;        // get width and height of the view in pixel
        max_y = getHeight() - 1;

        //draw background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawPaint(paint);

        if(!started) {
            paint.setTextSize(80.0f);
            paint.setColor(Color.WHITE);
            if(timeLeft>30) {
                canvas.drawText("READY", max_x / 2 - 200, max_y / 2, paint);
                canvas.drawText("SET", max_x / 2 - 200, max_y / 2 + 100, paint);
                canvas.drawText("GO!", max_x / 2 - 200, max_y / 2 + 200, paint);
            }
            else {
                started = true;
            }
        }
        else if(timerDone) {
            //display current score and top score
            paint.setTextSize(80.0f);
            paint.setColor(Color.WHITE);
            canvas.drawText("GAME OVER", max_x/2 - 200, max_y/2, paint);
            canvas.drawText("Score: "+ curScore, max_x/2 - 200, max_y/2+100, paint);
        }

        else {
            for (Circle p : popped) {
                paint.setColor(p.color);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paint.setTextSize(80);
                canvas.drawText("POP!", p.x, p.y, paint);

                popped.remove(p);
            }

            for (Circle c : circles) {
                paint.setColor(c.color);
                canvas.drawCircle(c.x, c.y, c.radius, paint);
            }

            paint.setTextSize(80.0f);
            paint.setColor(Color.WHITE);
            canvas.drawText("Score: "+ curScore, max_x - 350, max_y - 200, paint);
            canvas.drawText("Time Left: "+ timeLeft, max_x - 500, max_y - 100, paint);
        }
   }

   public int randomColor() {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        return rgb(r, g, b);
   }

    private final int setInterval() {
        if (timeLeft == 1) {
            timer.cancel();
            timerDone = true;
        }
        invalidate();
        return --timeLeft;
    }
}
