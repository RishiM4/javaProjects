import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class test extends KeyAdapter {
    
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println("H");
        if (key == KeyEvent.VK_W) {
            System.out.println("You pressed W");
        }
        
    }

   
    public static void main(String[] args) {
        while (true) {
            
        }
    }
}
