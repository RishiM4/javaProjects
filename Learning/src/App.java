import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws IOException {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Serve the HTML page
        server.createContext("/", exchange -> {
            String html = """
                    <html>
                    <body>
                        <h1>Video Demo</h1>
                        <video width="640" controls>
                            <source src="/video.mp4" type="video/mp4">
                            Your browser does not support the video tag.
                        </video>
                    </body>
                    </html>
                    """;
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, html.getBytes().length);
            exchange.getResponseBody().write(html.getBytes());
            exchange.close();
        });

        // Serve the video
        server.createContext("/video.mp4", exchange -> {
            File videoFile = new File("public/video.mp4");

            if (!videoFile.exists()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            byte[] videoBytes = Files.readAllBytes(videoFile.toPath());

            exchange.getResponseHeaders().set("Content-Type", "video/mp4");
            exchange.sendResponseHeaders(200, videoBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(videoBytes);
            os.close();
        });

        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:" + port);
    }
}
