package battleship;
import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class ShipSelector {
    public void createBorder(JButton button, int k) {
        if (currentRotation.equals("UP")||currentRotation.equals("DOWN")) {
            if (k==0) {
                MatteBorder border = new MatteBorder(2, 3, 0, 3, Color.BLACK);
                button.setBorder(border);
                }
                else if (k==currentSize-1) {
                MatteBorder border = new MatteBorder(0, 3, 2, 3, Color.BLACK);
                button.setBorder(border);
                }
                else {
                MatteBorder border = new MatteBorder(0, 3, 0, 3, Color.BLACK);
                button.setBorder(border);
            }
        }
        else {
            if (k==0) {
                MatteBorder border = new MatteBorder(2, 3, 2, 0, Color.BLACK);
                button.setBorder(border);
                }
                else if (k==currentSize-1) {
                MatteBorder border = new MatteBorder(2, 0, 2, 3, Color.BLACK);
                button.setBorder(border);
                }
                else {
                MatteBorder border = new MatteBorder(2, 0, 2, 0, Color.BLACK);
                button.setBorder(border);
            }
        }
    }
    String currentRotation = "UP";
    int currentSize = 5;
    boolean isFinished = false;
    JButton ship5[] = new JButton[5];
    JButton ship4[] = new JButton[4];
    JButton ship3[] = new JButton[3];
    JButton ship2[] = new JButton[3];
    JButton ship1[] = new JButton[2];
    public JFrame getShips(JFrame frame) {
        JPanel panel = new JPanel();
        ArrayList<JButton> buttons = new ArrayList<JButton>();
        ArrayList<JButton> usedCells = new ArrayList<JButton>();
        
        panel.setBounds(100,100,600,600);
        panel.setLayout(new GridLayout(10,10,-1,0));
        Button rotateButton = new Button("Rotate");
        rotateButton.setBounds(800,100,100,50);
        // 5, 4, 3, 3, 2
        for(int k = 1; k <= 100; k++) {
            buttons.add(new JButton(""+k));
            buttons.get(k-1).setName(k + "");
            
            panel.add(buttons.get(k-1));
        }
        
        for (JButton button : buttons) {
            button.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    
                    for (JButton button : buttons) {
                        
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
                            isFinished=true;
                            if (frame.getMouseListeners().length!=0) {
                                frame.removeMouseListener(frame.getMouseListeners()[0]);
                            }
                            
                            button.removeMouseListener(button.getMouseListeners()[0]);
                            button.removeMouseListener(button.getMouseListeners()[0]);


                        }
                        if (button.getBackground() == Color.LIGHT_GRAY) {
                            if (currentSize==5) {
                                for(int k = 0; k < ship5.length; k++) {
                                    if (ship5[k]==null) {
                                        createBorder(button,k);
                                        ship5[k] = button;
                                        break;
                                    }
                                }
                            }
                            else if (currentSize==4) {
                                for(int k = 0; k < ship4.length; k++) {
                                    if (ship4[k]==null) {
                                        createBorder(button,k);
                                        ship4[k] = button;
                                        break;
                                    }
                                }
                            }
                            else if (currentSize==4) {
                                for(int k = 0; k < ship4.length; k++) {
                                    if (ship4[k]==null) {
                                        createBorder(button,k);
                                        ship4[k] = button;
                                        break;
                                    }
                                }
                            }
                            else if (currentSize==3&&usedCells.size()<12) {
                                for(int k = 0; k < ship3.length; k++) {
                                    if (ship3[k]==null) {
                                        createBorder(button,k);
                                        ship3[k] = button;
                                        break;
                                    }
                                }

                            }
                            else if (currentSize==3) {
                                for(int k = 0; k < ship2.length; k++) {
                                    if (ship2[k]==null) {
                                        createBorder(button,k);
                                        ship2[k] = button;
                                        break;
                                    }
                                }

                            }
                            else {
                                for(int k = 0; k < ship1.length; k++) {
                                    if (ship1[k]==null) {
                                        createBorder(button,k);
                                        ship1[k] = button;
                                        break;
                                    }
                                }
                            }
                            
                           
                            button.setBackground(Color.GRAY);
                            usedCells.add(button);
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    for (JButton button : buttons) {
                        if (button.getBackground() == Color.LIGHT_GRAY) {
                            button.setBackground(null);
                        }
                    }
                    if (isFinished) {
                        for(int k = 0; k < ship3.length; k++) {
                            System.err.println(ship3[k].getName());
                        }
                        return;
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
                                if (remainder >= currentSize) {
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
        return frame;
    } 
    public static void main(String[] args) {
        JFrame frame = new JFrame("Battle Ship");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.setLayout(null);
        new ShipSelector().getShips(frame);
        frame.setVisible(true);
        

    }
}
