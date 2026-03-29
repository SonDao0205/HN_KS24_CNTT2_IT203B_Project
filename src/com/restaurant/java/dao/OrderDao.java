package com.restaurant.java.dao;

import com.restaurant.java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OrderDao {
    private static OrderDao instance;

    private OrderDao() {
    }

    public static OrderDao getInstance() {
        if (instance == null) {
            instance = new OrderDao();
        }
        return instance;
    }

    public boolean createOrder(int user_id, int table_id) {
        String sql = "INSERT INTO Orders(customer_id,table_id) VALUES (?,?)";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, table_id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi tạo đơn!");
            return false;
        }
    }
}

