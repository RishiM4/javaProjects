
public class WordleFiveLetterDriver {
    WordleFiveLetter wordle = new WordleFiveLetter();
    public void start() {
        Thread t = new Thread(() -> {
            while(true) {
            System.out.println(wordle.getRestart());
            if (wordle.getRestart() == true) {
                wordle = new WordleFiveLetter();
                wordle.start();
            }
        }
        });
        wordle.start();
        t.start();
    }
    public static void main(String[] args) {
        new WordleFiveLetterDriver().start();
        
    }
}
