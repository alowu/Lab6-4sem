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
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class Field extends JPanel {
    private boolean paused;
    private ArrayList<BouncingBall> balls = new ArrayList<>();

    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
// При создании его экземпляра используется анонимный класс,
// реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
// Задача обработчика события ActionEvent - перерисовка окна
            repaint();
        }
    });
    // Конструктор класса
    public Field() {
// Установить цвет заднего фона белым
        setBackground(Color.WHITE);
// Запустить таймер
        repaintTimer.start();
    }

    public synchronized void push(BouncingBall ball1) {

        for(BouncingBall ball2: balls)
        {
            if(!ball1.equals(ball2) )
            {
                double distance = Math.sqrt((ball1.getX() + ball1.getSpeedX() - ball2.getX() + ball2.getSpeedX())*(ball1.getX() + ball1.getSpeedX() - ball2.getX() + ball2.getSpeedX()) +
                        (ball1.getY() + ball1.getSpeedY() - ball2.getY() + ball2.getSpeedY())*(ball1.getY() + ball1.getSpeedY() - ball2.getY() + ball2.getSpeedY()));

                if( distance <= ball1.getRadius() + ball2.getRadius())
                {
                    //speed at Ox
                    double buf1 = ( (ball1.getRadius() - ball2.getRadius())*ball1.getSpeedX() + 2*ball2.getRadius()*ball2.getSpeedX())/
                            (ball1.getRadius() + ball2.getRadius());
                    double buf2 = (2*ball1.getRadius()*ball1.getSpeedX()+(ball2.getRadius() - ball1.getRadius())*ball2.getSpeedX())/
                            (ball1.getRadius() + ball2.getRadius());

                    if(buf1 < -1*ball1.getMaxSpeed())
                        buf1 = -1*ball1.getMaxSpeed();
                    else
                    if(buf1 > ball1.getMaxSpeed())
                        buf1 = ball1.getMaxSpeed();
                    ball1.setSpeedX(buf1);

                    if(buf2 < -1*ball1.getMaxSpeed())
                        buf2 = -1*ball1.getMaxSpeed();
                    else
                    if(buf2 > ball1.getMaxSpeed())
                        buf2 = ball1.getMaxSpeed();
                    ball2.setSpeedX(buf2);


                    //speed at Oy
                    buf1 = ( (ball1.getRadius() - ball2.getRadius())*ball1.getSpeedY() + 2*ball2.getRadius()*ball2.getSpeedY())/
                            (ball1.getRadius() + ball2.getRadius());
                    buf2 = (2*ball1.getRadius()*ball1.getSpeedY()+(ball2.getRadius() - ball1.getRadius())*ball2.getSpeedY())/
                            (ball1.getRadius() + ball2.getRadius());

                    if(buf1 < -1*ball1.getMaxSpeed())
                        buf1 = -1*ball1.getMaxSpeed();
                    else
                    if(buf1 > ball1.getMaxSpeed())
                        buf1 = ball1.getMaxSpeed();
                    ball1.setSpeedY(buf1);

                    if(buf2 < -1*ball1.getMaxSpeed())
                        buf2 = -1*ball1.getMaxSpeed();
                    else
                    if(buf2 > ball1.getMaxSpeed())
                        buf2 = ball1.getMaxSpeed();
                    ball2.setSpeedY(buf2);


                    distance = Math.sqrt((ball1.getX() - ball2.getX() )*(ball1.getX() - ball2.getX()) +
                            (ball1.getY() - ball2.getY() )*(ball1.getY() - ball2.getY() ));
                    while(distance < ball1.getRadius() + ball2.getRadius())
                    {
                        if(ball1.getRadius() < ball2.getRadius()){
                            ball1.setX(ball1.getX() + ball1.getSpeedX());
                            ball1.setY(ball1.getY() + ball1.getSpeedY());
                        }
                        else{
                            ball2.setX(ball2.getX() + ball2.getSpeedX());
                            ball2.setY(ball2.getY() + ball2.getSpeedY());
                        }
                        distance = Math.sqrt((ball1.getX() - ball2.getX() )*(ball1.getX() - ball2.getX()) +
                                (ball1.getY() - ball2.getY() )*(ball1.getY() - ball2.getY() ));
                    }

                    ball1.setColor(new Color((float)Math.random(), (float)Math.random(),
                             (float)Math.random()));
                    ball2.setColor(new Color((float)Math.random(), (float)Math.random(),
                             (float)Math.random()));

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




    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
// Вызвать версию метода, унаследованную от предка
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
// Последовательно запросить прорисовку от всех мячей из списка
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }
    // Метод добавления нового мяча в список
    public void addBall() {
//Заключается в добавлении в список нового экземпляра BouncingBall
// Всю инициализацию положения, скорости, размера, цвета
// BouncingBall выполняет сам в конструкторе
        balls.add(new BouncingBall(this));
    }

    // Метод синхронизированный, т.е. только один поток может
// одновременно быть внутри
    public synchronized void pause() {
// Включить режим паузы
        paused = true;
    }
/*
    public synchronized void greenPause() {
        pausedGreen = true;

    }
*/

    public synchronized void resume() {
// Выключить режим паузы
        paused = false;
        //   pausedGreen = false;

        notifyAll();
    }
    // Синхронизированный метод проверки, может ли мяч двигаться
// (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall ball) throws
            InterruptedException {
        if (paused) {
            wait();
        }
//        if( pausedGreen && ball.getColor().getGreen() > ball.getColor().getRed() + ball.getColor().getBlue() )
//        {
//            ball.wait();
//        }

    }
}
