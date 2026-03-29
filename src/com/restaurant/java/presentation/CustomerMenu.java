package com.restaurant.java.presentation;

import com.restaurant.java.entity.Menu_Item;
import com.restaurant.java.entity.Table;
import com.restaurant.java.service.IMenuServiceImpl;
import com.restaurant.java.service.IOrderService;
import com.restaurant.java.service.IOrderServiceImpl;
import com.restaurant.java.service.ITableServiceImpl;
import com.restaurant.java.utils.InputMethod;
import com.restaurant.java.utils.UserSession;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    public static void printMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("""
                    +---------------------------------------+
                    |           Customer Ordering           |
                    +---------------------------------------+
                    | 1. Xem menu                           |
                    | 2. Chọn bàn trống                     |
                    | 3. Chọn món                           |
                    | 4. Xem danh sách order                |
                    | 5. Huỷ món                            |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    getMenu();
                    break;
                case 2:
                    pickTable(sc);
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
                    System.out.println("Lựa chọn không phù hợp!");
                    break;
            }
        } while (choice != 0);
    }

    public static void getMenu() {
        List<Menu_Item> menuList = IMenuServiceImpl.getInstance().getList();
        if (menuList == null || menuList.isEmpty()) {
            System.out.println("Danh sách thực đơn rỗng!");
            return;
        }
        for (Menu_Item menuItem : menuList) {
            System.out.printf("|Name : %s | Price : %.2f |\n", menuItem.getName(), menuItem.getPrice());
        }
    }

    public static void pickTable(Scanner sc) {
        List<Table> tableList = ITableServiceImpl.getInstance().getTableAvailable();
        if (tableList == null || tableList.isEmpty()) {
            System.out.println("Danh sách bàn trống rỗng!");
            return;
        }

        for (Table table : tableList) {
            System.out.printf("|Id : %d |Number : %s | Capacity : %d |\n", table.getId(),table.getNumber(), table.getCapacity());
        }

        int choice;
        do {
            System.out.println("Chức năng : ");
            System.out.println("1. Chọn bàn");
            System.out.println("0. Quay lại");
            choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    int table_id;
                    while (true) {
                        table_id = InputMethod.getInt(sc,"Nhập id của bàn muốn chọn : ");
                        if(ITableServiceImpl.getInstance().getById(table_id) == null){
                            System.out.println("Id bàn không phù hợp!");
                        }else{
                            break;
                        }
                    }

                    if(ITableServiceImpl.getInstance().bookedTable(table_id)){
                        if(IOrderServiceImpl.getInstance().createOrder(UserSession.getInstance().getCurrentUser().getId(), table_id)){
                            System.out.println("Đặt bàn thành công!");
                        }
                    }else{
                        System.out.println("Đặt bàn thất bại!");
                    }
                    return;
                case 0:
                    return;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
                    break;

            }
        } while (true);
    }
}
