package inkball;

import java.awt.*;

public class Hole {
    private float x;
    private float y;
    private float radius;
    private char color;
    public Hole(float x, float y, char color) {
        this.x = x;
        this.y = y;
        this.radius = 32;
        this.color = color;
    }
    public char getColor() {
        return color;
    }
    public void setColor(char color) {
        this.color = color;
    }
    public float getRadius() {
        return radius;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void checkBallCollision() {
        //
    }
}
