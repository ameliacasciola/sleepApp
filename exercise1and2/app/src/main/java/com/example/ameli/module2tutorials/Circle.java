package com.example.ameli.module2tutorials;

public class Circle {
    public int x;
    public int y;
    public int radius;
    public int color;

    public Circle(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.radius = 80;
    }
    public void incrRadius() {
        this.radius += 40;
    }
}
