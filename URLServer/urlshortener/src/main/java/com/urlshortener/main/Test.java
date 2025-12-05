package com.urlshortener.main;

import java.sql.*;

public class Test {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "testdb";
        String user = "root";
        String password = "1234";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL!");

            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            System.out.println("Database created or already exists.");

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
            System.out.println("Table created or already exists.");

            String insert = "INSERT INTO urls (uuid, url) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insert);
            pstmt.setString(1, "001");
            pstmt.setString(2, "alice@example.com");
            pstmt.executeUpdate();
            System.out.println("User inserted!");

            

            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
