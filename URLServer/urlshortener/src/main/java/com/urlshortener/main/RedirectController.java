package com.urlshortener.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedirectController {
    Connection conn;
    public RedirectController() {
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
    public String findURLFromUUID(String uuid) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT url FROM urls WHERE uuid = ?");
            pstmt.setString(1, uuid);  
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("url");     
            }
        } catch (Exception e) {
        }
        return null;
    }

    @GetMapping("/{shortcode}")
    public ResponseEntity<?> redirect(@PathVariable String shortcode) {
        
        System.out.println(findURLFromUUID(shortcode));
        String url = "/404";
        String temp = findURLFromUUID(shortcode);
        if (temp != null) {
            url = temp;
        }
        return ResponseEntity.status(308)
                .header("Location", url)
                .build();
    }
}
