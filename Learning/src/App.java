import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class App {
    public static void main(String[] args) {
        Path filePath1 = Paths.get("wordleInputData.txt");
        String output = "";
        try {
            List<String> lines = Files.readAllLines(filePath1);
            
            try {
                for(int k = 0; k <10000; k++) {
                    output = output+lines.get(k)+" ";
                }
            } catch (Exception e) {
                //System.out.println(output);
            }
            StringBuffer temp = new StringBuffer(output);
            //System.out.println(temp);
             
            String tem="";
            try {
                while(temp.toString().contains(" ")) {
                    //System.out.println("HI");
                    tem = tem+temp.charAt(0)+temp.charAt(1)+temp.charAt(2)+temp.charAt(3)+temp.charAt(4)+"\n";
                    temp.deleteCharAt(0);
                    temp.deleteCharAt(0);
                    temp.deleteCharAt(0);
                    temp.deleteCharAt(0);
                    temp.deleteCharAt(0);
                    temp.deleteCharAt(0);
    
                }
            } catch (Exception e) {
                
                
            }
            //tem = tem+temp.charAt(0)+temp.charAt(1)+temp.charAt(2)+temp.charAt(3)+temp.charAt(4)+"\n";

            System.out.println(tem);
            
            
            
        } catch (IOException e) {
        }
        
        try {
           
            
           
            
           

        } catch (Exception e) {

        }
        
    }
}
