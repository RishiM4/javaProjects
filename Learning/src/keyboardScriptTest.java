import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class keyboardScriptTest {
    static Boolean startMacro = false;
    static long currentTime = System.currentTimeMillis();
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(399, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(null);
        JTextField text = new JTextField();
        text.setBounds(100,100,100,100);
        frame.add(text);
        ArrayList<Long> time = new ArrayList<Long>();
        ArrayList<Integer> key = new ArrayList<Integer>();
        ArrayList<String> type = new ArrayList<String>();
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (Exception e) {
        }
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                
                

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==10) {
                    startMacro = true;
                    currentTime = System.currentTimeMillis();
                    return;
                }
                time.add(System.currentTimeMillis()-currentTime);
                key.add(e.getKeyCode());
                type.add("Pressed");
                //System.out.println("a");
                System.out.println(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                time.add(System.currentTimeMillis()-currentTime);
                key.add(e.getKeyCode());
                type.add("Released");
                //System.out.println("ajskdlksajdlksalkjd");
            }
            
        });
       
        

       
        while(true) {
            while(!startMacro) {
                
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            
            for(int k = 0; k < time.size(); k++) {
                if (Math.round(time.get(k)/10)*10==Math.round((System.currentTimeMillis()-currentTime)/10)*10) {
            
                    System.out.println("printing");
                    if (type.get(k)=="Pressed") {
                        robot.keyPress(key.get(k));
                        
                    }
                    else {
                        robot.keyRelease(key.get(k));
                    }
                    time.remove(k);
                    type.remove(k);
                    key.remove(k);
                }
                
            }
            
        }

    }
}
