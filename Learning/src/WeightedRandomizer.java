import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.lang.Math;
public class WeightedRandomizer {
    public Double weightedRandom(ArrayList<Double> input){
        int decimalPlaces = 0;
        for(int k =0; k < input.size(); k++) {
            int j =0;
            while (Math.round(input.get(k)*Math.pow(10, j))/Math.pow(10, j)!=input.get(k)) {
                j++;
            }
            if (j>decimalPlaces) {
                decimalPlaces = j;
            }
        }
        HashMap<Integer,Double> weightedIndex = new HashMap<Integer,Double>();
        for(int k =0; k < input.size(); k++){
            weightedIndex.put(k, (input.get(k)*Math.pow(10, decimalPlaces)));
        }
        double totalWeight = 0;
        for(int k =0; k < input.size(); k++){
            totalWeight += input.get(k);
        }
        Random random = new Random();
        double output = random.nextDouble(totalWeight);
        for(int k =0; k < input.size(); k++){
            if (output<input.get(k)) {
                
                return k+0.0;
            }
            else{
                output -= input.get(k);
            }
        }
        return 0.0;
    }
    public static void main(String[] args) {
        

        HashMap<Integer,Double> output = new HashMap<Integer,Double>();
        output.put(0, 0.0);
        output.put(1, 0.0);
        output.put(2, 0.0);
        output.put(3, 0.0);
        for(int k =0; k < 10000; k++){
            double num = 0;
            if (num == 0) {
                output.put(0, output.get(0)+1);
            }
            if (num == 1) {
                output.put(1, output.get(1)+1);
            }
            if (num == 2) {
                output.put(2, output.get(2)+1);
            }
            if (num == 3) {
                output.put(3, output.get(3)+1);
            }
        }
        System.out.println(output.get(0)/1000);
        System.out.println(output.get(1)/1000);
        System.out.println(output.get(2)/1000);
        System.out.println(output.get(3)/1000);

    }
}
