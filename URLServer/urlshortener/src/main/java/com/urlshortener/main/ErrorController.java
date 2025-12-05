package com.urlshortener.main;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {

    @GetMapping("/404")
    public String error() {
        return """
                <html>
                    <head><title>Url Shortener | By Rishi Mohan</title></head>
                    <body>
                        <h1>ERROR 404:</h1>
                        <p>Page Not Found</p>
                    </body>
                    

                </html>
               """;
    }
}
