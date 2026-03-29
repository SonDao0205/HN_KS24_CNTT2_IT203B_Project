package com.restaurant.java.service;

import com.restaurant.java.dao.CategoriesDao;
import com.restaurant.java.dao.MenuDao;
import com.restaurant.java.entity.Categories;
import com.restaurant.java.entity.Menu_Item;

import java.util.List;

public class IMenuServiceImpl implements IMenuService {
    MenuDao menuDao = MenuDao.getInstance();
    CategoriesDao categoriesDao = CategoriesDao.getInstance();

    private static IMenuServiceImpl instance;
    private IMenuServiceImpl() {}
    public static IMenuServiceImpl getInstance() {
        if (instance == null) {
            instance = new IMenuServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean insert(Menu_Item menuItem,Categories categories) {
        if(menuItem==null || categories == null){
            throw new RuntimeException("Dữ liệu không hợp lệ!");
        }
        return menuDao.insert(menuItem,categories.getId());
    }

    @Override
    public boolean update(Menu_Item menuItem) {
        Menu_Item findMenuItem = menuDao.getById(menuItem.getId());
        if(findMenuItem == null){
            return false;
        }

        return menuDao.update(menuItem);
    }

    @Override
    public boolean delete(int id) {
        return menuDao.delete(id);
    }

    @Override
    public Menu_Item getById(int id) {
        if(id <= 0){
            throw new RuntimeException("Dữ liệu không hợp lệ!");
        }

        return menuDao.getById(id);
    }

    @Override
    public List<Menu_Item> getList() {
        return menuDao.getList();
    }
}
