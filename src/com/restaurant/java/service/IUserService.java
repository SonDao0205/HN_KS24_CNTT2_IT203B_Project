package com.restaurant.java.service;

import com.restaurant.java.entity.User;

public interface IUserService {
    User login(String username, String password);
    boolean register(String username, String password);
}
