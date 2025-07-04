//Given a smaller string s and a longer string b, design an algorithm to find all permutations of the shorter string within the longer one.
//Print the location of each permutation. 

import java.util.HashMap;
import java.util.Scanner;

public class PermutationChecker {
    private static boolean check (String substring, HashMap<Character,Integer> t) {
        HashMap<Character,Integer> f = new HashMap<>();
        for(Character key : t.keySet()) {
            f.put(key,t.get(key));
        }
        for(int k = 0; k < substring.length(); k++) {
            if (!f.containsKey(substring.charAt(k))||f.get(substring.charAt(k))==0) {
                return false;
            }
            else {
                f.replace(substring.charAt(k), f.get(substring.charAt(k))-1);
            }
            
        }



        return true;


    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String b = scanner.nextLine();
        String s = scanner.nextLine();
        b=b.toLowerCase();
        s=s.toLowerCase();
        HashMap<Character,Integer> t = new HashMap<Character,Integer>();
        for(int k = 0; k < s.length(); k++) {
            if (t.containsKey(s.charAt(k))) {
                t.replace(s.charAt(k), t.get(s.charAt(k))+1);
            }
            else {
                t.put(s.charAt(k),1);
            }
        }
        System.err.println("\n");
        System.err.println("\n");
        System.err.println("\n");
        System.err.println("\n");
        System.err.println("\n");
        for(int k=0; k <= b.length()-s.length(); k++) {
            if(check(b.substring(k, k+s.length()), t)) {
                System.out.println("Permutation '"+b.substring(k,k+s.length())+"' was found from index "+k+" to index "+(k+s.length()));
            }
        }



        scanner.close();
    }
}
