package com.restaurant.java.dao;

import com.restaurant.java.entity.User;
import com.restaurant.java.entity.enums.UserRoleEnum;
import com.restaurant.java.exception.AccountLockException;
import com.restaurant.java.utils.DatabaseConnection;
import com.restaurant.java.utils.PasswordHased;
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

    public User findById(int id) {
        String sql = "select * from user where id=?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRoleEnum.valueOf(rs.getString("role")),
                        rs.getBoolean("status")
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
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
                        UserRoleEnum.valueOf(rs.getString("role")),
                        rs.getBoolean("status")
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    public User login(String username, String password) throws Exception {
        User user = findByUsername(username);

        if(user == null){
            throw new Exception("Tên tài khoản hoặc mật khẩu không chính xác!");
        }

        if(!user.isStatus()){
            throw new AccountLockException("Tài khoản đã bị khoá!");
        }

        if (!PasswordHased.checkPassword(password, user.getPassword())) {
            throw new Exception("Tài khoản hoặc mật khẩu không chính xác!");
        }
        return user;
    }

    public boolean register(String username, String password) {
        if (findByUsername(username) != null) {
            throw new RuntimeException("Tên tài khoản đã tồn tại");
        }

        String sql = "INSERT INTO Users (username, password,role) VALUES (?, ?,?)";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, PasswordHased.hashPassword(password));
            pstmt.setString(3, UserRoleEnum.customer.name());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi đăng ký!");
        }
    }
}
