package src;
import com.sun.net.httpserver.*;
import java.net.*;

public class WebServer {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8000), 0);
            server.createContext("/account", new WebServerAccount());
            server.createContext("/", new WebServerHome());
            server.createContext("/create", new WebServerCreateChat());
            server.createContext("/invites", new WebServerPendingInvites());
            server.createContext("/test", new WebServerTest());
            server.setExecutor(null);
            server.start();
            System.out.println("Server running on http://localhost:8000/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

   
}
