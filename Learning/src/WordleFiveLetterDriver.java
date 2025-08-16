
public class WordleFiveLetterDriver {
    WordleFiveLetter wordle = new WordleFiveLetter();
    public void start() {
        Thread t = new Thread(() -> {
            while(true) {
                if (wordle.getRestart() == true) {
                    wordle = new WordleFiveLetter();
                    wordle.start();
                
                }
            }
        });
        t.start();
        wordle.start();
        
    }
    public static void main(String[] args) {
        new WordleFiveLetterDriver().start();
        
    }
}
