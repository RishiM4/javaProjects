import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerHome implements HttpHandler {
    ArrayList<String> t = new ArrayList<>();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        Headers headers = exchange.getRequestHeaders();
        List<String> cookies = headers.get("Cookie");
        String username = "";
        if (cookies != null) {
            for (String cookieHeader : cookies) {
                String[] individualCookies = cookieHeader.split(";");
                for (String cookie : individualCookies) {
                    String[] parts = cookie.trim().split("=", 2);
                    if (parts.length == 2) {
                        String name = parts[0];
                        username = parts[1];
                    }
                }
            }
            System.err.println(username);
            if ("partial=true".equalsIgnoreCase(query)) {
                StringBuilder responseMessage = new StringBuilder();
                for (String message : t) {
                    responseMessage.append("<li>").append(message).append("</li>");
                    
                }
                
                byte[] bytes = responseMessage.toString().getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
                return;
            }

            if ("POST".equalsIgnoreCase(method)) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, String> params = parseFormData(body);
                String message = params.getOrDefault("message", "").trim();
                if (!message.isEmpty()) {
                    LocalTime currentTime = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

                    String formattedTime = currentTime.format(formatter);
                    t.add(formattedTime+" - "+username+": "+escapeHtml(message));
                    Path filePath = Paths.get("webserver.txt");
                try {
			        List<String> lines = Files.readAllLines(filePath);
                    lines.add(t.get(t.size()-1));
                    Files.write(filePath, lines);
		        } catch (IOException e) {
			        e.printStackTrace();
		        }
                }

                exchange.getResponseHeaders().add("Location", "/");
                exchange.sendResponseHeaders(303, -1);
                return;
            }


            String html = """
                <html>
                    <title>Chat</title>
                    <body>
                        <h2>Enter Some Text%s</h2>
                        <form method="POST" action="/">
                            <input type="text" name="message" required autocomplete="off">
                            <button type="submit">Send</button>
                        </form>
                        <ul id="messageList">

                        </ul>
                        <script>
                            async function loadMessages() {
                                const res = await fetch('/?partial=true');
                                const html = await res.text();
                                document.getElementById('messageList').innerHTML = html;
                            }

                            setInterval(loadMessages, 2500); 
                            window.onload = loadMessages;
                        </script>
                        <script>
                            function scrollToBottom() {
                                window.scrollTo(0, document.body.scrollHeight);
                            }
                            window.addEventListener('load', scrollToBottom);
                            const observer = new MutationObserver(scrollToBottom);
                            observer.observe(document.body, { childList: true, subtree: true });
                        </script>
                    </body>
                </html>
            """.formatted(System.currentTimeMillis());

            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        else {
            String html = """
            <html>
                <body>
                    <p>Please go here to create an account</p>
                    <a href=/account>account</a>
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
        return s.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
}
