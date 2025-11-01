
package src;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class PrintWriter {
    public static void main(String[] args) {
        Path filePath = Paths.get("insert name here");
        try {
			List<String> lines = Files.readAllLines(filePath);
            
            Files.write(filePath, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}