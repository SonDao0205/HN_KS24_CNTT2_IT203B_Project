package com.restaurant.java.dao;

import com.restaurant.java.entity.Categories;
import com.restaurant.java.entity.Menu_Item;
import com.restaurant.java.utils.DatabaseConnection;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuDao {
    private static MenuDao instance;

    private MenuDao() {
    }

    public static MenuDao getInstance() {
        if (instance == null) {
            instance = new MenuDao();
        }
        return instance;
    }

    public boolean insert(Menu_Item menu, int category_id) {
        String sql = "INSERT INTO Menu_Items(categories_id,name,price) VALUES (?,?,?)";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, category_id);
            pstmt.setString(2, menu.getName());
            pstmt.setDouble(3, menu.getPrice());

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi thêm thực đơn!");
            return false;
        }
    }

    public boolean update(Menu_Item menu_item) {
        String sql = "UPDATE Menu_Items SET categories_id = ?, name = ?, price = ?  WHERE id=?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, menu_item.getCategories().getId());
            pstmt.setString(2, menu_item.getName());
            pstmt.setDouble(3, menu_item.getPrice());
            pstmt.setInt(4, menu_item.getId());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật thực đơn!");
            return false;
        }
    }

    public boolean delete(int menu_id) {
        String sql = "UPDATE Menu_Items SET status = 0  WHERE id=?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, menu_id);

            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật thực đơn!");
            return false;

        }
    }

    public Menu_Item getById(int id) {
        String sql = """
                SELECT m.id, m.name, m.price, m.status, c.id as categories_id, c.name as categories_name, c.status as categories_status
                FROM Project.Menu_Items m
                JOIN Categories c ON m.categories_id = c.id
                WHERE m.id = ?;
                """;

        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Categories categories = new Categories(
                        rs.getInt("categories_id"),
                        rs.getString("categories_name"),
                        rs.getBoolean("categories_status")
                );

                return new Menu_Item(
                        rs.getInt("id"),
                        categories,
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getBoolean("status")
                );

            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Lấy thực đơn thất bại!");
            return null;
        }
    }

    public List<Menu_Item> getList() {
        String sql = """
                SELECT m.id, m.name, m.price, m.status, c.id as categories_id, c.name as categories_name, c.status as categories_status
                FROM Project.Menu_Items m
                JOIN Categories c ON m.categories_id = c.id
                WHERE m.status = 1 AND c.status = 1;""";

        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            List<Menu_Item> list = new ArrayList<>();
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Categories categories = new Categories(
                        rs.getInt("categories_id"),
                        rs.getString("categories_name"),
                        rs.getBoolean("categories_status")
                );

                list.add(new Menu_Item(
                        rs.getInt("id"),
                        categories,
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getBoolean("status")
                ));
            }
            return list;
        } catch (Exception e) {
            System.out.println("Lấy danh sách thực đơn thất bại!");
            return null;
        }
    }
}
