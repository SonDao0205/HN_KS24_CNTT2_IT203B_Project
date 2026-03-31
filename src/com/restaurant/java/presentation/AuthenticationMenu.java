package com.restaurant.java.presentation;

import com.restaurant.java.entity.User;
import com.restaurant.java.entity.enums.UserRoleEnum;
import com.restaurant.java.exception.AccountLockException;
import com.restaurant.java.service.IUserServiceImpl;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.InputMethod;

import java.util.Scanner;

public class AuthenticationMenu {
    public static void printLoginMenu(Scanner sc) {
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
                    UserRoleEnum userRole = handleLogin(username,password,IUserServiceImpl.getInstance());
                    if(userRole != null){
                        System.out.println(Constant.GREEN_CODE + "Đăng nhập thành công!" + Constant.RESET_CODE);
                        switch (userRole) {
                            // phân quyền
                            case UserRoleEnum.chef -> ChefMenu.printMenu(sc);
                            case UserRoleEnum.manager ->  ManagerMenu.printMenu(sc);
                            case UserRoleEnum.customer ->   CustomerMenu.printMenu(sc);
                        }
                    }else{
                        System.out.println(Constant.RED_CODE + "Đăng nhập thất bại!" + Constant.RESET_CODE);
                    }
                    return;
                case 0:
                    break;
                default:
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        }while(choice != 0);
    }

    public static void printRegisterMenu(Scanner sc) {
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
                        if(handleRegister(username,password,IUserServiceImpl.getInstance())){
                            System.out.println(Constant.GREEN_CODE + "Đăng ký thành công!" + Constant.RESET_CODE);
                        }else{
                            System.out.println(Constant.RED_CODE + "Đăng ký thất bại!" + Constant.RESET_CODE);
                        }
                    return;
                case 0:
                    break;
                default:
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        }while(choice != 0);
    }

    public static UserRoleEnum handleLogin(String username, String password, IUserServiceImpl userService) {
        try{
            return userService.login(username, password).getRole();
        }catch(Exception e){
            return null;
        }
    }

    public static boolean handleRegister(String username, String password, IUserServiceImpl userService) {
        return userService.register(username, password);
    }
}
