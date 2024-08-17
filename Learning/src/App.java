import java.util.HashMap;

public class App {

	static String answer = "lleso";
    static String input =  "llell";
    private static int getChars(Character character, String input){
        int count = 0;
        for(int k = 0; k < input.length(); k++){
            if (input.charAt(k) == character) {
                count++;
            }
        }
        return count;
    }
    private static String removeChars(int count,String newCompiledAnswer,String compiledAnswer) {
        //2
        //##GYY
        //GGGYY
        
        HashMap<Integer, Boolean> index = new HashMap<Integer, Boolean>();
        StringBuffer output = new StringBuffer(compiledAnswer);
        for(int k = 0; k <5; k++) {
            index.put(k, false);
            if (newCompiledAnswer.charAt(k) != '#') {
                index.replace(k, true);
                
                
            }
        }
        System.out.println(index);

        for(int k = 4; k >=0&&count!=0; k--) {
            if (compiledAnswer.charAt(k) == 'Y' && index.get(k)) {
                output.setCharAt(k, 'X');
                count--;

            }
        }
        System.out.println(output);
        return output.toString();
    }
	private static String checkForDupe(String input, String answer, int index, String compiledAnswer){
        char character = answer.charAt(index-1);
        int numberOfChars = getChars(character, input);
        StringBuffer newCompiledAnswer = new StringBuffer(compiledAnswer);

        if (numberOfChars > 1) {
            //Chacacter = dupe character
            int k = -1;
            StringBuffer tempInput = new StringBuffer(input);
            
            while (!tempInput.toString().matches("["+character+"#]+")) {
                k++;
                
                if (input.charAt(k)!=character) {
                    tempInput.setCharAt(k, '#');
                    newCompiledAnswer.setCharAt(k, '#');
                    
                }
                
                
            }
            
            System.out.println(tempInput);
            
            System.out.println(newCompiledAnswer);
            return removeChars(numberOfChars-getChars(character, answer), newCompiledAnswer.toString(), compiledAnswer);

        }
        

        System.out.println(numberOfChars-getChars(character, answer));
        return compiledAnswer;
    }
    private static Boolean checkForGreen(String input, int index) {
        Boolean output = false;
        if (input.charAt(index) == answer.charAt(index)) {
            output = true;
        }
        return output;
    }
    private static Boolean checkForYellow(String input,  int index) {
        String temp = "" + input.charAt(index);
        
        for (int k = 0; k<5; k++) {
            if (temp.equals("" + answer.charAt(k)) ) {
                return true;
                
            }
        }
        return false;
    }
    private static String compile(String input) {
        StringBuffer output = new StringBuffer("XXXXX");
        for(int p = 0; p < 5; p++) {
            if(checkForYellow(input, p)) {
                output.setCharAt(p, 'Y');
            }
        }
        for(int p = 0; p <5; p++) {
            if(checkForGreen(input, p)) {
                output.setCharAt(p, 'G');
                
            }
        }
        System.out.println(output.toString());
        return output.toString();
    }
	public static void main(String[] args){	
        String output=compile(input);
       for (int k = 1; k<=5; k++){
            output = checkForDupe(input, answer,k,output);
       }
       
        

        
        System.out.println("HI "+output);
	}
}