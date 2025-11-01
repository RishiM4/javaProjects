package src;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerE2EE implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equalsIgnoreCase("POST")) {
            InputStream input = exchange.getRequestBody();
            String body = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("Received key submission:\n" + body);
            List<String> lines = Files.readAllLines(Paths.get("webServerPublicKeys.txt"));
            for(int k = 0; k < lines.size(); k++) {
                if (!lines.get(k).split(":::")[0].equals(getCookies(exchange).get("UUID"))) {
                    lines.add(getCookies(exchange).get("UUID")+":::"+body);
                }
            }
            Files.write(Paths.get("webServerPublicKeys.txt"), lines);
            byte[] response = "OK".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
            }
        }
       String html = """
<!DOCTYPE html>
<html>
<head>
  <title>Client Key Generation & Storage</title>
  <style>
    body { font-family: sans-serif; padding: 20px; }
    pre { background: #eee; padding: 10px; border-radius: 4px; }
  </style>
</head>
<body>
  <h1>Client Key Generation & Storage</h1>

  


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
          length: 256,
        },
        true,
        ["encrypt", "decrypt"]
      );
      return key;
    }

    async function exportAESKey(key) {
      const raw = await crypto.subtle.exportKey("raw", key);
      return btoa(String.fromCharCode(...new Uint8Array(raw)));
    }

    async function generateRSAKeyPair() {
      const keyPair = await crypto.subtle.generateKey(
        {
          name: "RSA-OAEP",
          modulusLength: 2048,
          publicExponent: new Uint8Array([1, 0, 1]),
          hash: "SHA-256",
        },
        false, // PRIVATE key is NOT extractable for security
        ["encrypt", "decrypt"]
      );
      return keyPair;
    }

    async function exportRSAPublicKey(key) {
      const spki = await crypto.subtle.exportKey("spki", key);
      return btoa(String.fromCharCode(...new Uint8Array(spki)));
    }

    async function main() {
    try {
        const db = await openDB();

        // AES Key generation
        let aesKey = await getKey(db, "aesKey");
        if (!aesKey) {
            aesKey = await generateAESKey();
            await storeKey(db, "aesKey", aesKey);
        }

        const aesKeyB64 = await exportAESKey(aesKey);

        let rsaKeyPair = {
            publicKey: await getKey(db, "rsaPublicKey"),
            privateKey: await getKey(db, "rsaPrivateKey")
        };

        if (!rsaKeyPair.publicKey || !rsaKeyPair.privateKey) {
            rsaKeyPair = await generateRSAKeyPair();
            await storeKey(db, "rsaPublicKey", rsaKeyPair.publicKey);
            await storeKey(db, "rsaPrivateKey", rsaKeyPair.privateKey);
        }

        const rsaPublicKey = await exportRSAPublicKey(rsaKeyPair.publicKey);

        await fetch("/e2ee", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                key: rsaPublicKey
            })
        });

    } catch (err) {
        console.error("Error:", err);
    }
    }

  main();
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
