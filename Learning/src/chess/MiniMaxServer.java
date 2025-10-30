package chess;

import java.io.*;
import java.net.*;
import chess.BoardV3.Move;
public class MiniMaxServer {
    public static void main(String[] args) throws IOException{
        int port = 6000;
        
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("listening on port " + port);

        while (true) {
            try(Socket socket = serverSocket.accept()) {
            
                System.out.println("Python connected!");

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String line;
                String output = "error";
                while ((line = in.readLine()) != null) {
                    System.err.println(line);
                    if (line.split(":")[0].equals("fen")) {
                        String fen = line.split(":")[1];
                        BoardV3 board = new BoardV3();
                        board.setFEN(fen);
                        Move move = MiniMaxV6.iterate(-1, 1000, board);
                        output = move.from + "," + move.to;
                    }

                    out.write(output +  "\n");
                    out.flush();
                }

                socket.close();
                System.out.println("client disconnected.");
            }
            catch (Exception e) {
            }
        }
    }
}
