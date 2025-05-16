package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:boutique.db";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Création des tables
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS categorys (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL," +
                "description TEXT NOT NULL" +
                ")"
            );
            
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS produits (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom TEXT NOT NULL," +
                "quantite INTEGER," +
                "prix REAL NOT NULL," + // Ajout de la virgule ici
                "category_id INTEGER NOT NULL," +
                "FOREIGN KEY (category_id) REFERENCES categorys(id)" + // PAS de virgule ici
                ")"
            );

            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS users ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                " email TEXT NOT NULL UNIQUE,"+
                "password TEXT NOT NULL"+
                ")"
            );

            conn.createStatement().execute(
                "INSERT OR REPLACE INTO users (email, password) VALUES ('admin@gmail.com', 'admin123')");
           
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}