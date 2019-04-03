package com.example.ameli.module2tutorials;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private LightView gameView;
    private boolean running;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, LightView gameView) {

        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        canvas = new Canvas();
    }

    public void setRunning(boolean isRunning) {
        running = isRunning;
    }

    @Override
    public void run() {
        while(running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    //update our view in the way we want to
                    this.gameView.draw(canvas);
                }
            }
            catch (Exception e) {}

            finally {
                if(canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) {e.printStackTrace();}

                }
            }

        }
    }
}
