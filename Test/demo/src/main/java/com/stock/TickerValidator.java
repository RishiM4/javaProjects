package com.stock;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TickerValidator {
    

    private static final String API_KEY = "testKey";
    public static List<String> filterTickers(List<String> tickers) {
        Iterator<String> iterator = tickers.iterator();
        while (iterator.hasNext()) {
            String ticker = iterator.next();
            if (!isValidTicker(ticker)) {
                iterator.remove();
            }
        }
        
        return tickers;
    }
    public static boolean isValidTicker(String ticker) {
        try {
            String urlString =
                    "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords="
                    + ticker + "&apikey=" + API_KEY;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();

            Gson gson = new Gson();
            JsonObject json = gson.fromJson(result.toString(), JsonObject.class);

            if (!json.has("bestMatches")) {
                return false;
            }

            JsonArray matches = json.getAsJsonArray("bestMatches");

            if (matches.size() == 0) {
                return false;
            }

            for (int i = 0; i < matches.size(); i++) {
                JsonObject match = matches.get(i).getAsJsonObject();
                String symbol = match.get("1. symbol").getAsString();

                if (symbol.equalsIgnoreCase(ticker)) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        List<String> tickers = new ArrayList<>();
        tickers.add("AAPL");
        tickers.add("MSFT");
        tickers.add("AKSDSAD");
        tickers.add("TSLA");
        System.err.println(filterTickers(tickers));
    }
}
