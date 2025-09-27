import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.PriorityQueue;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerTest implements HttpHandler{
    public static PriorityQueue<String> commands = new PriorityQueue<String>();
    public WebServerTest() {
        JFrame frame = new JFrame("Control Panel");
        frame.setBounds(0,0,300,300);
        frame.setLayout(null);
        frame.setVisible(true);
        JButton screenshot = new JButton("Screen Shot");
        screenshot.setBounds(10,10,150,50);
        frame.add(screenshot);

        screenshot.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                commands.add("screenShot");
            }
            
        });
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        InputStream input = exchange.getRequestBody();
            String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Received key submission:\n" + body);
        
        String response = "";
        if (commands.size()!=0) {
            if (commands.peek().equals("screenShot")) {
                response = "screenShot";
                commands.remove();
            }
        }
        exchange.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
