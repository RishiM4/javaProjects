package battleship;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Optional;
public class BattleShipClientHandler implements Runnable{
    private static final String regex = "!:!";
    private BufferedReader reader;
    private BufferedWriter writer;
    private BattleShipServer server;
    private Socket socket;
    private boolean matchmaking = true;
    private String playerName;
    private BattleShipClientHandler opponentHandler;
    public BufferedWriter opponentWriter;
    public BattleShipClientHandler(Socket socket, BattleShipServer server) {
        try {
        
            this.socket = socket;
            this.server = server;
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket() {
        return this.socket;
    }
    public String getPlayerName() {
        return this.playerName;
    }
    public String getReply() {
        String s;
        try {
            while ((s = reader.readLine()) != null) {
                if (s.equals("0000")) {
                    server.removeHandler(this);
                    socket.close();
                }
                return s;
            }
        } catch (Exception e) {
            e.printStackTrace();
            server.removeHandler(this);
        }
        return null;
        
    }
    private Optional<BattleShipClientHandler> findOtherCliendHandler(String target, ArrayList<BattleShipClientHandler> handlers) {
        for(int k = 0; k < handlers.size(); k++) {
            if (handlers.get(k).getPlayerName().equals(target)) {
                System.err.println("Made connection with other handler: " + handlers.get(k).playerName);
                return Optional.of(handlers.get(k));
            }
             
        }
        return Optional.empty();
    }
    public void write(String s) {
        try {
            System.err.println("Sending a message now!");
            writer.write(s+"\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isMatchmaking() {
        return matchmaking;
    }
    @Override

    public void run() {
        write("0001"+regex);
        String reply;
        while ((reply = getReply())!= null) {
            System.err.println(reply);
            if (reply.split(regex)[0].equals("0002")) {
                this.playerName = reply.split(regex)[1];
                System.out.println(this.playerName);
                System.err.println("");
                break;
            }
            
        }
        matchmaking = true;
        System.err.println("Started matchkaming");
        
        while ((reply = getReply())!= null) {
            System.err.println(reply.split(regex)[0]);
            String currentRegex = reply.split(regex)[0];
            if (currentRegex.equals("0003")) {
                System.err.println("sending hanlers");
                int numOfOpponents = 0;
                ArrayList<BattleShipClientHandler> handlers = server.getClientHandlers();
                String otherHandlers = "";
                for(int k = 0; k < handlers.size(); k++) {
                    if (handlers.get(k)!=this&&handlers.get(k).isMatchmaking()) {
                        numOfOpponents++;
                        otherHandlers += handlers.get(k).getPlayerName();
                    }
                }
                write("0004"+regex+(numOfOpponents)+regex+otherHandlers);
            }
            else if (currentRegex.equals("0005")) {
                System.err.println("got message looking for handlers");
                ArrayList<BattleShipClientHandler> handlers = server.getClientHandlers();
                String targetHandler = reply.split(regex)[1];
                Optional<BattleShipClientHandler> opponent= findOtherCliendHandler(targetHandler, handlers);
                try {
                    if (opponent.isPresent()) {
                        
                        System.err.println("Trying to send request to other handler");
                        BufferedWriter oppWriter = new BufferedWriter(new OutputStreamWriter(opponent.get().getSocket().getOutputStream()));
                        oppWriter.write("0007"+regex+playerName+"\n");
                        oppWriter.flush();
                        System.err.println("Sent request.");

                    }
                    else {
                        write("0006");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            else if (currentRegex.equals("0008")) {
                try {
                    ArrayList<BattleShipClientHandler> handlers = server.getClientHandlers();
                    String targetHandler = reply.split(regex)[2];
                    Optional<BattleShipClientHandler> opponent= findOtherCliendHandler(targetHandler, handlers);
                    if (opponent.isPresent()) {
                        if (Integer.parseInt(reply.split(regex)[1])==0) {
                            this.opponentHandler = opponent.get();
                            this.opponentWriter = new BufferedWriter(new OutputStreamWriter(opponentHandler.getSocket().getOutputStream()));
                            this.matchmaking = false;
                            opponentWriter.write("0009" + regex + playerName+"\n");
                            opponentWriter.flush();
                        }
                        else {
                            BufferedWriter tempWriter = new BufferedWriter(new OutputStreamWriter(opponent.get().getSocket().getOutputStream()));
                            tempWriter.write("0010" + regex + playerName+"\n");
                            tempWriter.flush();
                            tempWriter.close();
                        }
                    }
                    else {
                        System.err.println("jfd");
                        write("0006");
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            else if (currentRegex.equals("0011")) {
                try {
                    Optional<BattleShipClientHandler> opponent = findOtherCliendHandler(reply.split(regex)[1], server.getClientHandlers());
                    if (opponent.isPresent()) {
                        this.opponentHandler = opponent.get();
                        this.opponentWriter = new BufferedWriter(new OutputStreamWriter(opponentHandler.getSocket().getOutputStream()));
                        this.matchmaking = false;
                        write("0012");
                        opponentWriter.write("0012"+regex+playerName+"\n");
                        opponentWriter.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            
        }
    }
}
