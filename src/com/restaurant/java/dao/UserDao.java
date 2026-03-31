package com.restaurant.java.dao;

import com.restaurant.java.entity.User;
import com.restaurant.java.entity.enums.UserRoleEnum;
import com.restaurant.java.exception.AccountLockException;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.DatabaseConnection;
import com.restaurant.java.utils.PasswordHased;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "select * from Users where id=?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        UserRoleEnum.valueOf(rs.getString("role")),
                        rs.getBoolean("status")
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User login(String username, String password) throws Exception {
        User user = findByUsername(username);

        if (user == null) {
            throw new Exception(Constant.RED_CODE + "Tên tài khoản hoặc mật khẩu không chính xác!" + Constant.RESET_CODE);

        }

        if (!user.isStatus()) {
            throw new AccountLockException(Constant.RED_CODE + "Tài khoản đã bị khoá!" + Constant.RESET_CODE);

        }

        if (!PasswordHased.checkPassword(password, user.getPassword())) {
            throw new Exception(Constant.RED_CODE + "Tài khoản hoặc mật khẩu không chính xác!" + Constant.RESET_CODE);
        }
        return user;
    }

    public boolean register(String username, String password) {
        if (findByUsername(username) != null) {
            throw new RuntimeException(Constant.RED_CODE + "Tên tài khoản đã tồn tại" + Constant.RESET_CODE);
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

    public boolean insert(User user) {
        String sql = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, PasswordHased.hashPassword(user.getPassword()));
            pstmt.setString(3, user.getRole().name());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm tài khoản");
            return false;
        }
    }

    public boolean update(User user) {
        String sql = "UPDATE Users SET username = ? , role = ? WHERE id = ?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getRole().name());
            pstmt.setInt(3, user.getId());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm tài khoản");
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "UPDATE Users SET status = 0 WHERE id = ?";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm tài khoản");
            return false;
        }
    }

    public List<User> getList() {
        String sql = "SELECT * FROM Users";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            ResultSet rs = pstmt.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        UserRoleEnum.valueOf(rs.getString("role")),
                        rs.getBoolean("status")
                ));
            }
            return users;
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách tài khoản!");
            return null;
        }
    }

    public boolean blockUser(User user) {
        String sql = """
                UPDATE Users
                SET status = 0
                WHERE id = ? """;
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setInt(1, user.getId());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi khi khoá tài khoản!");
            return false;
        }
    }
}
