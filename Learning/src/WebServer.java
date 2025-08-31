import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;

public class WebServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new WebServerHome());
        server.createContext("/account", new WebServerAccount());

        server.setExecutor(null);
        server.start();
        System.out.println("Server running on http://localhost:8000/");
    }

   
}
