package entity;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class Field extends JPanel {
    private boolean paused;
    private ArrayList<BouncingBall> balls = new ArrayList<>();

    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field() {
        setBackground(Color.WHITE);
        repaintTimer.start();
    }

    public synchronized void push(BouncingBall ball1) {

        for (BouncingBall ball2 : balls) {
            if (!ball1.equals(ball2)) {
                double distance = Math.sqrt((ball1.getX() + ball1.getSpeedX() - ball2.getX() + ball2.getSpeedX()) * (ball1.getX() + ball1.getSpeedX() - ball2.getX() + ball2.getSpeedX()) +
                        (ball1.getY() + ball1.getSpeedY() - ball2.getY() + ball2.getSpeedY()) * (ball1.getY() + ball1.getSpeedY() - ball2.getY() + ball2.getSpeedY()));

                if (distance <= ball1.getRadius() + ball2.getRadius()) {
                    //speed at Ox
                    double buf1 = ((ball1.getRadius() - ball2.getRadius()) * ball1.getSpeedX() + 2 * ball2.getRadius() * ball2.getSpeedX()) /
                            (ball1.getRadius() + ball2.getRadius());
                    double buf2 = (2 * ball1.getRadius() * ball1.getSpeedX() + (ball2.getRadius() - ball1.getRadius()) * ball2.getSpeedX()) /
                            (ball1.getRadius() + ball2.getRadius());

                    if (buf1 < -1 * ball1.getMaxSpeed())
                        buf1 = -1 * ball1.getMaxSpeed();
                    else if (buf1 > ball1.getMaxSpeed())
                        buf1 = ball1.getMaxSpeed();
                    ball1.setSpeedX(buf1);

                    if (buf2 < -1 * ball1.getMaxSpeed())
                        buf2 = -1 * ball1.getMaxSpeed();
                    else if (buf2 > ball1.getMaxSpeed())
                        buf2 = ball1.getMaxSpeed();
                    ball2.setSpeedX(buf2);

                    //speed at Oy
                    buf1 = ((ball1.getRadius() - ball2.getRadius()) * ball1.getSpeedY() + 2 * ball2.getRadius() * ball2.getSpeedY()) /
                            (ball1.getRadius() + ball2.getRadius());
                    buf2 = (2 * ball1.getRadius() * ball1.getSpeedY() + (ball2.getRadius() - ball1.getRadius()) * ball2.getSpeedY()) /
                            (ball1.getRadius() + ball2.getRadius());

                    if (buf1 < -1 * ball1.getMaxSpeed())
                        buf1 = -1 * ball1.getMaxSpeed();
                    else if (buf1 > ball1.getMaxSpeed())
                        buf1 = ball1.getMaxSpeed();
                    ball1.setSpeedY(buf1);

                    if (buf2 < -1 * ball1.getMaxSpeed())
                        buf2 = -1 * ball1.getMaxSpeed();
                    else if (buf2 > ball1.getMaxSpeed())
                        buf2 = ball1.getMaxSpeed();
                    ball2.setSpeedY(buf2);


                    distance = Math.sqrt((ball1.getX() - ball2.getX()) * (ball1.getX() - ball2.getX()) +
                            (ball1.getY() - ball2.getY()) * (ball1.getY() - ball2.getY()));
                    while (distance < ball1.getRadius() + ball2.getRadius()) {
                        if (ball1.getRadius() < ball2.getRadius()) {
                            ball1.setX(ball1.getX() + ball1.getSpeedX());
                            ball1.setY(ball1.getY() + ball1.getSpeedY());
                        } else {
                            ball2.setX(ball2.getX() + ball2.getSpeedX());
                            ball2.setY(ball2.getY() + ball2.getSpeedY());
                        }
                        distance = Math.sqrt((ball1.getX() - ball2.getX()) * (ball1.getX() - ball2.getX()) +
                                (ball1.getY() - ball2.getY()) * (ball1.getY() - ball2.getY()));
                    }

                    ball1.setColor(new Color((float) Math.random(), (float) Math.random(),
                            (float) Math.random()));
                    ball2.setColor(new Color((float) Math.random(), (float) Math.random(),
                            (float) Math.random()));

                    URL url = null;
                    File file = new File("src/entity/sound.wav");
                    if (file.canRead()) {
                        try {
                            url = file.toURI().toURL();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                    AudioClip clip = Applet.newAudioClip(url);
                    clip.play();
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball : balls) {
            ball.paint(canvas);
        }
    }

    public void addBall() {
        balls.add(new BouncingBall(this));
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if (paused) {
            wait();
        }
    }
}
