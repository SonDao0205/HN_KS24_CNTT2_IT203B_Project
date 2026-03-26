package com.restaurant.java.presentation;

import com.restaurant.java.entity.User;
import com.restaurant.java.entity.enums.UserRoleEnum;
import com.restaurant.java.service.IUserServiceImpl;
import com.restaurant.java.utils.InputMethod;

import java.util.Scanner;

public class AuthenticationMenu {
    public static void printLoginMenu(Scanner sc, IUserServiceImpl userService) {
        int choice;
        System.out.println("---------------Login----------------");
        String username = InputMethod.getString(sc,"Tên đăng nhập : ");
        String password = InputMethod.getString(sc,"Mật khẩu : ");
        do {
            System.out.println("1. Đăng nhập");
            System.out.println("0. Quay lại");
            choice = InputMethod.getInt(sc,"Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    UserRoleEnum userRole = handleLogin(username,password,userService);
                    if(userRole != null){
                        System.out.println("Đăng nhập thành công!");
                        switch (userRole) {
                            // phân quyền
                            case UserRoleEnum.chef -> System.out.println("Chef");
                            case UserRoleEnum.manager ->  System.out.println("Manager");
                            case UserRoleEnum.customer ->   System.out.println("Customer");
                        }
                    }else{
                        System.out.println("Đăng nhập thất bại!");
                    }
                    return;
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
                    break;
            }
        }while(choice != 0);
    }

    public static void printRegisterMenu(Scanner sc, IUserServiceImpl userService) {
        int choice;
        System.out.println("---------------Register----------------");
        String username = InputMethod.getString(sc,"Tên đăng nhập : ");
        String password = InputMethod.getString(sc,"Mật khẩu : ");
        do {
            System.out.println("1. Đăng ký");
            System.out.println("0. Quay lại");
            choice = InputMethod.getInt(sc,"Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                        if(handleRegister(username,password,userService)){
                            System.out.println("Đăng ký thành công!");
                        }else{
                            System.out.println("Đăng ký thất bại!");
                        }
                    return;
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
                    break;
            }
        }while(choice != 0);
    }

    public static UserRoleEnum handleLogin(String username, String password, IUserServiceImpl userService) {
        return userService.login(username, password).getRole();
    }

    public static boolean handleRegister(String username, String password, IUserServiceImpl userService) {
        return userService.register(username, password);
    }
}
