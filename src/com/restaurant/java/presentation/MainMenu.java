package com.restaurant.java.presentation;

import com.restaurant.java.service.IUserServiceImpl;
import com.restaurant.java.utils.InputMethod;

import java.util.Scanner;

public class MainMenu {
    public static void printMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("""
                    +---------------------------------------+
                    |           PRJ - Restaurant            |
                    +---------------------------------------+
                    | 1. Đăng nhập                          |
                    | 2. Đăng ký                            |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc,"Lựa chọn của bạn : ");
            switch (choice) {
                case 1 -> AuthenticationMenu.printLoginMenu(sc);
                case 2 -> AuthenticationMenu.printRegisterMenu(sc);
                default -> System.out.println("Lựa chọn không phù hợp!");
            }
        }while(choice != 0);
    }
}
