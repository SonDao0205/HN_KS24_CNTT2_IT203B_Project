package com.restaurant.java.service;

import com.restaurant.java.entity.Categories;
import com.restaurant.java.entity.Menu_Item;

import java.util.List;

public interface IMenuService {
    public boolean insert(Menu_Item menuItem, Categories categories);
    public boolean update(Menu_Item menuItem);
    public boolean delete(int id);
    public Menu_Item getById(int id);
    public List<Menu_Item> getList();
}
