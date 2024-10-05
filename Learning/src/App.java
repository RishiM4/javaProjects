import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class App {
    

    
    public static void main(String[] args) {
        double myvalue = 193243148310010d;

        //Option 1 Print bare double.
        System.out.println(myvalue);

        //Option2, use decimalFormat.
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        System.out.println(df.format(myvalue));

        //Option 3, use printf.
        System.out.printf("%.9f", myvalue);
        System.out.println();

        //Option 4, convert toBigDecimal and ask for toPlainString().
        System.out.print(new BigDecimal(myvalue).toPlainString());
        System.out.println();

        //Option 5, String.format 
        System.out.println(String.format("%.12f", myvalue));
        
        

        

        
    }

    
}
