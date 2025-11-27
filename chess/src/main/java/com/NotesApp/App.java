package com.NotesApp;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App {
    
    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClient.newHttpClient();

String json = """
{
  "text": "Definition: Long-term shifts in temperature and weather patterns, mainly caused today by human activities (burning fossil fuels, deforestation, agriculture).

Key Drivers:

Greenhouse gases: CO₂, methane, nitrous oxide.

Industrialization + transportation.

Land-use changes reducing natural carbon sinks.

Major Effects:

Rising global temperatures.

Melting glaciers + sea-level rise.

More extreme weather (storms, droughts, heatwaves).

Ecosystem disruption + species extinction.

Human Impact:

Food and water insecurity.

Health risks (heat stress, disease spread).

Economic damage + climate migration.

Mitigation Strategies:

Renewable energy adoption.

Energy efficiency.

Reforestation + restoration.

Sustainable agriculture and transport.

Adaptation Strategies:

Flood defenses.

Climate-resilient crops.

Urban planning for extreme heat.

Current Concern: Warming is happening faster than predicted; international cooperation critical."
}
""";

    HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("http://localhost:8000/getKeyword"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(json))
    .build();

    HttpResponse<String> response =
    client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println(response.body());
    json = """
    {
    "text": ""
    }
    """;
    request = HttpRequest.newBuilder()
    .uri(URI.create("http://localhost:8000/embed"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString(json))
    .build();

    response =
    client.send(request, HttpResponse.BodyHandlers.ofString());

    System.out.println(response.body());
    }
}
