package DAO;


import models.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private Connection conn;

    public ProductDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Produit> searchProductsByName(String name) throws SQLException {
        List<Produit> products = new ArrayList<>();
        String sql = "SELECT * FROM produits WHERE nom LIKE ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Produit product = new Produit(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getDouble("prix"),
                    rs.getInt("quantite"),
                    rs.getInt("category_id")
                );
                products.add(product);
            }
        }
        
        return products;
    }

    public void insertProduct(Produit product) throws SQLException {
        String sql = "INSERT INTO produits (nom, prix, quantite, category_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getNom());
            stmt.setInt(2, product.getQuantite());
            stmt.setDouble(3, product.getPrix());
            stmt.setInt(4, product.getCategory());
            stmt.executeUpdate();
        }
    }

    public List<Produit> getAllProducts() throws SQLException {
        List<Produit> products = new ArrayList<>();
        String sql = "SELECT * FROM produits";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Produit p = new Produit(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getDouble("prix"),
                    rs.getInt("quantite"),                   
                    rs.getInt("category_id")
                );
                products.add(p);
            }
        }
        return products;
    }

    public void deleteProduct(int id) throws SQLException {
        String sql = "DELETE FROM produits WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean updateProduct(Produit produit) throws SQLException {
        String sql = "UPDATE produits SET nom = ?, quantite = ?, prix = ?, category_id = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, produit.getNom());
            pstmt.setInt(2, produit.getQuantite());
            pstmt.setDouble(3, produit.getPrix());
            pstmt.setInt(4, produit.getCategory());
            pstmt.setInt(5, produit.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
