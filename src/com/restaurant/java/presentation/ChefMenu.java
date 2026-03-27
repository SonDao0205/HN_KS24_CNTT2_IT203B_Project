package com.restaurant.java.presentation;

import com.restaurant.java.utils.InputMethod;
import com.restaurant.java.utils.UserSession;

import java.util.Scanner;

public class ChefMenu {
    public static void printMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("""
                    +---------------------------------------+
                    |              Chef Menu                |
                    +---------------------------------------+
                    | 1. Xem danh sách các món đang chờ     |
                    | 2. Cập nhật trạng thái món ăn         |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
            switch (choice) {
                case 0:
                    System.out.println("Bạn có chắc chắn muốn đăng xuất : ");
                    System.out.println("1. Xác nhận");
                    System.out.println("2. Huỷ");
                    int subChoice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
                    switch (subChoice) {
                        case 1:
                            UserSession.getInstance().logout();
                            return;
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
                    break;
            }
        } while (choice != 0);
    }
}
