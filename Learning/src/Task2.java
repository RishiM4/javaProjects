//Print all positive integer solutions to the equation a3 + b3 = c3 + d3 where a, b, c and d are integers between 1 and 1000.
//Optimize your solution to avoid duplicated work. 

import java.util.Scanner;

public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //ex = 1, 12 and 9,10
        try {
            int int1 = scanner.nextInt();
            int int2 = scanner.nextInt();
            double sum = Math.pow(int1, 3) + Math.pow(int2, 3);
            System.out.println("");
            System.out.println("");
            System.out.println("");
            int max = 1000;

            for(int k = 1; k <= max; k++){
                for(int j = 1; j <=k; j++){
                    if (Math.pow(k, 3)+Math.pow(j,3)==sum) {
                        System.out.println(j +" , "+k);
                    }
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        scanner.close(); 
    }
}
