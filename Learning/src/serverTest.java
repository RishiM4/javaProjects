import java.io.IOException;
import java.net.ServerSocket; 
import java.net.Socket;
public class ServerTest { 
    public static void main(String args[] ) throws IOException { 
        try (ServerSocket server = new ServerSocket(8080)) {
            System.out.println("Listening for connection on port 8080 ...."); 
            while (true) { 
                try(Socket socket = server.accept()){
                    String httpResponse = "HTTP/1.1 200 OK\r\n\r\nHello Rishi!"; 

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    socket.getOutputStream().write(httpResponse.getBytes()); 
                }
                
                
                

            }
            
        } 
        
        
    } 
}
