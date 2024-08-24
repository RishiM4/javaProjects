
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
    static String answer = "flips";
    private static void similiarWord(){
        
        StringBuffer tempAnswer = new StringBuffer(answer);
        try {
            Path filePath = Paths.get("wordleInputData.txt");
            List<String> lines = Files.readAllLines(filePath);
            ArrayList<String> output = new ArrayList<String>();
            Random random = new Random();

            Character rhymeChar1 = tempAnswer.charAt(random.nextInt(5));
            tempAnswer.deleteCharAt(tempAnswer.indexOf(rhymeChar1+""));
            Character rhymeChar2 = tempAnswer.charAt(random.nextInt(4));
            tempAnswer.deleteCharAt(tempAnswer.indexOf(rhymeChar2+""));
            Character rhymeChar3 = tempAnswer.charAt(random.nextInt(3));

                
           
            
            for(int k =0; k < lines.size(); k++){
                String current = lines.get(k);
                if (current.contains(rhymeChar1.toString())&&current.contains(rhymeChar2.toString())&&current.contains(rhymeChar3.toString())) {
                    
                    if (!current.equals(answer)) {
                        int numberOfDupeLetters = 0;
                        for(int j = 0; j < 5; j++) {
                            if (current.contains(answer.charAt(j)+"")) {
                                numberOfDupeLetters++;
                            }
                        }
                        if (numberOfDupeLetters==3) {
                            output.add(current);
                        }
                        
                    }
                }
            }
            if (output.size()==0) {
                similiarWord();
            }
            System.out.println("The word '"+output.get(random.nextInt(output.size()))+"' contains three letters in common with the answer.");
            return;
                
        } catch (Exception e) {
            
        }
        return;
    }
    public static void main(String[] args) {
        
        similiarWord();
        
        
    }
}
