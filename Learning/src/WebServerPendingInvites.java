package src;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerPendingInvites implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if (method.equalsIgnoreCase("POST")) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                String response = parseFormData(body).get("response");
                String uuid = parseFormData(body).get("chatID");
                if (response.equals("decline")) {
                    List<String> pendingInvites = Files.readAllLines(Paths.get("WebServerChats/%s/pendingUsers".formatted(uuid)));
                    if (pendingInvites.contains(getCookies(exchange).get("userID"))) {
                        pendingInvites.remove(getCookies(exchange).get("userID"));
                        Files.write(Paths.get("WebServerChats/%s/pendingUsers".formatted(uuid)),pendingInvites);
                    }
                }
                else if (response.equals("accept")) {
                    List<String> pendingInvites = Files.readAllLines(Paths.get("WebServerChats/%s/pendingUsers".formatted(uuid)));
                    if (pendingInvites.contains(getCookies(exchange).get("userID"))) {
                        pendingInvites.remove(getCookies(exchange).get("userID"));
                        Files.write(Paths.get("WebServerChats/%s/pendingUsers".formatted(uuid)),pendingInvites);
                    }
                    List<String> lines = Files.readAllLines(Paths.get("WebServerChats/%s/currentUsers".formatted(uuid)));
                    lines.add(getCookies(exchange).get("UUID"));
                    Files.write(Paths.get("WebServerChats/%s/currentUsers".formatted(uuid)),lines);
                }
            }
            String html = """
                <!DOCTYPE html>
<html>
<head>
  <title>Chat Join Requests</title>
  <style>
    body {
      font-family: sans-serif;
      margin: 20px;
    }

    h1 {
      color: #333;
    }

    ul {
      list-style-type: none;
      padding: 0;
    }

    li {
      background-color: #f9f9f9;
      margin-bottom: 10px;
      padding: 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
    }

    button {
      margin-left: 10px;
      padding: 5px 10px;
      border: none;
      border-radius: 3px;
      cursor: pointer;
    }

    button:hover {
      background-color: #eee;
    }

    em {
      color: green;
      font-style: normal;
      font-weight: bold;
    }
  </style>
</head>
<body>

  <h1>Chat Join Requests</h1>

  <ul id="requests">
    %s
  </ul>

  
</body>
</html>
                """;
            File directory = new File("WebServerChats");
            String files[] = directory.list();
            String invites = "";
            for(int k = 0; k < files.length; k++) {
                List<String> t = Files.readAllLines(Paths.get("WebServerChats/%s/pendingUsers".formatted(files[k])));
                if (t.contains(getCookies(exchange).get("userID"))) {
                    String item = """
    <li>
        From: <strong>%s</strong> Group: <strong>%s</strong>
        <form method="POST" action="/invites" style="display:inline;">
            <input type="hidden" name="chatID" value="%s">
            <input type="hidden" name="response" value="accept">
            <button type="submit">Accept</button>
        </form>
        <form method="POST" action="/invites" style="display:inline;">
            <input type="hidden" name="chatID" value="%s">
            <input type="hidden" name="response" value="decline">
            <button type="submit">Decline</button>
        </form>
    </li>
    """;

                    String origin = Files.readAllLines(Paths.get("WebServerChats/%s/origin".formatted(files[k]))).get(0);
                    String chatName = Files.readAllLines(Paths.get("WebServerChats/%s/chatName".formatted(files[k]))).get(0);
                    String uuid = files[k];
                    invites += item.formatted(origin,chatName,uuid,uuid);
                }
            }
            if (invites.equals("")) {
                html = html.formatted("<li>No invites found :(");
            }
            else {
                html = html.formatted(invites);
            }
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
