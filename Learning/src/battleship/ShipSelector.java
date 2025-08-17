package battleship;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShipSelector {
    static String currentRotation = "UP";
    static int currentSize = 5;
    static boolean isFinished = false;
    public Ship[] getShips(JFrame frame) {
        JPanel panel = new JPanel();
        ArrayList<Button> buttons = new ArrayList<Button>();
        ArrayList<Button> usedCells = new ArrayList<Button>();
        panel.setBounds(100,100,600,600);
        panel.setLayout(new GridLayout(10,10));
        Button rotateButton = new Button("Rotate");
        
        rotateButton.setBounds(800,100,100,50);
        // 5, 4, 3, 3, 2
        for(int k = 1; k <= 100; k++) {
            buttons.add(new Button("Button " + k));
            buttons.get(k-1).setName(k + "");
            panel.add(buttons.get(k-1));
        }
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
            
        });
        for (Button button : buttons) {
            button.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    for (Button button : buttons) {
                        if (button.getBackground() == Color.LIGHT_GRAY) {
                            usedCells.add(button);
                            button.setBackground(Color.GRAY);
                        }
                        if (usedCells.size() == 5) {
                            currentSize = 4;
                        }
                        else if (usedCells.size() == 9) {
                            currentSize = 3;
                        }
                        else if (usedCells.size() == 15) {
                            currentSize = 2; 
                        }
                        else if (usedCells.size() == 17) {
                            frame.remove(panel);
                            frame.remove(rotateButton);
                        }
                        
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    for (Button button : buttons) {
                        if (button.getBackground() == Color.LIGHT_GRAY) {
                            button.setBackground(null);
                        }
                    }
                    switch (currentRotation) {
                        case "UP":
                            try {
                                int k = Integer.parseInt(button.getName());
                                if (k > (currentSize-1)*10) {
                                    for(int i = 0; i < currentSize; i++) {
                                        if (usedCells.contains(buttons.get(k-((i*10)+1)))) {
                                            return;
                                        }
                                    }     
                                    for(int i = 0; i < currentSize; i++) {
                                        buttons.get(k-((i*10)+1)).setBackground(Color.LIGHT_GRAY);
                                    }      
                                }
                            } 
                            catch (Exception f) {
                                f.printStackTrace();
                            }
                            break;
                    
                        case "RIGHT":
                            try {
                                int k = Integer.parseInt(button.getName());
                                int remainder = k % 10;
                                if (remainder <= 11-currentSize && remainder != 0) {
                                    for (int i = 0; i < currentSize; i++) {
                                        if (usedCells.contains(buttons.get(i+k-1))) {
                                            return;
                                        }
                                    }
                                    for (int i = 0; i < currentSize; i++) {
                                        buttons.get(i+k-1).setBackground(Color.LIGHT_GRAY);
                                    }
                                }
                            } 
                            catch (Exception f) {
                                f.printStackTrace();
                            }
                            break;
                        case "DOWN":
                            try {
                                int k = Integer.parseInt(button.getName());
                                if (k <= 100-((currentSize-1)*10)) {
                                    for(int i = 0; i < currentSize; i++) {
                                        if (usedCells.contains(buttons.get(k+((i*10)-1)))) {
                                            return;
                                        }
                                    }
                                    for(int i = 0; i < currentSize; i++) {
                                        buttons.get(k+((i*10)-1)).setBackground(Color.LIGHT_GRAY);
                                    }
                                }
                            } catch (Exception f) {
                            }
                            break;
                        case "LEFT":
                            try {
                                int k = Integer.parseInt(button.getName());
                                int remainder = k % 10;
                                if (remainder >= currentSize && remainder != 0) {
                                    for (int i = 0; i < currentSize; i++) {
                                        if (usedCells.contains(buttons.get(k-i-1))) {
                                            return;
                                        }
                                    }
                                    for (int i = 0; i < currentSize; i++) {
                                        buttons.get(k-i-1).setBackground(Color.LIGHT_GRAY);
                                    }
                                }
                            } 
                            catch (Exception f) {
                                f.printStackTrace();
                            }
                            break;
                        
                    }
                        
                    

                }

                @Override
                public void mouseExited(MouseEvent e) {
                    

                }
                
            });
            frame.add(rotateButton);
            frame.add(panel);
        }
        rotateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentRotation.equals("UP")) {
                    currentRotation = "RIGHT";
                }
                else if (currentRotation.equals("RIGHT")) {
                    currentRotation = "DOWN";
                }
                else if(currentRotation.equals("DOWN")){
                    currentRotation = "LEFT";
                }
                else if (currentRotation.equals("LEFT")){
                    currentRotation = "UP";
                }
            }
            
        });
        return new Ship[2];
    } 
    public static void main(String[] args) {
        JFrame frame = new JFrame("Battle Ship");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setLayout(null);
        
        System.err.println(new ShipSelector().getShips(frame).length);
        frame.setVisible(true);
        

    }
}
