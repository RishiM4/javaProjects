package src;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerAccount implements HttpHandler {
    ArrayList<String> t = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String unique = "";
            String method = exchange.getRequestMethod();
            if ("POST".equalsIgnoreCase(method)) {

                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseFormData(body);
                String message = params.getOrDefault("message", "mkmk").trim();

                if (!message.isEmpty()) {
                    Path filePath = Paths.get("webServerUsers.txt");
                    List<String> users = Files.readAllLines(filePath);
                    Map<String,String> cookies = getCookies(exchange);
                    if (cookies.get("UUID")==null) {
                        UUID uuid = UUID.randomUUID();
                        exchange.getResponseHeaders().add("Set-Cookie", "UUID=%s; Path=/; HttpOnly".formatted(uuid.toString()));

                    }
                    if (cookies.get("userID")!=null) {
                        
                        users.set(users.indexOf(cookies.get("userID")),message);
                        Files.write(filePath, users);

                            exchange.getResponseHeaders().add("Set-Cookie", "userID=%s; Path=/; HttpOnly".formatted(escapeHtml(message)));
                            exchange.getResponseHeaders().add("Location", "/");
                            exchange.sendResponseHeaders(302, -1);
                            return;
                    }
                    else {
                        if (!users.contains(message)) {
                            users.add(message);
                            Files.write(filePath, users);

                            exchange.getResponseHeaders().add("Set-Cookie", "userID=%s; Path=/; HttpOnly".formatted(escapeHtml(message)));
                            exchange.getResponseHeaders().add("Location", "/");
                            exchange.sendResponseHeaders(302, -1);
                            return;
                        }
                        else {
                            unique = "Username taken";
                        }
                    }              
                    
                    
                }


            }


            String html = """
                <html>
                <body>
                    <h2>text</h2>
                    <form method="POST" action="/account">
                        <input type="text" name="message" required autocomplete="off">
                        <button type="submit">Send</button>
                    </form>       
                    <p>%sm</p>         
                </body>
                </html>
                """.formatted(unique);

            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    private String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }
    public static Map<String, String> getCookies(HttpExchange exchange) {
        Map<String, String> cookies = new HashMap<>();
        List<String> cookieHeaders = exchange.getRequestHeaders().get("Cookie");
        if (cookieHeaders != null) {
            for (String header : cookieHeaders) {
                String[] cookiePairs = header.split(";\\s*");
                for (String cookie : cookiePairs) {
                    String[] parts = cookie.split("=", 2);
                    if (parts.length == 2) {
                        cookies.put(parts[0], parts[1]);
                    }
                }
            }
        }
        return cookies;
    }
}