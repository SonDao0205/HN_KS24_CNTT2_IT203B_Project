package com.restaurant.java.dao;

import com.restaurant.java.entity.User;
import com.restaurant.java.entity.enums.UserRoleEnum;
import com.restaurant.java.utils.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {
    private static UserDao instance;

    private UserDao() {
    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRoleEnum.valueOf(rs.getString("role"))
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đăng nhập!");
        }
        return null;
    }

    public User login(String username, String password) {
        User user = findByUsername(username);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean register(String username, String password) {
        if (findByUsername(username) != null) {
            throw new RuntimeException("Tên tài khoản đã tồn tại");
        }

        String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đăng ký!");
        }
    }
}
