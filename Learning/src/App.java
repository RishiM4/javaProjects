package src;

public class App {

    // Simulated long task (like pinging a server)
    public static void pingServer() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        boolean[] loading = { true };

        Thread spinner = new Thread(() -> {
            char[] frames = { '|', '/', '-', '\\' };
            int i = 0;

            while (loading[0]) {
                System.out.print("\r" + frames[i % frames.length] + " Loading...");
                i++;

                try {
                    Thread.sleep(120); // animation speed
                } catch (InterruptedException e) {
                    return;
                }
            }

            System.out.print("\rDone!           \n");
        });

        spinner.start();

        pingServer();

        loading[0] = false;

        spinner.join();
    }
}
