
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.io.IOException;
import java.lang.Math;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
public class PrimeNumberFinder {
    static int numberOfPrimeNumbers = 1; 
    static Boolean timerActive = true;
    static long primeNumber = 1;
    static long number = 3;
    static ArrayList<Long> list = new ArrayList<Long>();
    static double startTime = 0;
    static Calendar calandar = Calendar.getInstance();
    static boolean writeToFile = false;
    private static Boolean checkIfPrime(double input){
        double max = Math.sqrt(input);
        int k = 0;
        while (list.get(k)<=max) {
            k++;
            double temp = input/list.get(k); 
            if ((int)(temp)==temp) {
                return false;
            }
        }
        
        return true;
    }
    private static void updateFile(){
        if (!writeToFile) {
            
            return;
        }
        Path filePath = Paths.get("primeNumberData.txt");
        try {
			List<String> lines = new ArrayList<String>();
            for(int k =0; k < list.size(); k++){
                lines.add(""+list.get(k));
            }
            Files.write(filePath, lines);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public static void main(String[] args) {
        if (writeToFile) {
            Path filePath = Paths.get("primeNumberData.txt");
            
            try {
			    List<String> lines = Files.readAllLines(filePath);
                numberOfPrimeNumbers = lines.size();
                number = Integer.parseInt(lines.get(lines.size()-1));
                for(int k =0; k < lines.size(); k++){
                    int temp = Integer.parseInt(lines.get(k));
                    list.add((long)temp);
                }
		    } catch (IOException e) {
			    e.printStackTrace();
		    }
        }
        else{
            list.add((long)3);
        }
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timerActive = false;
                calandar = Calendar.getInstance();
                System.out.println("Program Ran For : "+(calandar.getTimeInMillis()-startTime)+"ms");
                System.out.println("Prime Numbers Detected: "+numberOfPrimeNumbers);
                DecimalFormat df = new DecimalFormat("#");
                df.setMaximumFractionDigits(8);
                System.out.println("Largest Prime Number : "+df.format(primeNumber));
                updateFile();
                System.exit(0);
            }
        };
        timer.schedule(task, 30000);
        calandar = Calendar.getInstance();
        startTime = calandar.getTimeInMillis();
        while (timerActive) {
            if (checkIfPrime(number)) {
                primeNumber = number;
                list.add(number);
                numberOfPrimeNumbers++;
            }
            number += 2;
            
        }
    }
    
    
}
