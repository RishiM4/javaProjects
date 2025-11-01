package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class AutoCorrect {
    
    static List<String> list = new ArrayList<String>();
    
    static HashMap<String, Integer> finalList = new HashMap<String, Integer>();
    private static String sortAlphabetically(String base, String input){

        int result = base.compareToIgnoreCase(input);
        if (result < 0) {
            return "more";
        }
        else if (result > 0){
            return "less";
        }
        else {
            return "equal";
        }
       
        
    }
    private static boolean binarySearch(String input, int min, int max, List<String> lines){
        if (max-min == 0) {
            if (input.equals(lines.get(min))) {
                return true;
            }
            return false;
        }
        if (max-min == 1||max-min == 0||max-min == 2) {
            try {
                if (input.equals(lines.get(min-2))) {
                    return true;
                }
                if (input.equals(lines.get(min-1))) {
                    return true;
                }
                if (input.equals(lines.get(min))) {
    
                    return true;
                }
                if (input.equals(lines.get(min+1))) {
    
                    return true;
                }
            } catch (Exception e) {
            }
            
            return false;
        }
        if (max-min == 2) {
            if (input.equals(lines.get(min-1))) {
                return true;
            }
            if (input.equals(lines.get(min+1))) {

                return true;
            }
            
            return false;
        }
        if (max-min == 3) {
            if (input.equals(lines.get(min))) {
                return true;
            }
            if (input.equals(lines.get(min+1))) {

                return true;
            }
            if (input.equals(lines.get(min+2))) {

                return true;
            }
            
            return false;
        }
        if (max-min == 4) {
            if (input.equals(lines.get(min))) {
                return true;
            }
            if (input.equals(lines.get(min+1))) {

                return true;
            }
            if (input.equals(lines.get(min+2))) {

                return true;
            }
            if (input.equals(lines.get(min+3))) {

                return true;
            }
            
            return false;
        }
        if (max-min == 5) {
            if (input.equals(lines.get(min))) {
                return true;
            }
            if (input.equals(lines.get(min+1))) {

                return true;
            }
            if (input.equals(lines.get(min+2))) {

                return true;
            }
            if (input.equals(lines.get(min+3))) {

                return true;
            }
            if (input.equals(lines.get(min+4))) {

                return true;
            }
            
            return false;
        }
        int size = max - min;
        size = size/2;
        size = size + min;
        String result = sortAlphabetically(lines.get(size), input);
        if (result.equals("less")) {
            return binarySearch(input, min,size,lines);
        }
        else if (result.equals("more")){
            return binarySearch(input, size,max,lines);
        }
        else {
            return true;
        }
    }
    private static boolean binarySearch(String input, List<String> lines){
        if (lines.size() == 1) {
            if (input.equals(lines.get(0))) {
                return true;
            }
            return false;
        }
        if (lines.size() == 2) {
            if (input.equals(lines.get(0))) {
                return true;
            }
            if (input.equals(lines.get(1))) {
                return true;
            }
            return false;
        }
        if (lines.size() == 3) {
            if (input.equals(lines.get(0))) {
                return true;
            }
            if (input.equals(lines.get(1))) {
                return true;
            }
            if (input.equals(lines.get(2))) {
                return true;
            }
            return false;
        }
        int size = lines.size();
        size = size/2;
        String result = sortAlphabetically(lines.get(size), input);
        
        if (result.equals("less")) {
            return binarySearch(input, 0,size-1,lines);
        }
        else if (result.equals("more")){
            return binarySearch(input, size+1,lines.size(),lines);
        }
        else {
            return true;
        }
        
        
    }
   
    private static void checkWordInsert(String word, int depth) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        if (depth > 1) {
            return;
        }
        //insert
        for(int k = 0; k <= word.length(); k++) {
            //System.out.println(k);
            for(int j = 0; j < 26; j++){
                StringBuffer temp = new StringBuffer(word);
                temp.insert(k, alphabet.charAt(j));
                //System.out.println(temp.toString());
                if (binarySearch(temp.toString(), list)) {
                    
                    if (finalList.get(temp.toString())==null) {
                        
                        finalList.put(temp.toString(), depth);
                        System.out.println(depth);
                    }
                    if (finalList.get(temp.toString())> depth) {
                        finalList.remove(temp.toString());
                        finalList.put(temp.toString(), depth);
                    }
                }
                else if (temp.toString().equals("hello")) {
                    System.out.println(temp.toString());
                    
                }
                else{
                    checkWordInsert(temp.toString(), depth+1);
                }
                


                
            }
        }
        //delete
        //replace
        
    }
    private static void checkWordDelete(String word, int depth){
        if (depth > 1) {
            return;
        }
        //delete
        for(int k = 0; k <= word.length()-1; k++) {
            //System.out.println(k);
            for(int j = 0; j < 26; j++){
                StringBuffer temp = new StringBuffer(word);
                temp.deleteCharAt(k);
                //System.out.println(temp.toString());
                if (binarySearch(temp.toString(), list)) {
                    
                    if (finalList.get(temp.toString())==null) {
                        
                        finalList.put(temp.toString(), depth);
                        System.out.println(depth);
                    }
                    if (finalList.get(temp.toString())> depth) {
                        finalList.remove(temp.toString());
                        finalList.put(temp.toString(), depth);
                    }
                }
                else if (temp.toString().equals("hello")) {
                    System.out.println(temp.toString());
                    
                }
                else{
                    checkWordInsert(temp.toString(), depth+1);
                }
                


                
            }
        }
        //delete

    }
    private static void checkWordReplace(String word, int depth){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        if (depth > 1) {
            return;
        }
        //delete
        for(int k = 0; k <= word.length()-1; k++) {
            //System.out.println(k);
            for(int j = 0; j < 26; j++){
                StringBuffer temp = new StringBuffer(word);
                temp.setCharAt(k, alphabet.charAt(j));
                //System.out.println(temp.toString());
                if (binarySearch(temp.toString(), list)) {
                    System.out.println(depth);
                    if (finalList.get(temp.toString())==null) {
                        
                        finalList.put(temp.toString(), depth);
                        System.out.println(depth);
                    }
                    if (finalList.get(temp.toString())> depth) {
                        finalList.remove(temp.toString());
                        finalList.put(temp.toString(), depth);
                    }
                }
                else{
                    checkWordInsert(temp.toString(), depth+1);
                }
                


                
            }
        }
    }
    public static void main(String[] args) {
        Path filePath = Paths.get("words.txt");
        try {
			list = Files.readAllLines(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a word");
        String word = scanner.nextLine();
        word = word.toLowerCase();
        checkWordInsert(word, 0);
        checkWordDelete(word, 0);
        checkWordReplace(word, 0);
        System.out.println("Did You Mean?");
        for(int k = 0; k < finalList.size(); k++){
            System.out.println(finalList.get(k));
        }
        scanner.close();
        System.out.println(finalList);
        
        
    }
}
