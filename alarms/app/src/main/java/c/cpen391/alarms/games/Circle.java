package c.cpen391.alarms.games;

public class Circle {
    public int x;
    public int y;
    public int radius;
    public int color;

    public Circle(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.radius = 20;
    }
    public void incrRadius() {
        this.radius += 20;
    }
}
