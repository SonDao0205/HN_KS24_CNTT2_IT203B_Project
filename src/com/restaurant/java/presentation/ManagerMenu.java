package com.restaurant.java.presentation;

import com.restaurant.java.utils.InputMethod;
import com.restaurant.java.utils.UserSession;

import java.util.Scanner;

public class ManagerMenu {
    public static void printMenu(Scanner sc){
        int choice;
        do {
            System.out.println("""
                    +---------------------------------------+
                    |          Restaurant Management        |
                    +---------------------------------------+
                    | 1. Quản lý menu                       |
                    | 2. Quản lý bàn ăn                     |
                    | 3. Quản lý người dùng                 |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc,"Lựa chọn của bạn : ");
            switch (choice) {
                case 0:
                    System.out.println("Bạn có chắc chắn muốn đăng xuất : ");
                    System.out.println("1. Xác nhận");
                    System.out.println("2. Huỷ");
                    int subChoice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
                    switch (subChoice) {
                        case 1 -> UserSession.getInstance().logout();
                        case 2 -> choice = -1;
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
                    break;
            }
        }while(choice != 0);
    }
}
