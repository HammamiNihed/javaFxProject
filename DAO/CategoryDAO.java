package DAO;


import models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }

    public void insertProduct(Category category) throws SQLException {
        String sql = "INSERT INTO categorys (nom, description) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getNom());
            stmt.setString(2, category.getDescription());
            
            stmt.executeUpdate();
        }
    }

    public List<Category> getAllCategorys() throws SQLException {
        List<Category> categorys = new ArrayList<>();
        String sql = "SELECT * FROM categorys";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Category p = new Category(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("description")
                );
                categorys.add(p);
            }
        }
        return categorys;
    }

    public void deleteCategory(int id) throws SQLException {
        String sql = "DELETE FROM categorys WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean updateCategory(Category category) throws SQLException {
        String sql = "UPDATE categorys SET nom = ?, description = ? WHERE id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category.getNom());
            pstmt.setString(2, category.getDescription());
            pstmt.setInt(3, category.getId());
          
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
