package com.restaurant.java;

import com.restaurant.java.dao.UserDao;
import com.restaurant.java.presentation.MainMenu;
import com.restaurant.java.service.IUserServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        IUserServiceImpl userService = IUserServiceImpl.getInstance();
        MainMenu.printMenu(sc,userService);
    }
}
