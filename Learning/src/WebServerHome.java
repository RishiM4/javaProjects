package src;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerHome implements HttpHandler{
    ArrayList<String> t = new ArrayList<>();
    HashMap<String,String> currentChats = new HashMap<>();
    @Override
    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            Map<String,String> cookies = getCookies(exchange);
            if (cookies.get("userID")!=null&&cookies.get("UUID")!=null) {
                if (!currentChats.containsKey(getCookies(exchange).get("UUID"))) {
                    currentChats.put(getCookies(exchange).get("UUID"), "Global");
                }
                if ("true".equals(parseQuery(query).get("partial"))) {
                    String type = parseQuery(query).get("type");
                    if (type.equals("chatList")) {
                        String sidebarHtml =  """
                            <div class="chat-item chat-header-item">Chats</div>
                            <div class="chat-item">
                                <form method="POST" action="/" style="margin: 0; padding: 0;">
                                    <button type="submit name="chatName" value="Global" class="chat-item" 
                                        style="all: unset;display: block;width: 100%;text-align: left;font: inherit;">Global</button>
                                        <input type="hidden" name="UUID" value="Global">
                                        <input type="hidden" name="inputType" value="switchChat">
                                </form>
                            </div>

                        """;
                        File directory = new File("WebServerChats");
                        String files[] = directory.list();
                        for(int k=0; k < files.length; k ++) {
                            String uuid = getCookies(exchange).get("UUID");
                            Path path = Paths.get("WebServerChats/%s/currentUsers".formatted(files[k]));
                            List<String> t = Files.readAllLines(path);
                            if (t.contains(uuid)) {
                                String chatName = Files.readAllLines(Paths.get("WebServerChats/%s/chatName".formatted(files[k]))).get(0);
                                String item =  """
                                <div class="chat-item">
                                    <form method="POST" action="/" style="margin: 0; padding: 0;">
                                        <button type="submit" class="chat-item" 
                                            style="all: unset;display: block;width: 100%%;text-align: left;font: inherit;">%s</button>
                                            <input type="hidden" name="UUID" value="%s">
                                            <input type="hidden" name="inputType" value="switchChat">
                                    </form>
                                </div>
                                """.formatted(chatName,files[k]);
                                sidebarHtml+=item;
                            }
                        }
                        byte[] bytes = sidebarHtml.getBytes(StandardCharsets.UTF_8);
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        exchange.sendResponseHeaders(200, bytes.length);
                        exchange.getResponseBody().write(bytes);
                        exchange.getResponseBody().close();
                        return;
                    }
                    else if (type.equals("chatMessages")) {
                        String messageList = "";
                        Path path = Paths.get("WebServerChats/%s/messages".formatted(currentChats.get(getCookies(exchange).get("UUID"))));
                        List<String> messages = Files.readAllLines(path);
                        for(String s : messages) {
                            messageList+=s;
                        }
                        /*messageList+="""
                                <div class="my-message"><Strong>You: </Strong>aksdjksad</div>
                                """;
                        */
                        byte[] bytes = messageList.getBytes(StandardCharsets.UTF_8);
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
                    String inputType = parseFormData(body).get("inputType");
                    System.err.println(inputType);
                    if (inputType == null || inputType.trim().isEmpty()) {
                        
                        System.out.println("Received key submission:\n" + body);
                        List<String> lines = Files.readAllLines(Paths.get("webServerPublicKeys.txt"));
                        for(int k = 0; k < lines.size(); k++) {
                            if (!lines.get(k).split(":::")[0].equals(getCookies(exchange).get("UUID"))) {
                                lines.add(getCookies(exchange).get("UUID")+":::"+body);
                            }
                        }
                        Files.write(Paths.get("webServerPublicKeys.txt"), lines);

                        
                    }
                    else if (inputType.equals("newMessage")) {
                        String message = escapeHtml(parseFormData(body).getOrDefault("message", "").trim());
                        
                        if (!message.equals("")) {
                            System.out.println("User submitted message: " + message);
                            
                            String command = message.split(" ")[0];
                            if (command.equals("/ask")) {
                                StringBuffer temp = new StringBuffer(message);
                                temp.delete(0, 5);
                                
                                Thread t = new Thread(()->{
                                    try {
                                        String response = getResponse(temp.toString().trim());
                                        Path path = Paths.get("WebServerChats/%s/messages".formatted(currentChats.get(getCookies(exchange).get("UUID"))));
                                        List<String> lines = Files.readAllLines(path);
                                        if (lines.contains("<div><strong>AI: </strong>Generating Response...</div>")) {
                                            int index = lines.indexOf("<div><strong>AI: </strong>Generating Response...</div>");
                                            lines.set(index, "<div><strong>AI: </strong>%s</div>".formatted(response.replace("\\n", "<br>")));
                                        }
                                        else {
                                            lines.add("<div><strong>AI: </strong>%s</div>".formatted(response.replace("\\n", "<br>")));
                                        }
                                        Files.write(path, lines);
                                    } catch (Exception e) {
                                    }
                                    
                                });
                                t.start();
                                Path path = Paths.get("WebServerChats/%s/messages".formatted(currentChats.get(getCookies(exchange).get("UUID"))));
                                List<String> lines = Files.readAllLines(path);
                                lines.add("<div><strong>%s → AI: </strong>%s</div>".formatted(getCookies(exchange).get("userID"),temp.toString().trim()));
                                lines.add("<div><strong>AI: </strong>Generating Response...</div>");

                                Files.write(path, lines);
                                
                            }
                            else if (command.equals("/server")) {
                                Path path = Paths.get("WebServerChats/%s/messages".formatted(currentChats.get(getCookies(exchange).get("UUID"))));
                                List<String> lines = Files.readAllLines(path);
                                lines.add("<div><strong>Server: </strong>%s</div>".formatted(message.split(" ")[1]));
                                Files.write(path, lines);
                            }
                            else {
                                Path path = Paths.get("WebServerChats/%s/messages".formatted(currentChats.get(getCookies(exchange).get("UUID"))));
                                List<String> lines = Files.readAllLines(path);
                                lines.add("<div><strong>%s:</strong>%s</div>".formatted(getCookies(exchange).get("userID"),message));
                                Files.write(path, lines);
                            }
                            
                            
                            
                        }
                    }
                    else if (inputType.equals("switchChat")) {
                        currentChats.replace(getCookies(exchange).get("UUID"), parseFormData(body).get("UUID"));
                    }
                    
                    exchange.getResponseHeaders().set("Location", "/");
                    exchange.sendResponseHeaders(302, -1);
                    return;
                }
                String html = """
                    <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

                            <title>Chat Server | By Rishi & Advik</title>
                            <style>
                                html, body {
                                    width: 100%%;
                                    height: 100%%;
                                    margin: 0;
                                    padding: 0;
                                    overflow: hidden;
                                }

                                    body {
                                        display: flex;
                                        font-family: Arial, sans-serif;
                                        height: 100vh;
                                        overflow: hidden;
                                    }

                                    .sidebar {
                                        width: 200px;
                                        border-right: 1px solid #ccc;
                                        overflow-y: auto;
                                        overflow-x: hidden;
                                        background-color: #f7f7f7;
                                        box-sizing: border-box;
                                        flex-shrink: 0;
                                    }

                                    .chat-item {
                                        border-bottom: 1px solid #e0e0e0ff;
                                        cursor: pointer;
                                    }

                                    .chat-item button {
                                        all: unset;
                                        display: block;
                                        width: 100%%;
                                        box-sizing: border-box;
                                        text-align: left;
                                        font: inherit;
                                        cursor: pointer;
                                        padding: 15px;
                                        white-space: nowrap;
                                        overflow: hidden;
                                        text-overflow: ellipsis;
                                    }

                                    .chat-header-item {
                                        font-weight: bold;
                                        border-bottom: 1px solid #ccc;
                                        background-color: #f0f0f0;
                                        padding: 15px;
                                    }

                                    .chat-window {
                                    flex: 1;
                                    display: flex;
                                    flex-direction: column;
                                    height: 100%%;
                                    min-width: 0; 
                                    overflow: hidden;
                                    position: relative;
                                }

                                    .chat-header {
                                        padding: 15px;
                                        border-bottom: 1px solid #ccc;
                                        background-color: #f0f0f0;
                                        font-weight: bold;
                                        flex-shrink: 0;
                                    }

                                    .chat-messages {
                                        flex: 1;
                                        overflow-y: auto;
                                        padding: 15px;
                                        background-color: #fff;
                                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen,
                                                    Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
                                    }

                                    .my-message {
                                        background-color: #76767611;
                                        align-self: flex-end;
                                        margin-left: auto;
                                        text-align: right;
                                    }

                                    .chat-input {
                                        display: flex;
                                        padding: 10px;
                                        border-top: 1px solid #ccc;
                                        background-color: #f9f9f9;
                                        flex-shrink: 0;
                                    }

                                    .chat-input input[type="text"] {
                                        flex: 1;
                                        padding: 10px;
                                        border: 1px solid #ccc;
                                        border-radius: 4px;
                                        margin-right: 10px;
                                        font: inherit;
                                    }

                                    .chat-input button {
                                        padding: 10px 15px;
                                        border: none;
                                        background-color: #4caf50;
                                        color: white;
                                        border-radius: 4px;
                                        cursor: pointer;
                                        font: inherit;
                                    }

                                    .chat-input button:hover {
                                        background-color: #45a049;
                                    }
                                </style>
                            </head>
                            <body>
                                <div class="sidebar" id="chatList">
                                    %s
                                </div>
                                <div class="chat-window">
                                    <div class="chat-header">%s</div>
                                        <div class="chat-messages" id="chatMessages">
                                            <div><strong>Alice:</strong> Hello!</div>
                                            <div><strong>You:</strong> Hi, how are you?</div>
                                        </div>

                                        <div class="chat-input">
                                            <form method="POST" action="/" style="display: flex; width: 100%%;">
                                                <input type="text" name="message" placeholder="Type a message..." autocomplete="off" required autofocus>
                                                <button type="submit">Send</button>
                                                <input type="hidden" name="inputType" value="newMessage">
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <script>
                                    let lastMessageHtml = ""; 

                                    async function updateMessages() {
                                        const res = await fetch('/?partial=true&type=chatMessages');
                                        const html = await res.text();
                                        const chatMessages = document.getElementById('chatMessages');

                                        if (html !== lastMessageHtml) {
                                            chatMessages.innerHTML = html;
                                            chatMessages.scrollTop = chatMessages.scrollHeight; 
                                            lastMessageHtml = html; 
                                        }
                                    }


                                    window.onload = () => {
                                        updateMessages();
                                    };
                                    setInterval(updateMessages, 1000);

                                </script>
                                <script>
                                    function resizeChatWindow() {
                                        const vh = window.innerHeight;
                                        const chatWindow = document.querySelector('.chat-window');
                                        if (chatWindow) {
                                            chatWindow.style.height = vh + 'px';
                                        }
                                    }

                                    window.addEventListener('resize', resizeChatWindow);
                                    window.addEventListener('orientationchange', resizeChatWindow);
                                    window.addEventListener('load', resizeChatWindow);
                                </script>
                                <script>
                                    const dbName = "cryptoKeyDB";
                                    const storeName = "keys";

                                    async function openDB() {
                                        return new Promise((resolve, reject) => {
                                            const request = indexedDB.open(dbName, 1);
                                            request.onerror = () => reject("Failed to open IndexedDB");
                                            request.onsuccess = () => resolve(request.result);
                                            request.onupgradeneeded = e => {
                                                const db = e.target.result;
                                                if (!db.objectStoreNames.contains(storeName)) {
                                                    db.createObjectStore(storeName);
                                                }
                                            };
                                        });
                                    }

                                    async function storeKey(db, keyName, key) {
                                        return new Promise((resolve, reject) => {
                                            const tx = db.transaction(storeName, "readwrite");
                                            const store = tx.objectStore(storeName);
                                            const request = store.put(key, keyName);
                                            request.onsuccess = () => resolve();
                                            request.onerror = () => reject("Failed to store key " + keyName);
                                        });
                                    }

                                    async function getKey(db, keyName) {
                                        return new Promise((resolve, reject) => {
                                            const tx = db.transaction(storeName, "readonly");
                                            const store = tx.objectStore(storeName);
                                            const request = store.get(keyName);
                                            request.onsuccess = () => resolve(request.result);
                                            request.onerror = () => reject("Failed to get key " + keyName);
                                        });
                                    }

                                    async function generateAESKey() {
                                        const key = await crypto.subtle.generateKey(
                                            {
                                            name: "AES-GCM",
                                            length: 128,
                                            },
                                            true,
                                            ["encrypt", "decrypt"]
                                        );
                                        return key;
                                    }
async function clearKeys() {
    const db = await openDB();
    const tx = db.transaction(storeName, "readwrite");
    const store = tx.objectStore(storeName);
    await Promise.all([
        store.delete("rsaPrivateKey"),
        store.delete("rsaPublicKey")
    ]);
    await tx.complete;
}
                                    async function exportAESKey(key) {
                                        const raw = await crypto.subtle.exportKey("raw", key);
                                        return btoa(String.fromCharCode(...new Uint8Array(raw)));
                                    }

                                    async function generateRSAKeyPair() {
                                        const keyPair = await crypto.subtle.generateKey(
                                            {
                                            name: "RSA-OAEP",
                                            modulusLength: 1024,
                                            publicExponent: new Uint8Array([1, 0, 1]),
                                            hash: "SHA-256",
                                            },
                                            true, // PRIVATE key is NOT extractable for security
                                            ["encrypt", "decrypt"]
                                        );
                                        return keyPair;
                                    }

                                    async function exportRSAPublicKey(key) {
                                        const spki = await crypto.subtle.exportKey("spki", key);
                                        return btoa(String.fromCharCode(...new Uint8Array(spki)));
                                    }
                                    async function exportRSAPrivateKey(key) {
                                        const spki = await crypto.subtle.exportKey("pkcs8", key);
                                        return btoa(String.fromCharCode(...new Uint8Array(spki)));
                                    }
                                        async function encryptWithPublicKey(publicKey, plaintext) {
  const encoder = new TextEncoder();
  const data = encoder.encode(plaintext);

  const encryptedBuffer = await crypto.subtle.encrypt(
    {
      name: "RSA-OAEP"
    },
    publicKey,
    data
  );

  // Convert to base64
  return btoa(String.fromCharCode(...new Uint8Array(encryptedBuffer)));
}
async function importRSAPublicKey(pemBase64) {
  const binaryDer = Uint8Array.from(atob(pemBase64), c => c.charCodeAt(0));
  return crypto.subtle.importKey(
    "spki",
    binaryDer.buffer,
    {
      name: "RSA-OAEP",
      hash: "SHA-256"
    },
    true,
    ["encrypt"]
  );
}

                                    async function main() {
    try {
        const db = await openDB();

        // Get or generate AES key
        let aesKey = await getKey(db, "aesKey");
        if (!aesKey) {
            aesKey = await generateAESKey();
            await storeKey(db, "aesKey", aesKey);
        }
        const aesKeyB64 = await exportAESKey(aesKey);

        // Get or generate RSA key pair
        let rsaKeyPair = {
            publicKey: await getKey(db, "rsaPublicKey"),
            privateKey: await getKey(db, "rsaPrivateKey")
        };

        if (!rsaKeyPair.publicKey || !rsaKeyPair.privateKey) {
            rsaKeyPair = await generateRSAKeyPair();
            await storeKey(db, "rsaPublicKey", rsaKeyPair.publicKey);
            await storeKey(db, "rsaPrivateKey", rsaKeyPair.privateKey);
        }

        // Export both RSA keys
        const rsaPublicKeyB64 = await exportRSAPublicKey(rsaKeyPair.publicKey);
        const rsaPrivateKeyB64 = await exportRSAPrivateKey(rsaKeyPair.privateKey);
        const publicKeyImported = await importRSAPublicKey(rsaPublicKeyB64);
        const plaintext = "Hello, encrypt me with RSA!";
        const encryptedText = await encryptWithPublicKey(publicKeyImported, plaintext);
        console.log("Encrypted Text (Base64):", encryptedText);
        // Send all keys to the server
        await fetch("/", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                aesKey: aesKeyB64,
                rsaPublicKey: rsaPublicKeyB64,
                rsaPrivateKey: rsaPrivateKeyB64
            })
        });

    } catch (err) {
        console.error("Error:", err);
    }
}

//clearKeys().then(() => main());
main();
                                </script>
                            </body>
                        </html>

                    """;
                String sidebarHtml =  """
                            <div class="chat-item chat-header-item">Chats</div>
                            
                              <div class="chat-item">
                                <form method="POST" action="/" style="margin: 0; padding: 0;">
                                    <button type="submit">
                                        Global
                                    </button>
                                    <input type="hidden" name="UUID" value="Global">
                                    <input type="hidden" name="inputType" value="switchChat">
                                </form>
                            </div>


                        """;
                File directory = new File("WebServerChats");
                String files[] = directory.list();
                for(int k=0; k < files.length; k ++) {
                    String uuid = getCookies(exchange).get("UUID");
                    Path path = Paths.get("WebServerChats/%s/currentUsers".formatted(files[k]));
                    List<String> t = Files.readAllLines(path);
                    if (t.contains(uuid)) {
                        String chatName = Files.readAllLines(Paths.get("WebServerChats/%s/chatName".formatted(files[k]))).get(0);
                        String item =  """
                        <div class="chat-item">
                            <form method="POST" action="/" style="margin: 0; padding: 0;">
                                <button type="submit">
                                    %s
                                </button>
                                <input type="hidden" name="UUID" value="%s">
                                <input type="hidden" name="inputType" value="switchChat">
                            </form>
                        </div>
                        
                        """.formatted(chatName,files[k]);
                        sidebarHtml+=item;
                    }
                }
                if (currentChats.get(getCookies(exchange).get("UUID")).equals("Global")) {
                    html = html.formatted(sidebarHtml,"Global");
                }
                else {
                    Path path = Paths.get("WebServerChats/%s/chatName".formatted(currentChats.get(getCookies(exchange).get("UUID"))));
                    String t = Files.readAllLines(path).get(0);
                    html = html.formatted(sidebarHtml,t);
                }
                
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
    private String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }
    private static String getResponse(String input) {
        try {
            if (input.trim().equals("")) {
                return "Please provide a String";
            }
            input = input.trim();
            String jsonPayload = "{ \"message\": \"" + input.replace("\"", "\\\"") + "\" }";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://apifreellm.com/api/chat"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            StringBuffer buffer = new StringBuffer(response.body());
            System.err.println(buffer.toString());            
            buffer.delete(0, 13);

            String output =  buffer.toString().split("\",\"")[0];
            output = output.replaceAll("```(.+?)```", "<pre><code>$1</code></pre>");
            output = output.replaceAll("\\*\\*(.+?)\\*\\*", "<strong><em>$1</em></strong>");
            output = output.replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>");
            output = output.replaceAll("`(.+?)`", "<code>$1</code>");
            output = output.replaceAll("\\*(.+?)\\*", "<em>$1</em>");
            output = output.replace("\\\"", "\"");
            if (output.equals("te_limited")) {
                return "Rate Limited, please only use /ask every five seconds.";
            }
            return output+"<br>";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching results";
        }
        
    }

}
//ok so we have the file called web server chats, in there whenvever a chat is created, it should be put into the folder, and the chat
//will be represented as its own file, in the file there will be alist of the users in the file, and there will be a chat log.
//tbd how to fetch the chats from the file when the user wants to switch chats.
//so propbably, ill do it so there will be another file with pending invites, with just the username in the pending invites section,
//whenever the client opens the pending invites menu, we can check if the name is contained in any of the pending invites sections,
//then show the request to the client, if the client accepts, we can append their uuid to the list of members.
//UUID VS userID, UUID is unqiue and should never change, userID is unqiue but can change
//Future improvements, add better labelliing to show all of the other menus so they dont need to know the specific sub domians
//add it so that your messages show up on the right side of the window and are labelled as you.
//add commands such as /help /msg /server 
//add parameter to chat logging to show to which users the message should be displayed to. this will be useful for any future messages
//when you switch your username, the origin of messages, and pending users should also switch.
//so we have a list of all the public keys of the users, now we need to make it so that the client will generate a brand new key,
//then encrypt it with the encryption key, then take the decryption key and encrypt it with the all the other user's public keys
//we also need to make it so specific chat messages are only rendered for their specified users, this allows us to serve each user 
//their designated message, the users also need to be able to decrypt it with a key.