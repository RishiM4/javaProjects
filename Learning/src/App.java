import java.util.concurrent.*;

public class App {

    

    public static void main(String[] args) throws Exception {
        int moves[] = new int[256];
        moves[0] = 1;
        moves[moves[0]++] = 1;
        moves[moves[0]++] = 1;
        moves[moves[0]++] = 1;
        System.err.println(moves[4]);
    }
}
