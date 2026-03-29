package com.restaurant.java.service;

import com.restaurant.java.entity.Categories;

import java.util.List;

public interface ICategoriesService {
    public boolean insert(Categories categories);
    public boolean update(int id, String name);
    public boolean delete(int id);
    public List<Categories> getList();
    public Categories getById(int id);
}
