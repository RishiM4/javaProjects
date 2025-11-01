package src.battleship;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class BattleShipServer {
    ArrayList<BattleShipClientHandler> clientHandlers;
    public ArrayList<BattleShipClientHandler> getClientHandlers() {
        return clientHandlers;
    }
    public void removeHandler(BattleShipClientHandler handler) {
        clientHandlers.remove(handler);
    }
    private void createDummySocket() {
        try (Socket socket = new Socket("localhost", 1085)){} 
        catch (Exception e) {}
    }
    public void run() {
        clientHandlers = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(1085)) {
            createDummySocket();
            serverSocket.accept();
            while(true) {
                Socket client = serverSocket.accept();
                BattleShipClientHandler handler = new BattleShipClientHandler(client, this);
                clientHandlers.add(handler);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        new BattleShipServer().run();
    }

    
}
