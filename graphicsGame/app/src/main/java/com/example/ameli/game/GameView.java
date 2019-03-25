package com.example.ameli.game;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.ameli.game.MainActivity.doneGame;

public class GameView extends View  {
    private Graphic graphic;
    public static List<Letter> word = new ArrayList<Letter>();
    private MainThread thread;
    public final Context context = getContext();


    public GameView(Context context) {
        super(context);
        graphic = new Graphic(BitmapFactory.decodeResource(getResources(),R.drawable.ball2));
        word = getRandomWord();
        graphic.x = graphic.screenWidth/2;
        graphic.y = graphic.screenHeight - 50;
    }

    public void update(float newX, float newY) {
        if(doneGame) {
            doneGame();
        }
        else {
            graphic.update(newX, newY, word);
            invalidate();
        }
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

        return letters;
    }

    public void doneGame() {
        Intent intent = new Intent(context, DoneActivity.class);
        context.startActivity(intent);
    }
}


