package com.restaurant.java.service;

import com.restaurant.java.dao.ReviewDao;
import com.restaurant.java.entity.Review;
import com.restaurant.java.entity.User;
import com.restaurant.java.utils.Constant;

import java.util.List;

public class IReviewServiceImpl implements IReviewService {
    private static IReviewServiceImpl instance;
    private IReviewServiceImpl(){};
    public static IReviewServiceImpl getInstance() {
        if (instance == null) {
            instance = new IReviewServiceImpl();
        }
        return instance;
    }

    ReviewDao reviewDao = ReviewDao.getInstance();
    @Override
    public boolean addReview(Review review) {
        if(review == null) {
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }
        return reviewDao.addReview(review);
    }

    @Override
    public List<Review> getReviewList() {
        List<Review> reviewList = reviewDao.getReviewList();
        if(reviewList == null || reviewList.isEmpty()) {
            return null;
        }
        return reviewList;
    }
}
