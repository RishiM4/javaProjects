

public class App {

	

	private static void checkForDupe(String input, String answer, int index){
        Character character = answer.charAt(index-1);
        int originalLenght = input.length();
        input = input.replaceAll(character.toString(), "");
        int numberOfChars = originalLenght-input.length()-1;
        if (numberOfChars > 1) {
            
        }
        System.out.println(numberOfChars);
    }
	public static void main(String[] args){	
        String answer = "helos";
        String input =  "hellb";
        checkForDupe(input, answer,5);
        
	}
}
