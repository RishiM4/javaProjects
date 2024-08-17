import java.io.*;

public class printWriterTest {
    public static void main(String[] args) {
        String text = "Hell world!"; // Your text data
        try (PrintWriter out = new PrintWriter("date.txt")) {
            out.println(text); // Write the text to the file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}