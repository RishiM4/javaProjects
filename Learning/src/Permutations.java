

import java.util.ArrayList;
import java.util.Scanner;

public class Permutations {
    private static ArrayList<String> findPermutations(StringBuilder current) {
        StringBuilder temp = new StringBuilder(current);
        ArrayList<String> results = new ArrayList<String>();
        for (int k = 0; k < temp.length(); k++) {
            //take a  character, assign it to the asnwer string, remove from current string, then repeat
            findPermutations(results, temp, temp.charAt(k)+"", k);
        }
        return results;
    }
    private static ArrayList<String> findPermutations(ArrayList<String> results, StringBuilder current, String answer, int remove) {
        StringBuilder temp = new StringBuilder(current);
        
       
        temp.deleteCharAt(remove);
        
       
       
        for (int k = 0; k < temp.length();k++) {
            //take a character, assign it to the asnwer string, remove from current string, then repeat
            if (temp.length() == 1) {
                if (!results.contains(answer+temp)) {
                    results.add(answer+temp);
                }
                return results;
            }
            findPermutations(results, temp, answer+temp.charAt(k), k);
        }
        return results;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder b = new StringBuilder(scanner.nextLine());
        int numOfPermutations = 1;
        for(int k = 1; k <= b.length(); k++) {
            numOfPermutations *= k;
        }
        Long currentTime = System.currentTimeMillis();
        //ArrayList<String> results = findPermutations(b);
        
        System.out.println("Code took "+(System.currentTimeMillis()-currentTime)+"ms to find " + numOfPermutations + " unique permutations!");
        
        
        /*for(int k = 0; k < results.size(); k++) {
            for(int j = 0; j < s.length()-b.length()+1; j++) {
                //System.out.println(s.substring(j, j+b.length()));
                if (s.substring(j, j+b.length()).equals(results.get(k))) {
                    System.err.println(results.get(k));
                }
            }
        }*/


        scanner.close();
    }
}
