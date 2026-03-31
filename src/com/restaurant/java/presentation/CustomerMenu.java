package com.restaurant.java.presentation;

import com.restaurant.java.entity.Menu_Item;
import com.restaurant.java.entity.Order;
import com.restaurant.java.entity.Order_Item;
import com.restaurant.java.entity.Table;
import com.restaurant.java.service.IMenuServiceImpl;
import com.restaurant.java.service.IOrderService;
import com.restaurant.java.service.IOrderServiceImpl;
import com.restaurant.java.service.ITableServiceImpl;
import com.restaurant.java.utils.InputMethod;
import com.restaurant.java.utils.UserSession;

import java.util.ArrayList;
import java.util.Iterator;
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
                    | 4. Xem danh sách món đã gọi           |
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
                case 3:
                    pickMenu(sc);
                    break;
                case 4:
                    printOrderItemsByUser(sc);
                    break;
                case 5:
                    cancelItemOrder(sc);
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
        System.out.println("Danh sách thực đơn : ");
        for (Menu_Item menuItem : menuList) {
            System.out.printf("| Id : %d | Name : %s | Price : %.2f |\n", menuItem.getId(), menuItem.getName(), menuItem.getPrice());
        }
    }

    public static void pickTable(Scanner sc) {
        List<Table> tableList = ITableServiceImpl.getInstance().getTableAvailable();
        if (tableList == null || tableList.isEmpty()) {
            System.out.println("Danh sách bàn trống rỗng!");
            return;
        }

        for (Table table : tableList) {
            System.out.printf("|Id : %d |Number : %s | Capacity : %d |\n", table.getId(), table.getNumber(), table.getCapacity());
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
                        table_id = InputMethod.getInt(sc, "Nhập id của bàn muốn chọn : ");
                        if (ITableServiceImpl.getInstance().getById(table_id) == null) {
                            System.out.println("Id bàn không phù hợp!");
                        } else {
                            break;
                        }
                    }
                    if (ITableServiceImpl.getInstance().bookedTable(table_id)) {
                        if (IOrderServiceImpl.getInstance().createOrder(UserSession.getInstance().getCurrentUser().getId(), table_id)) {
                            System.out.println("Đặt bàn thành công!");
                        }
                    } else {
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

    public static void pickMenu(Scanner sc) {
        if (!printListTableByUser()) {
            return;
        }
        int table_id = InputMethod.getInt(sc, "Nhập id của bàn muốn gọi món : ");
        Order pickOrder = IOrderServiceImpl.getInstance().getOrder(UserSession.getInstance().getCurrentUser().getId(), table_id);
        if (pickOrder == null) {
            System.out.println("Id bàn không hợp lệ!");
            return;
        }
        int item_id;
        int choice;
        List<Menu_Item> menuItems = new ArrayList<>();
        int quantity;
        String note = "";
        do {
            getMenu();
            System.out.println("0. Quay lại");
            item_id = InputMethod.getInt(sc, "Nhập id món muốn thêm : ");
            if (item_id == 0) {
                return;
            }
            Menu_Item item = IMenuServiceImpl.getInstance().getById(item_id);
            if (item == null) {
                System.out.println("Id món không hợp lệ!");
            } else {
                quantity = InputMethod.getInt(sc, "Nhập số lượng : ");
                if (quantity <= 0) {
                    System.out.println("Số lượng không hợp lệ!");
                    return;
                }
                System.out.print("Ghi chú cho món : ");
                note = sc.nextLine();
                menuItems.add(item);
                System.out.println("Thêm món vào giỏ hàng thành công!");
                System.out.println("Bạn có muốn tiếp tục đặt món : ");
                System.out.println("1. Tiếp tục đặt món");
                System.out.println("2. Huỷ các món đã đặt trong giỏ hàng");
                System.out.println("3. Xác nhận đặt món");
                choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
                switch (choice) {
                    case 1:
                        break;
                    case 2:
                        menuItems = handleCancelItemOrder(sc, menuItems, quantity);
                        break;
                    case 3:
                        if (!handleCompleteOrder(menuItems, pickOrder, quantity, note)) {
                            System.out.println("Thêm món thất bại!");
                        } else {
                            System.out.println("Thêm món thành công!");
                        }
                        return;
                    default:
                        System.out.println("Lựa chọn không phù hợp!");
                        break;
                }
            }

        } while (true);
    }

    public static List<Menu_Item> handleCancelItemOrder(Scanner sc, List<Menu_Item> menuItems, int quantity) {
        if (menuItems == null || menuItems.isEmpty()) {
            System.out.println("Giỏ hàng rỗng!");
            return null;
        }
        System.out.println("Danh sách món trong giỏ hàng : ");
        for (Menu_Item menuItem : menuItems) {
            System.out.printf("|Id : %d |Name : %s | Price : %.2f | Quantity : %d |\n", menuItem.getId(), menuItem.getName(), menuItem.getPrice(), quantity);
        }
        int choice = 0;
        while (choice != 2) {
            int item_id = InputMethod.getInt(sc, "Nhập id món muốn huỷ : ");
            boolean exist = menuItems.stream().anyMatch(item -> item.getId() == item_id);
            if (exist) {
                System.out.println("Bạn có chắc chắn muốn huỷ món : ");
                System.out.println("1. Huỷ");
                System.out.println("2. Quay lại");
                choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
                switch (choice) {
                    case 1:
                        menuItems.removeIf(item -> item.getId() == item_id);
                        System.out.println("Huỷ món khỏi giỏ hàng thành công!");
                        return menuItems;
                    case 2:
                        break;
                    default:
                        System.out.println("Lựa chọn không phù hợp!");
                        break;
                }
            } else {
                System.out.println("Id không hợp lệ!");
            }
        }
        return menuItems;

    }

    public static boolean handleCompleteOrder(List<Menu_Item> menuItems, Order order, int quantity, String note) {
        for (Menu_Item menuItem : menuItems) {
            if (menuItem != null) {
                if (!IOrderServiceImpl.getInstance().addItemOrder(order.getId(), menuItem, quantity, note)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean printListTableByUser() {
        List<Table> tableList = IOrderServiceImpl.getInstance().getListTableByUser(UserSession.getInstance().getCurrentUser().getId());
        if (tableList == null) {
            return false;
        }
        System.out.println("Thông tin các bàn hiện tại : ");
        for (Table table : tableList) {
            System.out.printf("Id : %d | Number : %s | Capacity : %d |\n", table.getId(), table.getNumber(), table.getCapacity());
        }
        return true;
    }

    public static void printOrderItemsByUser(Scanner sc) {
        if (!printListTableByUser()) {
            return;
        }
        int table_id = InputMethod.getInt(sc, "Nhập id của bàn muốn theo dõi : ");
        Order pickOrder = IOrderServiceImpl.getInstance().getOrder(UserSession.getInstance().getCurrentUser().getId(), table_id);
        if (pickOrder == null) {
            System.out.println("Id bàn không hợp lệ!");
            return;
        }
        List<Order_Item> orderItemsList = IOrderServiceImpl.getInstance().getOrderItems(UserSession.getInstance().getCurrentUser().getId(), table_id, pickOrder);
        if (orderItemsList == null || orderItemsList.isEmpty()) {
            System.out.println("Danh sách các món đã gọi trống! Bạn chưa gọi món!");
            return;
        }
        System.out.println("Danh sách các món đã gọi : ");
        for (Order_Item orderItem : orderItemsList) {
            System.out.printf("|Id : %d | Name : %s | Price : %.2f | Quantity : %d | Status : %s |\n",
                    orderItem.getId(),
                    orderItem.getMenu_item().getName(),
                    orderItem.getUnit_price(),
                    orderItem.getQuantity(),
                    orderItem.getStatus()
            );
        }
    }

    public static void cancelItemOrder(Scanner sc) {
        printOrderItemsByUser(sc);
        int id = InputMethod.getInt(sc, "Nhập id của món muốn huỷ : ");
        int choice;
        System.out.println("Bạn có chắc chắn muốn huỷ món : ");
        System.out.println("1. Chắc chắn");
        System.out.println("2. Huỷ");
        choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
        switch (choice) {
            case 1:
                if (IOrderServiceImpl.getInstance().cancelOrderItem(id)) {
                    System.out.println("Huỷ món thành công!");
                } else {
                    System.out.println("Huỷ món thất bại!");
                }
                return;
            case 2:
                System.out.println("Đã huỷ thao tác!");
                return;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }


    }
}
