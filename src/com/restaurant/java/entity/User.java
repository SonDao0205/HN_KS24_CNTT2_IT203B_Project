package com.restaurant.java.entity;

import com.restaurant.java.entity.enums.UserRoleEnum;
import com.restaurant.java.utils.InputMethod;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

public class User {
    private int id;
    private String username;
    private String password;
    private UserRoleEnum role;
    private boolean status;

    public User() {
    }

    public User(int id, String username, String password, UserRoleEnum role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = true;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoleEnum getRole() {
        return role;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void input(Scanner sc){
        setUsername(InputMethod.getString(sc,"Nhập tên tài khoản : "));
        setPassword(InputMethod.getString(sc,"Nhập mật khẩu : "));
        setStatus(true);
        setRole(UserRoleEnum.customer);
    }

}
