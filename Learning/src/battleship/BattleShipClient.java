package battleship;

import java.awt.Button;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.MatteBorder;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class BattleShipClient implements Runnable{
    private static final String REGEX = "!:!";
    boolean shipSelectionFinished;
    boolean isFinished = false;
    JButton ship5[] = new JButton[5];
    JButton ship4[] = new JButton[4];
    JButton ship3[] = new JButton[3];
    JButton ship2[] = new JButton[3];
    JButton ship1[] = new JButton[2];
    BufferedReader reader;
    BufferedWriter writer;
    JFrame frame = new JFrame("BattleShip");
    JButton refreshButton = new JButton("Refresh");
    Socket socket;
    String currentRotation = "UP";
    int currentSize = 5;
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
    @Override
    public void run() {
        try {
            String currentString;
            
            while ((currentString = reader.readLine()) != null) {
                System.err.println(currentString);
                String currentRegex = currentString.split(REGEX)[0];
                System.err.println(currentRegex);
                System.err.println("");
                if (currentRegex.equals("0001")) {
                    String name = JOptionPane.showInputDialog("What is your name?");
                    while (name == null||name.equals("")||name.contains(",")||name.contains("!:!")) {
                        name = JOptionPane.showInputDialog("What is your name?");
                    }
                    write("0002"+REGEX+name);
                    break;
                }
            }
            write("0003"+REGEX);
            refreshButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    write("0003"+REGEX); 
                }
                
            });
            refreshButton.setBounds(100,100,100,100);
            JScrollPane pane = new JScrollPane();
            pane.setBounds(200, 100, 250, 500);
            frame.add(refreshButton);
            frame.repaint();
            frame.revalidate();

            while ((currentString = reader.readLine()) != null) {
                System.err.println(currentString);
                String currentRegex = currentString.split(REGEX)[0];
                System.err.println(currentRegex);
                System.err.println("");
                if (currentRegex.equals("0004")) {
                    try {
                        if (Integer.parseInt(currentString.split(REGEX)[1]) < 1) {
                            frame.remove(pane);
                            JPanel panel = new JPanel();
                            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                            JLabel label = new JLabel("             No players found currently :(");
                            JLabel label1 = new JLabel("                          Try Again Later");
                            panel.add(label);
                            panel.add(label1);
                            pane = new JScrollPane(panel);
                            pane.setBounds(200, 100, 250, 500);
                            frame.add(pane);
                            frame.repaint();
                            frame.revalidate();
                        }
                        else {
                            frame.remove(pane);
                            JPanel panel = new JPanel();
                            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                            String handlers = currentString.split(REGEX)[2];
                            String arrays[] = handlers.split(",");
                            for(int k = 0; k < arrays.length; k++) {
                                System.err.println("Player: "+arrays[k]);
                                JButton button = new JButton(arrays[k]);
                                button.setName(arrays[k]);
                                button.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        System.err.println("Sending request to "+button.getName());
                                        write("0005"+REGEX+button.getName());
                                    }
                                    
                                });
                                button.setSize(250,50);
                                panel.add(button);
                            }
                            pane = new JScrollPane(panel);
                            pane.setBounds(200, 100, 250, 500);
                            frame.add(pane);
                            frame.repaint();
                            frame.revalidate();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                }
                else if (currentRegex.equals("0007")) {
                    int choice = JOptionPane.showOptionDialog(frame, currentString.split(REGEX)[1]+" has invited you to join your game, would you like to accept?", "Game invite",JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,null,null,null);
                    System.err.println(choice);
                    
                    System.err.println("sending result now");
                    write("0008"+REGEX+choice+REGEX+currentString.split(REGEX)[1]);
                    
                }
                else if (currentRegex.equals("0009")) {
                    write("0011"+REGEX+currentString.split(REGEX)[1]);
                    System.err.println("match starting shortly...");
                }
                else if (currentRegex.equals("0010")) {
                    System.err.println(currentString.split(REGEX)[1]+" has declined you game invite. :(");
                }
                else if (currentRegex.equals("0012")) {
                    frame.remove(pane);
                    frame.remove(refreshButton);
                    frame.repaint();
                    frame.revalidate();
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
                                        if (!shipSelectionFinished) {
                                            shipSelectionFinished = true;
                                            String output = "";
                                            ArrayList<JButton[]> ships = new ArrayList<>();
                                            ships.add(ship5);
                                            ships.add(ship4);
                                            ships.add(ship3);
                                            ships.add(ship2);
                                            ships.add(ship1);
                                            for (int k = 0; k < ships.size(); k ++) {
                                                for(int j = 0; j < ships.get(k).length; j++) {
                                                    output += ships.get(k)[j].getName() + ",";
                                                }
                                                output += REGEX;
                                            }
                                            write("0013"+REGEX+output);
                                            
                                        }
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
                                             for (int i = 0; i <= currentSize; i++) {
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
                    frame.repaint();
                    frame.revalidate();
                }   
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public void write(String s) {
        try {
            writer.write(s+"\n");
            writer.flush();
        } catch (Exception e) {
        }
    }
    public void start() {
        String hostname = "192.168.0.104";
        int port = 1085;
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(frame.getMaximumSize());
        frame.setLayout(null);
        frame.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
               write("0000");
               try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            }

        });
        frame.setVisible(true);
        try  {
            socket = new Socket(hostname, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new Thread(this).start();
            


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new BattleShipClient().start();
    }

    
}
