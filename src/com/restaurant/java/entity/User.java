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

    public User(int id, String username, String password, UserRoleEnum role, boolean status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(int id, String username, UserRoleEnum role, boolean status) {
        this.username = username;
        this.role = role;
        this.status = status;
        this.id = id;
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

    public static void tableHeader() {
        System.out.println("+-----------------------------------------------------+");
        System.out.printf("|%-5s|%-20s|%-10s|%-15s|\n","ID", "Username","Role","Status");
        System.out.println("+-----------------------------------------------------+");
    }

    public void displayData(){
        System.out.printf("|%-5d|%-20s|%-10s|%-15s|\n",this.id, this.username,this.role,(this.status ? "Đang hoạt động" : "Ngừng hoạt động"));
        System.out.println("+-----------------------------------------------------+");
    }
}
