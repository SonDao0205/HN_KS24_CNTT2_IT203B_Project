package com.restaurant.java.presentation;

import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.InputMethod;

import java.util.Scanner;

public class StatisticManagement {
    public static void printMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("""
                    +---------------------------------------+
                    |           Statistic Management        |
                    +---------------------------------------+
                    | 1. Doanh thu theo tháng               |
                    | 2. Món ăn bán chạy nhất               |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc,"Lựa chọn của bạn : ");
            switch (choice) {
                case 0:
                    break;
                default:
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        }while(choice != 0);
    }
}
