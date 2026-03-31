package com.restaurant.java.presentation;

import com.restaurant.java.entity.Review;
import com.restaurant.java.service.IReviewServiceImpl;

import java.util.List;
import java.util.Scanner;

public class ReviewManagement {
    public static void getAllReviews() {
        List<Review> reviewList = IReviewServiceImpl.getInstance().getReviewList();
        if(reviewList == null){
            System.out.println("Danh sách đánh giá rỗng!");
            return;
        }
        System.out.println("Danh sách các đánh giá : ");
        Review.tableHeader();
        for (Review review : reviewList) {
            review.displayData();
        }
    }
}
