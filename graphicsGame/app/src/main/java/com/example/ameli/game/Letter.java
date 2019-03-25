package com.example.ameli.game;

import java.util.Random;
import static com.example.ameli.game.Graphic.height;

public class Letter {
    public boolean display;
    public char c;
    public float x;
    public float y;

    public Letter(char c) {
        Random rand = new Random();
        int x = rand.nextInt(10000)%(Graphic.screenWidth - 50);
        int y = rand.nextInt(10000)%(Graphic.screenHeight - height - 50);

        this.c = c;
        this.display = true;
        this.x = x;
        this.y = y;
    }
}
