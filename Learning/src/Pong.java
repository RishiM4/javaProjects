import javax.swing.*;
import java.awt.*;
import java.lang.Math;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
public class Pong {
    static double currentX = 0;
    static double currentY = 0;
    private static int paddle1Y = 150;
    private static int paddle2Y = 150;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 60;
    private static final int PADDLE_SPEED = 10;
    private static final Set<Integer> pressedKeys = new HashSet<>();
    public static void drawCircle(Graphics g) {
        //g.drawOval(0, 0, 10, 10);
        g.setColor(Color.BLACK);
        g.fillOval(0, 0, 10, 10);
    }
    public static Point moveCircle(Graphics g, double velocityX, double velocityY) {
        currentX += velocityX;
        currentY += velocityY;
        
        return new Point((int)Math.round(currentX),(int)Math.round(currentY));
    }
    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Draw a Circle");
        JPanel panel = new JPanel() {
            
            @Override
            protected void paintComponent(Graphics g) {
                drawCircle(g);
                
            }
        };
        panel.setLocation(0,0);
        frame.add(panel);
        frame.setSize(frame.getMaximumSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        boolean gameIsRunning = true;
        double velocityX = 0.5;
        double velocityY = 0.5;
        frame.setSize(800,400);
        Random random = new Random();
        
        while (gameIsRunning) {
            if (currentX <0||currentX>777) {
                velocityX = velocityX*(-random.nextDouble(0.9, 1.1));
            }
            if (currentY <0||currentY>353) {
                velocityY = velocityY*(-random.nextDouble(0.9, 1.1));
                System.out.println(velocityY);
            }
            panel.setLocation(moveCircle(null, velocityX, velocityY));
            Thread.sleep(5);
        }
        
    }
}
