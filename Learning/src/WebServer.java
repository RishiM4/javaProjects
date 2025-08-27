import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class WebServer {
    static String t;
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/", exchange -> {
            if (exchange.getRequestURI().getPath().equals("/")) {
                String response = """
                <html>
                <head><title>Basic</title></head>
                <body>
                    <h1>Enter Something</h1>
                    <a href="/home">Visit Example</a>
                    <ul>
                        !!!
                    </ul>
                    
                </body>
                </html>
                """;
            

                sendResponse(exchange, response.replaceAll("!!!", "<li>ddi</li><li>bydasdade</li>"));
            }
            else {
                showPageNotFound(exchange);
            }
            
        });
        server.createContext("/home", exchange -> {
            if (exchange.getRequestURI().getPath().equals("/home")) {
                String response = """
                <html>
                <head><title>Home</title></head>
                <body>
                    <h1>Enter Something</h1>
                    <a href="/home">Visit Example</a>
                    <ul>
                        !!!
                    </ul>
                    
                </body>
                </html>
                """;
            

                sendResponse(exchange, response.replaceAll("!!!", "<li>ddi</li><li>bydasdade</li>"));
            }
            else {
                showPageNotFound(exchange);
            }
            
        });
        
        server.setExecutor(null); // Default thread pool
        server.start();
    } 
    private static void showPageNotFound(HttpExchange exchange) {
        String response = """
                <html>
                <head><title>Page Not Found</title></head>
                <body>
                    <h1>Error 404</h1>
                    <a>Page Not Found</a>
                    
                </body>
                </html>
                """;
            

        sendResponse(exchange, response);
    }
    
    private static void sendResponse(HttpExchange exchange, String response){
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
