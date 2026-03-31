package com.restaurant.java.dao;

import com.restaurant.java.entity.Table;
import com.restaurant.java.entity.enums.TableEnum;
import com.restaurant.java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TableDao {
    private static TableDao instance;

    private TableDao() {
    }

    public static TableDao getInstance() {
        if (instance == null) {
            instance = new TableDao();
        }
        return instance;
    }

    public boolean insert(Table table) {
        String sql = "INSERT INTO Tables(number,capacity) VALUES (?,?)";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, table.getNumber());
            pstmt.setInt(2, table.getCapacity());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi thêm thực đơn!");
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "UPDATE Tables SET status = 'block'  WHERE id= ?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi xoá bàn!");
            return false;
        }
    }

    public boolean update(Table table) {
        String sql = "UPDATE Tables SET capacity = ? , number = ? WHERE id = ?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, table.getCapacity());
            pstmt.setString(2, table.getNumber());
            pstmt.setInt(3, table.getId());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật bàn!");
            return false;
        }
    }

    public Table getById(int id) {
        String sql = "SELECT * FROM Tables WHERE id = ? AND status = 'available'";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Table(
                        rs.getInt("id"),
                        rs.getString("number"),
                        rs.getInt("capacity"),
                        TableEnum.valueOf(rs.getString("status"))
                );
            }
            return null;
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu bàn!");
            return null;
        }
    }

    public boolean isNumberExists(String number) {
        String sql = "SELECT * FROM Tables WHERE number = ? ";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, number);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Lỗi tìm số bàn định danh");
            return false;
        }
    }

    public List<Table> getList() {
        String sql = "SELECT * FROM Tables WHERE status = 1";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.executeQuery();
            ResultSet rs = pstmt.executeQuery();
            List<Table> tables = new ArrayList<>();
            while (rs.next()) {
                tables.add(new Table(
                        rs.getInt("id"),
                        rs.getString("number"),
                        rs.getInt("capacity"),
                        TableEnum.valueOf(rs.getString("status"))
                ));
            }
            return tables;
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách bàn!");
            return null;
        }
    }

    public List<Table> getListTable(TableEnum tableEnum) {
        String sql = "SELECT * FROM Tables WHERE status = ?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, tableEnum.toString());
            pstmt.executeQuery();
            ResultSet rs = pstmt.executeQuery();
            List<Table> tables = new ArrayList<>();
            while (rs.next()) {
                tables.add(new Table(
                        rs.getInt("id"),
                        rs.getString("number"),
                        rs.getInt("capacity"),
                        TableEnum.valueOf(rs.getString("status"))
                ));
            }
            return tables;
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách bàn!");
            return null;
        }
    }

    public boolean updateTableStatus(int id, TableEnum tableEnum) {
        String sql = "UPDATE Tables SET status = ? WHERE id = ?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, tableEnum.toString());
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật trạng thái bàn!");
            return false;
        }
    }
}
