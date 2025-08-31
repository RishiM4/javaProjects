import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerAccount implements HttpHandler {
    ArrayList<String> t = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        
        String method = exchange.getRequestMethod();
        if ("POST".equalsIgnoreCase(method)) {

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = parseFormData(body);
            String message = params.getOrDefault("message", "mkmk").trim();
            if (!message.isEmpty()) {
                exchange.getResponseHeaders().add("Set-Cookie", "userID=%s; Path=/; HttpOnly".formatted(message));
                exchange.getResponseHeaders().add("Location", "/");
                exchange.sendResponseHeaders(302, -1);
            }


        }


        String html = """
            <html>
            <body>
                <h2>text</h2>
                <form method="POST" action="/account">
                    <input type="text" name="message" required>
                    <button type="submit">Send</button>
                </form>                
            </body>
            </html>
            """;

        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        for (String pair : formData.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                String key = URLDecoder.decode(parts[0], "UTF-8");
                String value = URLDecoder.decode(parts[1], "UTF-8");
                map.put(key, value);
            }
        }
        return map;
    }
}
