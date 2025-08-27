import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket; 
import java.net.Socket;
public class ServerTest { 
    public static void main(String args[] ) throws IOException { 
        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Listening for connection on port 8080 ...."); 
            while (true) { 
                try(Socket socket = server.accept()){
                    String htmlResponse = """
                            HTTP/1.1 200 OK

                            <html>
                                <head>
                                    <title>Simple Server</title>
                                </head>
                                <body>
                                    <h1>Hello, World!</h1>
                                </body>
                            </html>
                            """;

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    BufferedWriter t = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    t.write(htmlResponse);
                    t.flush();
                    
                }
                
                
                

            }
            
        } 
        
        
    } 
}
