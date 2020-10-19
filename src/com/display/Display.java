package com.display;

import IO.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public abstract class Display {
    private static boolean created = false;
    private static JFrame window;
    private static Canvas content;

    private static BufferedImage buffer;
    private static int[] bufferData;
    private static Graphics bufferGraphics;
    private static int clearColor;

    private static BufferStrategy bufferStrategy;

    private static float delta = 0;

    public static void create(int width , int heigth , String title , int _clearColor , int numBuffres) {
        if (created)
            return;
        window = new JFrame(title);
        //закрывается окно по нажатию кнопки
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content = new Canvas();
      /*     { public void paint(Graphics g){ --  если делать совсем простым способом, просто переопределя встроенный метод;
                super.paint(g);
                render(g);
            }
        };

       */
        //необходимо в convas передавать объект типо Dimension,т.е нельзя просто педедать два параметра типов int
        content.setPreferredSize(new Dimension(width , heigth));
        //нельзя менять размер окна
        window.setResizable(false);
        //мы не хотим чтоб наш конвас закрывал функциональный интерфейс нашей рамки, поэтому добавляем только во внутреннюю часть;
        window.getContentPane().add(content);
        //рамка ровно подберем размер, подстариваясь под наш контент(канвас)
        window.pack();
        //объявляем окно видимым
        window.setVisible(true);
        //окно создается по центру
        window.setLocationRelativeTo(null);


        buffer = new BufferedImage(width , heigth , BufferedImage.TYPE_INT_ARGB);
        //достаем из бафера массив int, в котором хранится информация о цвете нашего бафера
        bufferData = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
        bufferGraphics = buffer.getGraphics();
        ((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);

        clearColor = _clearColor;
        //имплиминтируем количество баферов внутри нашего контента
        content.createBufferStrategy(numBuffres);
        //мы создаем стратегию буферизацию и предаем количество баферов, который мы хотим
        //мы вытащили bafferStrategy
        bufferStrategy = content.getBufferStrategy();

        created = true;
    }

    public static void clear() {
        Arrays.fill(bufferData , clearColor);
    }


    /*   public static void render(){
           bufferGraphics.setColor(new Color(0xff0000ff));
           bufferGraphics.fillOval((int) (350 + (Math.sin(delta)*200)), 250, 100, 100);
           ((Graphics2D)bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
           bufferGraphics.fillOval((int) (350 + (Math.sin(delta)*200)), 400, 100, 100);

           ((Graphics2D)bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

           delta+=0.02f;
       }

     */
    public static void swapBuffers() {
        Graphics g = bufferStrategy.getDrawGraphics();
        g.drawImage(buffer , 0 , 0 , null);
        bufferStrategy.show();
        //т.е мы не сразу мы копировали Image на экран мы, его копируем в какай-то доп buffer внтури нашего конваса,
        // который уже умееют внутри конваса перерисовываать за нас так как нужно,имплиминтриуя мультибаферинг,
        // а количество баферов мы будем передавать в наш констуктор
       /* Graphics graphics=content.getGraphics();
        graphics.drawImage(buffer, 0, 0, null);

        */
    }

    public static Graphics2D getGraphics() {
        return (Graphics2D) bufferGraphics;
    }


    //уничтожает наше окно;
    public static void destroy() {
        if (!created)
            return;
        window.dispose();
    }
    public static void setTitle(String title){
        window.setTitle(title);
    }

    //может и не понадобиться
    public static void addInputListener(Input inputListener){
        window.add(inputListener);
    }






    /* public static void render(){
        content.repaint();
     }
     private static void render( Graphics g){
        g.setColor(Color.blue);
        g.fillOval(400-50, 300-50, 100, 100);
     }

     */


}
