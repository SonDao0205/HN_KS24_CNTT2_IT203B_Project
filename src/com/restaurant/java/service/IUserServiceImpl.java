package com.restaurant.java.service;

import com.restaurant.java.dao.UserDao;
import com.restaurant.java.entity.User;
import com.restaurant.java.utils.UserSession;

public class IUserServiceImpl implements IUserService {
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
}
