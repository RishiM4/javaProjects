import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerTest implements HttpHandler{
    ArrayList<String> t = new ArrayList<>();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        
        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        Map<String,String> cookies = getCookies(exchange);
        if (cookies.get("userID")!=null&&cookies.get("UUID")!=null) {
            if ("true".equals(parseQuery(query).get("partial"))) {
                String type = parseQuery(query).get("type");
                if (type.equals("chatList")) {
                    String sidebarHtml =  """
                        <div class="chat-item">Global</div>
                    """
                    .formatted(System.currentTimeMillis());
                    byte[] bytes = sidebarHtml.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.getResponseBody().close();
                    return;
                }
                else if (type.equals("chatMessages")) {
                    String sidebarHtml =  """
                    <div><strong>Rishi:</strong> Hello!</div>
                    <div><strong>ME:</strong> Me, how are %syou?</div>
                    
                    """.formatted(System.currentTimeMillis());
                    for(String s : t) {
                        sidebarHtml += "<div><strong>Rishi:</strong>%s</div>".formatted(s);
                    }
                    byte[] bytes = sidebarHtml.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, bytes.length);
                    exchange.getResponseBody().write(bytes);
                    exchange.getResponseBody().close();
                    return;
                }
            
            }

            if ("POST".equalsIgnoreCase(method)) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                

                String message = parseFormData(body).getOrDefault("message", "").trim();

                System.out.println("User submitted message: " + message);
                t.add(message);
                exchange.getResponseHeaders().set("Location", "/");
                exchange.sendResponseHeaders(302, -1);
                return;
            }
            String html = """
                <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <title>Chat UI</title>
                        <style>
                            body {
                            margin: 0;
                            font-family: Arial, sans-serif;
                            height: 100vh;
                            display: flex;
                            }

                            .sidebar {
                            width: 250px;
                            border-right: 1px solid #ccc;
                            overflow-y: auto;
                            background-color: #f7f7f7;
                            }

                            .chat-item {
                            padding: 15px;
                            border-bottom: 1px solid #e0e0e0;
                            cursor: pointer;
                            }

                            .chat-item:hover {
                            background-color: #eaeaea;
                            }

                            .chat-window {
                            flex: 1;
                            display: flex;
                            flex-direction: column;
                            }

                            .chat-header {
                            padding: 15px;
                            border-bottom: 1px solid #ccc;
                            background-color: #f0f0f0;
                            font-weight: bold;
                            }

                            .chat-messages {
                            flex: 1;
                            padding: 15px;
                            overflow-y: auto;
                            background-color: #fff;
                            }

                            .chat-input {
                            display: flex;
                            padding: 10px;
                            border-top: 1px solid #ccc;
                            background-color: #f9f9f9;
                            }

                            .chat-input input[type="text"] {
                            flex: 1;
                            padding: 10px;
                            border: 1px solid #ccc;
                            border-radius: 4px;
                            margin-right: 10px;
                            }

                            .chat-input button {
                            padding: 10px 15px;
                            border: none;
                            background-color: #4caf50;
                            color: white;
                            border-radius: 4px;
                            cursor: pointer;
                            }

                            .chat-input button:hover {
                            background-color: #45a049;
                            }
                            </style>
                        </head>
                        <body>

                            <!-- Sidebar (Chat List) -->
                            <div class="sidebar" id="chatList">
                                <div class="chat-item">Alice</div>
                                <div class="chat-item">Bob</div>
                                <div class="chat-item">Charlie</div>
                                <!-- Add more chat items here -->
                            </div>

                            <!-- Chat Window -->
                            <div class="chat-window">
                                <div class="chat-header">Alice</div>

                                <div class="chat-messages" id="chatMessages">
                                <!-- Chat messages go here -->
                                <div><strong>Alice:</strong> Hello!</div>
                                <div><strong>You:</strong> Hi, how are you?</div>
                                </div>

                                <div class="chat-input">
                                <form method="POST" action="/" style="display: flex; width: 100%;">
                                    <input type="text" name="message" placeholder="Type a message..." autocomplete="off" required>
                                    <button type="submit">Send</button>
                                </form>
                                </div>
                            </div>
                            <script>
                            async function updateSidebar() {
                                const res = await fetch('/?partial=true&type=chatList');
                                const html = await res.text();
                                document.getElementById('chatList').innerHTML = html;
                            }
                            async function updateMessages() {
                                const res = await fetch('/?partial=true&type=chatMessages');
                                const html = await res.text();
                                document.getElementById('chatMessages').innerHTML = html;
                            }

                            window.onload = () => {
                                updateSidebar();
                                updateMessages();
                            };

                            setInterval(updateSidebar, 1000);
                            setInterval(updateMessages, 1000);

                            </script>
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
    private Map<String, String> parseQuery(String query) throws UnsupportedEncodingException {
    Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;

        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                map.put(key, value);
            }
        }

        return map;
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
//ok so we have the file called web server chats, in there whenvever a chat is created, it should be put into the folder, and the chat
//will be represented as its own file, in the file there will be alist of the users in the file, and there will be a chat log.
//tbd how to fetch the chats from the file when the user wants to switch chats.