package com.restaurant.java.service;


import com.restaurant.java.entity.User;

import java.util.List;

public interface IUserService {
    public boolean insert(User user);
    public boolean update(User user);
    public boolean delete(int id);
    public User getById(int id);
    public User getByUsername(String username);
    public List<User> getAll();
}
