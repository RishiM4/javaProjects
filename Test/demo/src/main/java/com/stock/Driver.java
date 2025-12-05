package com.stock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Driver {
    static Scanner scanner;
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    public static void getStocks(){
        Path path = Paths.get("config.txt");
        try {
            Files.write(path, new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            System.err.println("Please enter a stock, when you are done, exit the terminal and rerun");
            String input = scanner.next().toUpperCase();
            
            clearConsole();
            final boolean[] loading = {true};

            Thread t = new Thread(()->{
                int k =0;
                String[] strings = {"|","/","-","\\"};
                while (loading[0]) {
                    System.err.print("\r%s Validating Stock: ".formatted(strings[k])+input+"      ");
                    k++;
                    if (k==4) {
                        k=0;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
            });
            
            t.start();
            boolean isValid = TickerValidator.isValidTicker(input);
            loading[0] = false;
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clearConsole();

            if (!isValid) {
                System.err.println("Ticker: '%s' is invalid".formatted(input));
            }
            else{
                System.err.println("Ticker valid, caching results...");
                lines.add(input);
                try {
                    Files.write(path, lines);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clearConsole();
                System.err.println("Ticker: %s cached!".formatted(input));
            }
            
            
        }
    }
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("config.txt");
        clearConsole();
        
        scanner = new Scanner(System.in);
        try {
            Files.readAllLines(path);
            System.err.println("Located config file!");
        } catch (Exception e) {
            System.err.println("Failed to find config file, generating now...");
            getStocks();
        }
        try {
            Files.readAllLines(Paths.get("Stocks.csv"));
        } catch (Exception e) {
            List<String> temp = new ArrayList<>();
            temp.add("Dividend Yield,PE Ratio,Price,Symbol,Sector,Market Cap,Dividend Per Share,Cash on hand,Name");
            Files.write(Paths.get("Stocks.csv"),temp);
        }
        clearConsole();
        List<String> lines = Files.readAllLines(path);
        Iterator<String> iterator = lines.iterator();
        while (iterator.hasNext()) {
            String ticker = iterator.next();
            System.err.println("Current ticker: "+ticker);
            final boolean[] loading = {true};

            Thread t = new Thread(()->{
                int k = 0;
                String[] strings = {"|","/","-","\\"};
                while (loading[0]) {
                    System.err.print("\r"+strings[k]+" Fetching data for ticker: "+ticker);
                
                    k++;
                    if (k==4) {
                        k=0;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }

                }
            });
            t.start();

            Iterator<String> values = DataFetcher.fetchData(ticker).values().iterator();
            loading[0]=false;
            
            clearConsole();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean noErrors = true;
            String output = "";
            while(values.hasNext()){
                String string = values.next();
                if (string=="null") {
                    noErrors = false;
                    break;
                }
                output+=","+string;
            }
            if (noErrors) {
                System.err.println("Sucess: "+output.replaceFirst(",",""));
                List<String> csv = Files.readAllLines(Paths.get("Stocks.csv"));
                csv.add(output.replaceFirst(",", ""));
                Files.write(Paths.get("Stocks.csv"), csv);


                List<String> temp = Files.readAllLines(path);
                temp.remove(ticker);
                Files.write(path, temp);

            }
            else {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
        scanner.close();
        lines = Files.readAllLines(path);
        if (lines.isEmpty()) {
            System.err.println("Completed!!! Please open Stocks.csv in excel to view the data.");
            File file = new File("config.txt");
            file.delete();
        }
        else{
            System.err.println("Rate limited, program terminating. Please wait a second and rerun.");
        }
    }
}
