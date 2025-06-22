import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class flappyBird {
    static JFrame frame = new JFrame();
    static Double obstacle1 = 0.0;
    static Double obstacle2 = 150.0;
    static Double obstacle3 = 300.0;
    static Double y = 200.0;
    static Double velocityY = 0.0;
    static int hole1 = 50;
    static int hole2 = 100;
    static int hole3 = 150;
    static  Set<Integer> pressedKeys = new HashSet<>();
    public static void main(String[] args) {
        boolean isJumping = false;
        frame.setSize(399, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.clearRect(0, 0, 400, 400);
                g.setColor(Color.getHSBColor(0.35f, 1f, 0.6f));
                
                g.fillRect((int)Math.round(obstacle1), 0, 40, 400);
                g.clearRect((int)Math.round(obstacle1), hole1, 40, 100);

               
                g.fillRect((int)Math.round(obstacle2), 0, 40, 400);
                g.clearRect((int)Math.round(obstacle2), hole2, 40, 100);
               
                g.fillRect((int)Math.round(obstacle3), 0, 40, 400);
                g.clearRect((int)Math.round(obstacle3), hole3, 40, 100);
                
                g.fillOval(100, (int)Math.round(y), 10, 10);
            }
        };
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            
            }

            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
            
        });

        frame.add(panel);
        frame.setBackground(Color.WHITE);
        frame.setSize(400, 400);
        while(true){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Random random = new Random();
             if (pressedKeys.contains(KeyEvent.VK_SPACE)&&velocityY>-2) {
                velocityY = -4.0;
                isJumping = true;
            }
            if (velocityY<=2&&isJumping) {
                velocityY+=0.2;
            }
            else if (velocityY<=3&&!isJumping){
                velocityY=3.0;
            }
            else{
                isJumping = false;
            }
            y+=velocityY;
            if (y<10&&y>=350) {
                y-=velocityY;
                
            }
            System.out.println(velocityY);
            obstacle1--;
            obstacle2--;
            obstacle3--;
            if (obstacle1 < -50) {
                obstacle1 = 400.0;
                hole1 = random.nextInt(350);
            }
            if (obstacle2<-50) {
                obstacle2 = 400.0;
                hole2 = random.nextInt(350);

            }
            if (obstacle3<-50) {
                obstacle3 = 400.0;
                hole3 = random.nextInt(350);

            }
            panel.repaint();
            
        }
    }
}