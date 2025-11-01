package src;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;

public class StockFetcher {
    public static void main(String[] args) throws IOException, InterruptedException {
        String symbol = "AAPL";
        String apiKey = "14H3681ZN9Q00BM7";
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="
                     + symbol + "&interval=5min&apikey=" + apiKey;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
