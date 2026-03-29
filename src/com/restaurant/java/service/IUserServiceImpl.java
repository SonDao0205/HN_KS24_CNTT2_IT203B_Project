package com.restaurant.java.service;

import com.restaurant.java.dao.UserDao;
import com.restaurant.java.entity.User;
import com.restaurant.java.utils.UserSession;

import java.util.List;

public class IUserServiceImpl implements IAuthService, IUserService {
    UserDao userDao = UserDao.getInstance();
    UserSession userSession = UserSession.getInstance();
    private static IUserServiceImpl instance = new IUserServiceImpl();

    private IUserServiceImpl() {}

    public static IUserServiceImpl getInstance() {
        if (instance == null) {
            instance = new IUserServiceImpl();
        }
        return instance;
    }


    @Override
    public User login(String username, String password) {
        if(username.isEmpty() || password.isEmpty()){
            return null;
        }
        try{
            User currentUser = userDao.login(username, password);
            if (currentUser != null) {
                userSession.login(currentUser);
                return currentUser;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean register(String username, String password) {
        if(username.isEmpty() || password.isEmpty()){
            return false;
        }
        try{
            return userDao.register(username, password);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean insert(User user) {
        if(user == null){
            System.out.println("Dữ liệu không hợp lệ!");
            return false;
        }
        if(getByUsername(user.getUsername()) != null){
            System.out.println("Tên tài khoản đã tồn tại!");
            return false;
        }
        return userDao.insert(user);
    }

    @Override
    public boolean update(User user) {
        if(user == null){
            System.out.println("Dữ liệu không hợp lệ!");
            return false;
        }
        return userDao.update(user);
    }

    @Override
    public boolean delete(int id) {
        if(id <= 0){
            System.out.println("Dữ liệu không hợp lệ!");
            return false;
        }
        if(getById(id) == null){
            System.out.println("Id không hợp lệ!");
            return false;
        }
        return userDao.delete(id);
    }

    @Override
    public User getById(int id) {
        if(id <= 0){
            System.out.println("Dữ liệu không hợp lệ!");
            return null;
        }
        return userDao.findById(id);
    }

    @Override
    public User getByUsername(String username) {
        if(username == null || username.isEmpty()){
            System.out.println("Dữ liệu không hợp lệ!");
            return null;
        }
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        List<User> userList = userDao.getList();
        if(userList == null || userList.isEmpty()){
            System.out.println("Danh sách tài khoản rỗng!");
            return null;
        }
        return userList;
    }

    public boolean bookTable(int user_id, int table_id){
        if(user_id <= 0 || table_id <= 0){
            System.out.println("Dữ liệu không hợp lệ!");
            return false;
        }
        return false;
    }
}
