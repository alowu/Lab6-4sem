package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {
    private static final int MAX_RADIUS = 80;
    private static final int MIN_RADIUS = 30;
    private static final int MAX_SPEED = 20;
    private Field field;
    private int radius;
    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private double x;
    private double y;

    private int speed;
    private double speedX;
    private double speedY;
    private float g = (float) Math.random();

    public int getMaxSpeed() {
        return MAX_SPEED;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeedX() {
        return speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public int getRadius() {
        return radius;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public BouncingBall(Field field) {
        this.field = field;
        radius = new Double(Math.random() * (MAX_RADIUS -
                MIN_RADIUS)).intValue() + MIN_RADIUS;

        speed = new Double(Math.round(5 * MAX_SPEED / radius)).intValue();
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }

        double angle = Math.random() * 2 * Math.PI;

        speedX = 3 * Math.cos(angle);
        speedY = 3 * Math.sin(angle);

        color = new Color((float) Math.random(), (float) Math.random(),
                (float) Math.random());

        x = Math.random() * (field.getSize().getWidth() - 2 * radius) + radius;
        y = Math.random() * (field.getSize().getHeight() - 2 * radius) + radius;

        Thread thisThread = new Thread(this);

        thisThread.start();
    }

    public void run() {
        try {
            while (true) {
                field.canMove(this);
                field.push(this);
                if (x + speedX <= radius) {
                    speedX = -speedX;
                    x = radius;
                } else if (x + speedX >= field.getWidth() - radius) {
                    speedX = -speedX;
                    x = new Double(field.getWidth() - radius).intValue();
                } else if (y + speedY <= radius) {
                    speedY = -speedY;
                    y = radius;
                } else if (y + speedY >= field.getHeight() - radius) {
                    speedY = -speedY;
                    y = new Double(field.getHeight() - radius).intValue();
                } else {
                    x += speedX;
                    y += speedY;
                }

                Thread.sleep(16 - speed);
            }
        } catch (InterruptedException ex) {}
    }

    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius,
                2 * radius, 2 * radius);
        canvas.draw(ball);
        canvas.fill(ball);
    }
}
