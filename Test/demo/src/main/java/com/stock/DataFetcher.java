package com.stock;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DataFetcher {

    
    public static void main(String[] args)  {
        fetchData("AAPL");
    }
    public static HashMap<String,String> fetchData(String symbol) {
        HashMap<String,String> result = new HashMap<>();
        result.put("Symbol", symbol);
        List<String> fields = List.of("DividendYield","DividendPerShare","Name","Sector","MarketCapitalization","PERatio");
        extractData(get(assembleURL("https://www.alphavantage.co/query?function=OVERVIEW", symbol)).body(),fields, result);
        result.put("Cash",extractCashOnHand(get(assembleURL("https://www.alphavantage.co/query?function=BALANCE_SHEET", symbol)).body()));
        result.put("Price", getCurrentPrice(get(assembleURL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE", symbol)).body()));
        System.err.println(result);
        return result;
    }
    public static String extractData(String response, List<String> fields, HashMap<String,String> result) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        for(String field:fields) {
            if (jsonObject.has(field)) {
                result.put(field, jsonObject.get(field).getAsString());              
            }
        }
        return null;

        
    }
    public static String extractCashOnHand(String response) {
        try {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            JsonArray annualReports = jsonObject.getAsJsonArray("annualReports");
            JsonObject latestReport = annualReports.get(0).getAsJsonObject();
            return latestReport.get("cashAndCashEquivalentsAtCarryingValue").getAsString();
        } catch (Exception e) {
            return "null";
        }
        
    }
    public static String getCurrentPrice(String response) {
         Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

        if (jsonObject.has("Global Quote")) {
            JsonObject quote = jsonObject.getAsJsonObject("Global Quote");
            if (quote.has("05. price")) {
                return quote.get("05. price").getAsString();
            }
        }
        return null;
    }
    public static HttpResponse<String> get(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }
    public static String assembleURL(String url, String ticker) {
        return url+"&symbol="+ ticker + "&apikey=" + UUID.randomUUID();
    }
    
    
    

    
}
