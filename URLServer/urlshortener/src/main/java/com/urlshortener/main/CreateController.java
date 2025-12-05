package com.urlshortener.main;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class CreateController {
    static Connection conn;
    public CreateController() {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "testdb";
        String user = "root";
        String password = "1234";

        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL!");

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);

            conn = DriverManager.getConnection(url + dbName, user, password);
            System.out.println("Connected to database: " + dbName);

            String createTable = """
                CREATE TABLE IF NOT EXISTS urls (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    uuid VARCHAR(256),
                    url TEXT(32768)
                );
            """;

            stmt = conn.createStatement();

            stmt.executeUpdate(createTable);

            System.out.println("SQL initialized!");

            
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isValidUrl(String url) {
        try {
            new URL(url).toURI(); 
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public String createMapping(String url) {
        System.err.println("TRYING TO CREATE MAPPING");
        try {
            String uuid = UUID.randomUUID().toString();
            String insert_query = "INSERT INTO urls (uuid, url) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insert_query);
            pstmt.setString(1, uuid);
            pstmt.setString(2, url);
            pstmt.executeUpdate();
            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        

    }
    @PostMapping("/create")
    public String create(@RequestParam String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        if (isValidUrl(url)) {
            String uuid = createMapping(url);
           return String.format("""
        <html>
            <head>
                <title>URL Shortener</title>
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
                    p {
                        font-size: 18px;
                        color: #333;
                    }
                    a {
                        color: #007bff;
                        text-decoration: none;
                        font-weight: bold;
                    }
                    a:hover {
                        text-decoration: underline;
                    }
                </style>
            </head>
            <body>
                <p>URL received: %s</p>
                <p>Shortened URL: <a href="http://localhost:8080/%s">http://localhost:8080/%s</a></p>
                <a href="/">Back</a>
            </body>
        </html>
        """, url, uuid, uuid);


        }
        else {
            return "Invalid URL";
        }
    }
}
