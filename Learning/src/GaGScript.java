
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Thread.sleep;

import javax.swing.JFrame;
public class GaGScript implements MouseMotionListener{
    public void mouseTracker() {
        JFrame frame = new JFrame();
        frame.addMouseMotionListener(this);
        frame.setAlwaysOnTop(true);
        frame.requestFocus();
       
        frame.setVisible(true);
    }
    public static void click(Robot robot) {
        try {
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(500);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (Exception e) {
        }
    }
    public static void main(String[] args) {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
            Robot robot = new Robot();
            
            robot.keyPress(KeyEvent.VK_W);
            Thread.sleep(500);
            robot.keyRelease(KeyEvent.VK_W);
            Thread.sleep(100);
            robot.keyPress(KeyEvent.VK_S);
            Thread.sleep(500);
            robot.keyRelease(KeyEvent.VK_S);
            Thread.sleep(100);
            robot.keyPress(KeyEvent.VK_A);
            Thread.sleep(500);
            robot.keyRelease(KeyEvent.VK_A);
            Thread.sleep(100);
            robot.keyPress(KeyEvent.VK_D);
            Thread.sleep(500);
            robot.keyRelease(KeyEvent.VK_D);

            robot.mouseMove(510,110);
            Thread.sleep(1000);
            robot.mouseMove(500,100);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(100);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(750);
            robot.mouseMove(500,500);
            sleep(500);
            robot.keyPress(KeyEvent.VK_W);
            Thread.sleep(250);
            robot.keyRelease(KeyEvent.VK_W);
            sleep(500);
            robot.keyPress(KeyEvent.VK_E);
            Thread.sleep(250);
            robot.keyRelease(KeyEvent.VK_E);
            sleep(500);
            robot.keyPress(KeyEvent.VK_E);
            Thread.sleep(250);
            robot.keyRelease(KeyEvent.VK_E);
            sleep(500);
            robot.keyPress(KeyEvent.VK_E);
            Thread.sleep(250);
            robot.keyRelease(KeyEvent.VK_E);
            
            robot = new Robot();
            sleep(1000);

            robot.mouseMove(700,600);
            
            sleep(500);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(100);

            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot = new Robot();

            sleep(5000);

            robot.mouseWheel(50);

            Thread.sleep(500);
             
            robot.mouseWheel(50);

            Thread.sleep(500);
             
            robot.mouseWheel(50);

            Thread.sleep(500);
             
            robot.mouseWheel(50);

            Thread.sleep(2500);

            robot.mouseMove(700, 550);

            sleep(500);
            click(robot);
            sleep(500);
            robot.mouseMove(575, 665);
            sleep(500);
            robot.mouseMove(573, 666);
            for (int i = 0; i < 5; i++) {
                click(robot);
                sleep(250);
            }
            robot.mouseMove(575, 675);
            for (int i = 0; i < 15; i++) {
                robot = new Robot();

                sleep(500);

                robot.mouseMove(700, 300);
                
                sleep(1000);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(500);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                
                sleep(1000);
                robot.mouseMove(575, 663-(i*2));
                sleep(500);
                robot.mouseMove(573, 655-(i*2));

                sleep(500);
                for (int j = 0; j < 5; j++) {
                    click(robot);
                    sleep(250);
                }
                sleep(500);
                
            }
                
            
            Thread.sleep(5000);

            robot.mouseMove(1025, 225);

            Thread.sleep(1000);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(500);

            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            
            Thread.sleep(500);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(500);

            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            
            Thread.sleep(500);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(500);

            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            
            Thread.sleep(500);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(500);

            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            
            Thread.sleep(500);

            robot.mouseMove(900,500);   
            
            Thread.sleep(1000);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(500);

            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(1000);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(500);

            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(1000);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(500);

            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(1000);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);

            Thread.sleep(500);

            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);


        } catch (Exception e) {
            e.printStackTrace();
        }

            }
        }, 1000, 300000); 
                
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("mouse moved to " + e.getX() + ", "+ e.getY());
        
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println("mouse moved to " + e.getX() + ", "+ e.getY());
    }
}
