import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

public class Backups {
    private static void emptyFolder(String path){
        File directory = new File("C:\\javaBackups\\"+path);
        
        if (directory.mkdirs()) {
            System.out.println("Directory created successfully!");
        } else {
            System.out.println("Failed to create directory.");
        }
    }
    private static void traverse(String Path, String fileName){
        File file = new File("C:\\Users\\Rishi (New)\\javaProjects\\"+Path);
        if (file.isDirectory()) {
            String childFile[] = file.list();
            emptyFolder(fileName+"\\"+Path);
            for(int k = 0; k < childFile.length; k++){
                
                traverse(Path+"\\"+childFile[k],fileName);
            }
        }
        else{
            Path source = Paths.get("C:\\Users\\Rishi (New)\\javaProjects\\"+Path);
            Path destination = Paths.get("C:\\javaBackups\\"+fileName+"\\"+Path);
            try {
                Files.copy(source,destination);
            } catch (IOException e) {
                
                e.printStackTrace();
            }
        }
    }
    private static String checkForDupe(String input, int number){
        
        File file = new File("C:\\javaBackups");
        String childFile[] = file.list();
        for(int k = 0; k < childFile.length; k++){
            

            if (childFile[k].equals(input)) {
                number++;
                input = checkForDupe(input+"("+number+")", number);
            }
        }
        System.out.println(input);
        return input;
    }
    public static void main(String[] args) {
        // Specify the directory path
       
        Scanner scanner = new Scanner(System.in);
         LocalDate currentDate = LocalDate.now();
        StringBuffer temp = new StringBuffer(scanner.nextLine() +  " - " + currentDate);
        while(temp.toString().contains(":")){
            temp.setCharAt(temp.indexOf(":"), '-');
        }
        
        String fileName = temp.toString();
        
        System.out.println(fileName);
        fileName = checkForDupe(fileName,0);
        
        emptyFolder(fileName+"\\Learning");        
        traverse("",fileName+"\\Learning");
        
        try {
            FileWriter myWriter = new FileWriter("C:\\javaBackups\\"+fileName+"\\commit.txt");
            myWriter.write(scanner.nextLine());
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        scanner.close();
        
    }
}
