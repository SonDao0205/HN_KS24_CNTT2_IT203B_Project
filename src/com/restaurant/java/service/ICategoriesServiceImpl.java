package com.restaurant.java.service;

import com.restaurant.java.dao.CategoriesDao;
import com.restaurant.java.entity.Categories;

import java.util.List;

public class ICategoriesServiceImpl implements ICategoriesService {
    CategoriesDao categoriesDao = CategoriesDao.getInstance();

    private static ICategoriesServiceImpl instance;

    private ICategoriesServiceImpl() {}

    public static ICategoriesServiceImpl getInstance() {
        if (instance == null) {
            instance = new ICategoriesServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean insert(Categories categories) {
        if(categories==null){
            throw new RuntimeException("Dữ liệu không hợp lệ!");
        }
        return categoriesDao.insert(categories);
    }

    @Override
    public boolean update(int id, String name) {
        if(id <= 0 || name.isEmpty()){
            throw new RuntimeException("Dữ liệu không hợp lệ!");
        }
        return categoriesDao.update(id,name);
    }

    @Override
    public boolean delete(int id) {
        if(id <= 0){
            throw new RuntimeException("Dữ liệu không hợp lệ!");
        }
        return categoriesDao.delete(id);
    }

    @Override
    public List<Categories> getList() {
        List<Categories> categoriesList = categoriesDao.getList();
        if(categoriesList.isEmpty()){
            System.out.println("Danh sách danh mục trống!");
            return null;
        }
        return categoriesList;
    }

    @Override
    public Categories getById(int id) {
        if(id <= 0){
            throw new RuntimeException("Dữ liệu không hợp lệ!");
        }
        return categoriesDao.getById(id);
    }
}
