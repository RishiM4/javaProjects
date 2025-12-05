package com.stock;

import java.util.ArrayList;
import java.util.List;

public class ClearRateLimit {
    public static void main(String[] args) {
        List<String> tickers = new ArrayList<>();
        tickers.add("AAPL");
        tickers.add("MSFT");
        tickers.add("AKSDSAD");
        tickers.add("TSLA");
        TickerValidator.filterTickers(tickers);
        System.err.println("Rate limit cleared! Please wait ~5 seconds before trying again");
    }
}
