package src;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Quiz {
    static List<String> lines;
    static JFrame frame = new JFrame();
    static JLabel label = new JLabel();
    static int questionNumber = -1;
    private static void updateFrame(){
        System.out.println("HI");
        if (questionNumber == -1) {
            Random random = new Random();
            questionNumber = random.nextInt(lines.size());
            while(questionNumber % 2 != 0){
                questionNumber = random.nextInt(lines.size());
            }
            label.setText(lines.get(questionNumber));
        }
        else{
            label.setText(lines.get(questionNumber+1));
            questionNumber = -1;
        }
    }
    public static void main(String[] args) {
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        label.setLocation(200,200);
        frame.add(label);
        
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println(e.getKeyCode());
                if (e.getKeyCode() == 0){
                    updateFrame();
                    System.out.println(questionNumber);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
        Path filePath = Paths.get("quizQuestions.txt");
        try {
			lines = Files.readAllLines(filePath);
            
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
