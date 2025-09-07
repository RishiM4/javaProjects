import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerCreateChat implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

                // Read the request body
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder formData = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    formData.append(line);
                }

                
                Map<String, String> parameters = parseFormData(formData.toString());

                List<String> users = parseFormData(formData.toString(), "users");
                String chatName = escapeHtml(parameters.get("chatName"));
                String uuid = UUID.randomUUID().toString();
                File files = new File("WebServerChats/%s".formatted(uuid));
                files.mkdir();
                
                FileWriter writer = new FileWriter("WebServerChats/%s/chatName".formatted(uuid));
                writer.write(chatName);
                writer.flush();
                writer.close();

                writer = new FileWriter("WebServerChats/%s/currentUsers".formatted(uuid));
                writer.write(getCookies(exchange).get("UUID"));
                writer.flush();
                writer.close();

                writer = new FileWriter("WebServerChats/%s/pendingUsers".formatted(uuid));
            
                for(int k = 0; k < users.size(); k++) {
                    writer.write(users.get(k)+"\n");
                    
                    System.err.println(users.get(k));
                }
                writer.flush();
                writer.close();

                writer = new FileWriter("WebServerChats/%s/messages".formatted(uuid));
                writer.write("");
                writer.flush();
                writer.close();

                writer = new FileWriter("WebServerChats/%s/origin".formatted(uuid));
                writer.write(getCookies(exchange).get("userID"));
                writer.flush();
                writer.close();

                exchange.getResponseHeaders().add("Location", "/account");
                exchange.sendResponseHeaders(303, -1);
                return;
            }
            String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Chat Server | By Rishi & Advik</title>
                    <style>
                        body {
                        font-family: sans-serif;
                        margin: 40px;
                        background-color: #f8f8f8;
                        }

                        form {
                        background-color: #fff;
                        padding: 20px;
                        border-radius: 8px;
                        max-width: 400px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                        }

                        h3 {
                        margin-top: 0;
                        color: #333;
                        }

                        label {
                        display: block;
                        margin: 10px 0;
                        }

                        input[type="text"] {
                        width: 100%%;
                        padding: 8px;
                        margin-top: 5px;
                        box-sizing: border-box;
                        border: 1px solid #ccc;
                        border-radius: 4px;
                        }

                        button {
                        margin-top: 15px;
                        padding: 10px 15px;
                        background-color: #4CAF50;
                        color: white;
                        border: none;
                        border-radius: 4px;
                        cursor: pointer;
                        }

                        button:hover {
                        background-color: #45a049;
                        }
                    </style>
                </head>
                <body>

                <form method="POST" action="/create-chat">
                    <h3>Select People to Add to Chat</h3>

                    
                    %s
                    <label>
                    Chat Name:
                    <input type="text" name="chatName" required>
                    </label>

                    <button type="submit">Create Chat</button>
                </form>

                </body>
            </html>
            """;
            String otherUsers = "";
            List<String> userList = Files.readAllLines(Paths.get("webServerUsers.txt"));
            for(String s : userList) {
                if (!s.equals(getCookies(exchange).get("userID"))) {
                    otherUsers += """
                        <label><input type="checkbox" name="users" value="%s"> %s</label>
                            
                            """.formatted(s,s);
                }
            }
            html = html.formatted(otherUsers);
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
    private String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
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
    public static List<String> parseFormData(String formData, String key) throws UnsupportedEncodingException {
        List<String> values = new ArrayList<>();

        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=", 2);
            if (parts.length == 2) {
                String decodedKey = URLDecoder.decode(parts[0], StandardCharsets.UTF_8.name());
                String decodedValue = URLDecoder.decode(parts[1], StandardCharsets.UTF_8.name());

                if (decodedKey.equals(key)) {
                    values.add(decodedValue);
                }
            }
        }

        return values;
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
