package src;
import java.awt.Robot;
import java.awt.event.KeyEvent;
public class Test {
    public static void main(String[] args) {
        while(true) {
            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_SPACE);
                Thread.sleep(12    );

                robot.keyRelease(KeyEvent.VK_SPACE);
                Thread.sleep(12);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
                    