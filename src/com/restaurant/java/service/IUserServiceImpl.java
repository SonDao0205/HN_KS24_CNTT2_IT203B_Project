package com.restaurant.java.service;

import com.restaurant.java.dao.UserDao;
import com.restaurant.java.entity.User;
import com.restaurant.java.utils.Constant;
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
            if(!currentUser.isStatus()){
                System.out.println(Constant.YELLOW_CODE + "Tài khoản này đã bị khoá!" + Constant.RESET_CODE);
                return null;
            }
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
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }
        if(getByUsername(user.getUsername()) != null){
            System.out.println(Constant.YELLOW_CODE + "Tên tài khoản đã tồn tại!" + Constant.RESET_CODE);
            return false;
        }
        return userDao.insert(user);
    }

    @Override
    public boolean update(User user) {
        if(user == null){
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }
        return userDao.update(user);
    }

    @Override
    public boolean delete(int id) {
        if(id <= 0){
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }
        if(getById(id) == null){
            System.out.println(Constant.INVALID_ID_FOUND);

            return false;
        }
        return userDao.delete(id);
    }

    @Override
    public User getById(int id) {
        if(id <= 0){
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return null;
        }
        return userDao.findById(id);
    }

    @Override
    public User getByUsername(String username) {
        if(username == null || username.isEmpty()){
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return null;
        }
        return userDao.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        List<User> userList = userDao.getList();
        if(userList == null || userList.isEmpty()){
            System.out.println(Constant.YELLOW_CODE + "Danh sách tài khoản rỗng!" + Constant.RESET_CODE);
            return null;
        }
        return userList;
    }

    @Override
    public boolean blockUser(User user) {
        if(user == null){
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }
        if(!user.isStatus()){
            System.out.println(Constant.YELLOW_CODE + "Tài khoản này đã bị khoá!" + Constant.RESET_CODE);
            return false;
        }
        return userDao.blockUser(user);
    }
}
