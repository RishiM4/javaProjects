package battleship;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BattleShipClient implements Runnable{
    private static final String REGEX = "!:!";
    BufferedReader reader;
    BufferedWriter writer;
    JFrame frame = new JFrame("BattleShip");
    JButton refreshButton = new JButton("Refresh");
    Socket socket;
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
                    while (name == null||name.equals("")||name.contains(",")) {
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
                    System.err.println(" starting games now!!!!!!!");
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
        String hostname = "192.168.0.107";
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
