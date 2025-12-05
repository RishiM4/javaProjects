package com.urlshortener.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return """
        <html>
            <head>
                <title>Url Shortener | By Rishi Mohan</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f9;
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        justify-content: center;
                        height: 100vh;
                        margin: 0;
                    }
                    h1 {
                        color: #333;
                    }
                    form {
                        margin-top: 20px;
                        display: flex;
                        flex-direction: column;
                        gap: 10px;
                        background-color: #fff;
                        padding: 20px;
                        border-radius: 10px;
                        box-shadow: 0 2px 8px rgba(0,0,0,0.2);
                    }
                    input {
                        padding: 10px;
                        font-size: 16px;
                        border-radius: 5px;
                        border: 1px solid #ccc;
                    }
                    button {
                        padding: 10px;
                        font-size: 16px;
                        background-color: #007bff;
                        color: white;
                        border: none;
                        border-radius: 5px;
                        cursor: pointer;
                    }
                    button:hover {
                        background-color: #0056b3;
                    }
                    label {
                        font-weight: bold;
                    }
                </style>
            </head>
            <body>
                <h1>URL Shortener</h1>
                <p>By Rishi Mohan</p>
                <br>
                <p>Please enter the URL you would like shortened:</p>
                <form action="/create" method="POST">
                    <input name="url" />
                    <button type="submit">Shorten</button>
                </form>
            </body>
        </html>
       """;

    }
}
