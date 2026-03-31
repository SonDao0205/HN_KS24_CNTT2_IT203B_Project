package com.restaurant.java.dao;

import com.restaurant.java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StatisticDao {
    private static StatisticDao instance;

    private StatisticDao() {
    }

    public static StatisticDao getInstance() {
        if (instance == null) {
            instance = new StatisticDao();
        }
        return instance;
    }


    public double revenueByMonth(int month, int year) {
        String sql = """
                SELECT SUM(total_amount) AS total_revenue
                FROM Orders
                WHERE status = 'paid'\s
                  AND MONTH(checkout_at) = ?
                  AND YEAR(checkout_at) = ? """;
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_revenue");
            }
            return 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi tính doanh thu theo tháng!");
            return 0;
        }
    }

    public void bestSeller() {
        String sql = """
            SELECT 
                m.name AS m_name, 
                SUM(o.quantity) AS total_sell
            FROM Order_Items o
            JOIN Menu_Items m ON o.menu_item = m.id
            WHERE o.status NOT IN ('cancel', 'waiting', 'pending')
            GROUP BY m.id, m.name
            ORDER BY total_sell DESC
            LIMIT 3 """;

        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\n----------- TOP 3 Best Seller -----------");
            System.out.printf("| %-25s | %-10s |\n", "Name", "Quantity");
            System.out.println("------------------------------------------");

            while (rs.next()) {
                String name = rs.getString("m_name");
                int totalQty = rs.getInt("total_sell");
                System.out.printf("| %-25s | %-10d |\n", name, totalQty);
            }
            System.out.println("------------------------------------------");

        } catch (Exception e) {
            System.out.println("Lỗi khi thống kê món bán chạy");
        }
    }
}
