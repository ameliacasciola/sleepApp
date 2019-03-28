package com.example.ameli.game;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.ameli.game.GameView.word;
import static com.example.ameli.game.Graphic.height;
import static com.example.ameli.game.Graphic.width;
import static com.example.ameli.game.MainActivity.doneGame;
import static com.example.ameli.game.MainActivity.yPos;

public class Letter {
    public boolean display;
    public char c;
    public float x;
    public float y;
    public static List<Pair<Integer, Integer>> taken = new ArrayList<>();
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public Letter(char c) {
        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        Random rand = new Random();
        int xQuad  = ((Math.abs(rand.nextInt()))%(screenWidth-100))+50;
        int yQuad  = ((Math.abs(rand.nextInt()))%(screenHeight-height-100))+50;
//        Pair<Integer, Integer> newPair = new Pair(xQuad,yQuad);
//
//        while(containsPair(taken, newPair)) {
//            rand = new Random();
//            xQuad  = ((Math.abs(rand.nextInt()))%(screenWidth-100))+50;
//            yQuad  = ((Math.abs(rand.nextInt()))%(screenHeight-height-100))+50;
//            newPair = new Pair(xQuad,yQuad);
//        }
//
//        taken.add(new Pair(xQuad, yQuad));

        this.c = c;
        this.display = true;
        this.x = xQuad;
        this.y = yQuad;
    }


    private boolean containsPair(List<Pair<Integer, Integer>> list, Pair<Integer, Integer> pair) {
        for(Pair p: list) {

            Rect r1 = new Rect((int)p.first-width, (int)p.second-height, (int)p.first+width, (int)p.second+height);	//x1,y1,x2,y2
            if (r1.contains((int) pair.first, (int) pair.second)) {
                return true;
            }
        }
        return false;
    }
}