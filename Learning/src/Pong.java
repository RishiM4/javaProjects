import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Pong {

    static Double x = 185.0; 
    static Double y = 175.0; 
    static Double velocityX = 0.0;
    static Double velocityY = 0.0; 
    static int paddle1Y = 160;
    static int paddle2Y = 160;
    static int player1Score = 0;
    static int player2Score = 0;
    static Set<Integer> pressedKeys = new HashSet<>();
    static Random random = new Random();
    static String state = "pause";
    
    static int collisions = 1;
    static JFrame frame = new JFrame("Pong");
    private static void assignVelocity(){
        int velocity = 50;
        collisions = 0;
        while(velocity>25&&velocity<75){
            velocity = random.nextInt(100);
            
        }
        if (velocity<25) {
            velocityX = (velocity*frame.getWidth()/-50)/100.0;
        }
        else if (velocity>75) {
            velocityX = (velocity*frame.getWidth()/200)/100.0;
        }
        velocity = 50;
        while(velocity>25&&velocity<75){
            velocity = random.nextInt(100);
            
        }
        if (velocity<25) {
            velocityY = (velocity * frame.getHeight()/-75)/100.0;
        }
        else if (velocity>75) {
            velocityY = (velocity*frame.getHeight()/225)/100.0;
        }
        System.out.println(velocityX);
        System.out.println(velocityY);
        if (velocityX<frame.getWidth()/266&&velocityX>frame.getWidth()/-266) {
           assignVelocity();
        }
        else if (velocityY<frame.getHeight()/600&&velocityY>frame.getHeight()/-600) {
            assignVelocity();
        }
        
        state = "game";
    }
    public static void main(String[] args) {
        frame.setSize(399, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                this.setBackground(Color.WHITE);
                
                g.setColor(Color.ORANGE);
                g.fillOval((int)Math.round(x), (int)Math.round(y), 10, 10);
                Ellipse2D.Double ball = new Ellipse2D.Double(x,y,10,10);

                g.setColor(Color.BLUE);
                g.fillRect(25, paddle1Y, 10, 40);
                
            
                //g.fillRect(350, paddle2Y-1, 10, 1);
                //g.fillRect(350, paddle2Y+40, 10, 1);
                //g.fillRect(349, paddle2Y, 1, 40);
                //g.fillRect(frame.getWidth()-40,paddle2Y,1,40);


                g.drawString(player1Score+" : "+player2Score, frame.getWidth()/2-25, 20);

                g.setColor(Color.RED);
                g.fillRect(frame.getWidth()-50, paddle2Y, 10, 40);
                

                //g.fillRect(25, paddle1Y-1, 10, 1);
                //g.fillRect(25, paddle1Y+40, 10, 1);
                //g.fillRect(35, paddle1Y, 1, 40);
                //g.fillRect(24, paddle1Y, 1, HEIGHT);

                Rectangle player1Up = new Rectangle(25,paddle1Y-2,10,2);
                Rectangle player1Down = new Rectangle(25,paddle1Y+40,10,2);
                Rectangle player1Left = new Rectangle(23, paddle1Y, 2, 40);
                Rectangle player1Right = new Rectangle(35,paddle1Y,2,40);

                Rectangle player2Up = new Rectangle(frame.getWidth()-50,paddle2Y-2,10,2);
                Rectangle player2Down = new Rectangle(frame.getWidth()-50, paddle2Y+40, 10, 2);
                Rectangle player2Left = new Rectangle(frame.getWidth()-52, paddle2Y, 2, 40);
                Rectangle player2Right = new Rectangle(frame.getWidth()-40, paddle2Y, 2, 40);
                
                if (ball.intersects(player1Up)||ball.intersects(player1Down)||ball.intersects(player2Up)||ball.intersects(player2Down)) {
                    velocityY = velocityY*-1;
                    collisions++;
                    velocityY = velocityY*1.05;
                    
                }
                if (ball.intersects(player1Left)||ball.intersects(player1Right)||ball.intersects(player2Left)||ball.intersects(player2Right)) {
                    velocityX = velocityX *-1;
                    collisions++;
                    velocityX  = velocityX* 1.05;
                    
                }



            }
        };

        frame.add(panel);
        
        frame.setSize(400, 400);
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
            
        }); 
            
        Timer timer = new Timer(10, e -> {
            if (pressedKeys.contains(KeyEvent.VK_UP)) {
                if(paddle2Y>0){
                    paddle2Y -= (frame.getHeight()/200)*(1+collisions/25);
                }
            }
            if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
                if(paddle2Y<frame.getHeight()-75){
                    paddle2Y += (frame.getHeight()/200)*(1+collisions/25);
                }
            }
            if (pressedKeys.contains(KeyEvent.VK_W)) {
                if(paddle1Y>0){
                    paddle1Y -= (frame.getHeight()/200)*(1+collisions/150);
                }
            }
            if (pressedKeys.contains(KeyEvent.VK_S)) {
                if(paddle1Y<frame.getHeight()-75){
                    paddle1Y += (frame.getHeight()/200)*(1+collisions/150);
                }
            }
            if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
                if (state.equals("pause")) {
                    assignVelocity();
                }
            }
            if (pressedKeys.contains(KeyEvent.VK_MINUS)){
                paddle1Y = (int)Math.round(y);
            }
            if (pressedKeys.contains(KeyEvent.VK_EQUALS)){
                paddle2Y = (int)Math.round(y);
            }
            
        });
        timer.start();
        while (true) { 
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
            panel.repaint();
            if (x>frame.getWidth()-25){
                x=(frame.getWidth()/2)-15.0;
                y=(frame.getHeight()/2)-25.0;
                velocityX = 0.0;
                velocityY = 0.0;
                state = "pause";
                player1Score++;
            }
            else if (x<0) {
                x=(frame.getWidth()/2)-15.0;
                y=(frame.getHeight()/2)-25.0;
                velocityX = 0.0;
                velocityY = 0.0;
                state = "pause";
                player2Score++;
            }
            if (y>frame.getHeight()-48||y<0){
                velocityY = velocityY*-1;
            }
            
            
            x+=velocityX;
            y+=velocityY;
        }
    }
}
