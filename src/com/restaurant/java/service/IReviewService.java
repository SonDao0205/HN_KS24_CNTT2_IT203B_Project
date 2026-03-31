package com.restaurant.java.service;

import com.restaurant.java.entity.Review;
import com.restaurant.java.entity.User;

import java.util.List;

public interface IReviewService {
    public boolean addReview(Review review);
    public List<Review> getReviewList();
}
