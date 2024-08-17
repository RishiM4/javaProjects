import java.util.Scanner;

class scannerTest {
    public String ScanString(){
        try (Scanner scanner = new Scanner(System.in)) {
            String input =  scanner.nextLine();
            scanner.next();
            return input;
        }
    } 
    public static void main(String[] args) {
        

        
        //String test = ScanString();
        //System.err.println(test);
        
        

   
    
    }
}