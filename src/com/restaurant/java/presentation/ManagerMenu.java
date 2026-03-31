package com.restaurant.java.presentation;

import com.restaurant.java.utils.Constant;
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
                    | 2. Quản lý danh mục                   |
                    | 3. Quản lý bàn ăn                     |
                    | 4. Duyệt order                        |
                    | 5. Quản lý người dùng                 |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc,"Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    MenuManagement.printMenu(sc);
                    break;
                case 2:
                    CategoriesManagement.printMenu(sc);
                    break;
                case 3:
                    TableManagement.printMenu(sc);
                    break;
                case 4:
                    OrderManagement.approveOrder(sc);
                    break;
                case 5:
                    UserManagement.printMenu(sc);
                    break;
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
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        }while(choice != 0);
    }
}
