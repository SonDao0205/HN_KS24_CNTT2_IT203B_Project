package com.restaurant.java.dao;

import com.restaurant.java.entity.Categories;
import com.restaurant.java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDao {
    private static CategoriesDao instance;

    private CategoriesDao() {
    }

    public static CategoriesDao getInstance() {
        if (instance == null) {
            instance = new CategoriesDao();
        }
        return instance;
    }


    public Categories getById(int id) {
        String sql = "select * from categories where id = ?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Categories(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBoolean("status")
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh mục!");
            return null;
        }
    }

    public boolean insert(Categories categories) {
        String sql = "INSERT INTO Categories (name) VALUES (?)";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, categories.getName());
            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Lỗi thêm danh mục!");
            return false;
        }
    }

    public boolean update(int id, String name) {
        String sql = "UPDATE Categories SET name=? WHERE id=?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, name);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật danh mục!");
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "UPDATE Categories SET status= 0 WHERE id=?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi xoá danh mục!");
            return false;
        }
    }

    public List<Categories> getList() {
        String sql = "SELECT * FROM Categories WHERE status = 1";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            ResultSet rs = pstmt.executeQuery();
            List<Categories> categoriesList = new ArrayList<>();
            while(rs.next()) {
                categoriesList.add(new Categories(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBoolean("status")
                ));
            }
            return categoriesList;
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách danh mục!");
            return null;
        }
    }
}
