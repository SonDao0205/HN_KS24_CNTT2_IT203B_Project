package com.restaurant.java.service;

import com.restaurant.java.entity.User;

public interface IAuthService {
    User login(String username, String password);
    boolean register(String username, String password);
}
