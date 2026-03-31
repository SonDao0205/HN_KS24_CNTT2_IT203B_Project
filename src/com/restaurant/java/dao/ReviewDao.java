package com.restaurant.java.dao;

import com.restaurant.java.entity.Review;
import com.restaurant.java.entity.User;
import com.restaurant.java.entity.enums.UserRoleEnum;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReviewDao {
    private static ReviewDao instance;

    private ReviewDao() {
    }

    public static ReviewDao getInstance() {
        if (instance == null) {
            instance = new ReviewDao();
        }
        return instance;
    }

    public boolean addReview(Review review) {
        String sql = "INSERT INTO Reviews(content,user_id,star) VALUES (?,?,?)";
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, review.getContent());
            pstmt.setInt(2, review.getUser().getId());
            pstmt.setInt(3, review.getStar());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(Constant.RED_CODE + "Lỗi khi thêm đánh giá!" + Constant.RESET_CODE);
            return false;
        }
    }

    public List<Review> getReviewList() {
        String sql = """
                SELECT\s
                r.id as r_id, r.content as r_content, r.star as r_star,
                u.id as u_id, u.username as u_username, u.role as u_role, u.status as u_status
                FROM Reviews r
                JOIN Users u ON u.id = r.user_id
                """;
        try (
                Connection conn = DatabaseConnection.openConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.executeQuery();
            ResultSet rs = pstmt.getResultSet();
            List<Review> reviewList = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("u_id"),
                        rs.getString("u_username"),
                        UserRoleEnum.valueOf(rs.getString("u_role")),
                        rs.getBoolean("u_status")
                );

                reviewList.add(new Review(
                        rs.getInt("r_id"),
                        rs.getString("r_content"),
                        user,
                        rs.getInt("r_star")
                ));
            }
            return  reviewList;
        } catch (Exception e) {
            System.out.println(Constant.RED_CODE + "Lỗi khi lấy danh sách review!" + Constant.RESET_CODE);
            return null;
        }
    }
}
