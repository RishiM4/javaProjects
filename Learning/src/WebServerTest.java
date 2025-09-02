import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebServerTest implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String s = """
                <!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Chat Interface</title>
  <style>
    body {
      margin: 0;
      font-family: sans-serif;
    }

    .container {
      display: flex;
      height: 100vh;
    }

    .sidebar {
      width: 250px;
      background-color: #f0f0f0;
      border-right: 1px solid #ccc;
      overflow-y: auto;
    }

    .chat-list {
      list-style: none;
      padding: 0;
      margin: 0;
    }

    .chat-list li {
      padding: 15px;
      border-bottom: 1px solid #ddd;
      cursor: pointer;
    }

    .chat-list li:hover {
      background-color: #e0e0e0;
    }

    .chat-window {
      flex: 1;
      display: flex;
      flex-direction: column;
    }

    .messages {
      flex: 1;
      padding: 15px;
      overflow-y: auto;
      background-color: #fff;
    }

    .input-area {
      padding: 10px;
      border-top: 1px solid #ccc;
      background-color: #fafafa;
    }

    .input-area form {
      display: flex;
    }

    .input-area input[type="text"] {
      flex: 1;
      padding: 10px;
    }

    .input-area button {
      padding: 10px;
    }
  </style>
</head>
<body>
  <div class="container">
    <!-- Sidebar -->
    <div class="sidebar">
      <ul class="chat-list" id="chatList">
        <!-- Chat items will be added here -->
      </ul>
    </div>

    <!-- Chat Window -->
    <div class="chat-window">
      <div class="messages" id="chatMessages">
        <!-- Messages will appear here -->
      </div>

      <div class="input-area">
        <form id="messageForm">
          <input type="text" name="message" id="messageInput" autocomplete="off" required>
          <button type="submit">Send</button>
        </form>
      </div>
    </div>
  </div>

  <script>
    // Sample list of chats (you'd fetch this from your Java server)
    const chats = ["Alice", "Bob", "Charlie"];
    const chatListEl = document.getElementById("chatList");
    const chatMessagesEl = document.getElementById("chatMessages");
    const messageForm = document.getElementById("messageForm");
    const messageInput = document.getElementById("messageInput");

    let currentChat = null;
    const chatHistory = {};

    function loadChat(chatName) {
      currentChat = chatName;
      chatMessagesEl.innerHTML = "";
      const messages = chatHistory[chatName] || [];

      messages.forEach(msg => {
        const div = document.createElement("div");
        div.textContent = msg;
        chatMessagesEl.appendChild(div);
      });
    }

    // Build the sidebar list
    chats.forEach(chat => {
      const li = document.createElement("li");
      li.textContent = chat;
      li.onclick = () => loadChat(chat);
      chatListEl.appendChild(li);
    });

    // Handle message send
    messageForm.onsubmit = (e) => {
      e.preventDefault();
      if (!currentChat) return;

      const msg = messageInput.value.trim();
      if (msg === "") return;

      // Save message
      if (!chatHistory[currentChat]) chatHistory[currentChat] = [];
      chatHistory[currentChat].push(msg);

      // Update UI
      const div = document.createElement("div");
      div.textContent = msg;
      chatMessagesEl.appendChild(div);
      messageInput.value = "";
    };

    // Load first chat by default
    if (chats.length > 0) loadChat(chats[0]);
  </script>
</body>
</html>


                """;
                byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

}
