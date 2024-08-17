public class BubbleSort {
    private static int[] sortDigit(int character, int[] input) {
        
        int length = input.length-1;
        int[] output = input;
        
        int k = 0;
        for(;length>character; length--) {
        
            int intOne = input[k];
            int intTwo = input[k+1];
            if (intOne > intTwo) {
                //swap
                output[k] = intTwo;
                output[k+1] = intOne;
            }
            else{
                //dont swap
            }
            k++;

        }
        return output;
    }
    private static void printResults(int[] input) {
        
        for (int k = 0; k < input.length; k++) {
            System.out.println(input[k]);
        }
        
    }
    public static void main(String[] args){
		int[] input = {5,4,3,2,1,88};
        
        int character = 0;
        for (int length = input.length -2;length >= 0; length--) {
            sortDigit(character, input);
            character++;
        }
        printResults(input);

	}
}
