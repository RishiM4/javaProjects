/*pg 91 
Implement a method to perform basic string compression using counts of repeated characters. If the compressed string is longer than the 
uncompressed string, return the uncompressed string. Assume that the string has only uppercase and lowercase letters from a-z
*/

import java.util.Scanner;

public class StringCompression {
    private static String encode(String input) {
        String compressed = "";
        int i = 0;
        while(compressed.length() < input.length() && i<input.length()-1) {
            if (input.charAt(i)==input.charAt(i+1)) {
                int k = i+1;
                int count = 1;
                while(k < input.length() && input.charAt(i)==input.charAt(k)) {
                    count++;
                    k++;
                }
                compressed = compressed + input.charAt(i) + count;
                i = i + count - 1;
            }
            else {
                compressed += input.charAt(i);
            }
            i++;
        }
        return compressed;
    }
    private static String decode(String input) {
        String decompressed = "";
        
        return decompressed;
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        

        System.out.println(encode(input));
        scanner.close();
        decode(input);
    }
}
