package com.restaurant.java.presentation;

import com.restaurant.java.entity.*;
import com.restaurant.java.service.*;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.InputMethod;
import com.restaurant.java.utils.UserSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                    | 6. Đánh giá                           |
                    | 7. Thanh toán                         |
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
                case 6:
                    handleAddReview(sc);
                    break;
                case 7:
                    handlePay(sc);
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
        } while (choice != 0);
    }

    public static void getMenu() {
        List<Menu_Item> menuList = IMenuServiceImpl.getInstance().getList();
        if (menuList == null || menuList.isEmpty()) {
            System.out.println("Danh sách thực đơn rỗng!");
            return;
        }
        System.out.println("Danh sách thực đơn : ");
        Menu_Item.tableHeader();
        for (Menu_Item menuItem : menuList) {
            menuItem.displayDataCustomer();
            System.out.println("+-------------------------------------------------------------------------+");
        }
    }

    public static void pickTable(Scanner sc) {
        List<Table> tableList = ITableServiceImpl.getInstance().getTableAvailable();
        if (tableList == null || tableList.isEmpty()) {
            return;
        }

        Table.tableHeader();
        for (Table table : tableList) {
            table.displayTableCustomer();
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
                        boolean flag = false;
                        table_id = InputMethod.getInt(sc, "Nhập id của bàn muốn chọn : ");
                        for (Table table : tableList) {
                            if (table.getId() == table_id) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            System.out.println("Id bàn không phù hợp!");
                        } else {
                            break;
                        }
                    }
                    if (ITableServiceImpl.getInstance().bookedTable(table_id)) {
                        if (IOrderServiceImpl.getInstance().createOrder(UserSession.getInstance().getCurrentUser().getId(), table_id)) {
                            System.out.println(Constant.GREEN_CODE + "Đặt bàn thành công!" + Constant.RESET_CODE);
                        }
                    } else {
                        System.out.println(Constant.RED_CODE + "Đặt bàn thất bại!" + Constant.RESET_CODE);
                    }
                    return;
                case 0:
                    return;
                default:
                    System.out.println(Constant.INVALID_CHOICE);
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
            System.out.println(Constant.INVALID_ID_FOUND);
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
                System.out.println(Constant.INVALID_ID_FOUND);
            } else {
                quantity = InputMethod.getInt(sc, "Nhập số lượng : ");
                if (quantity <= 0) {
                    System.out.println(Constant.VARIABLE_ERR_MGS);
                    return;
                }
                System.out.print("Ghi chú cho món : ");
                note = sc.nextLine();
                menuItems.add(item);
                System.out.println(Constant.GREEN_CODE + "Thêm món vào giỏ hàng thành công!" + Constant.RESET_CODE);
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
                            System.out.println(Constant.RED_CODE + "Thêm món thất bại!" + Constant.RESET_CODE);
                        } else {
                            System.out.println(Constant.GREEN_CODE + "Thêm món thành công!" + Constant.RESET_CODE);
                        }
                        return;
                    default:
                        System.out.println(Constant.INVALID_CHOICE);
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
        Menu_Item.tableHeader();
        for (Menu_Item menuItem : menuItems) {
            menuItem.displayDataCustomer();
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
                        System.out.println(Constant.GREEN_CODE + "Huỷ món khỏi giỏ hàng thành công!" + Constant.RESET_CODE);
                        return menuItems;
                    case 2:
                        break;
                    default:
                        System.out.println(Constant.INVALID_CHOICE);
                        break;
                }
            } else {
                System.out.println(Constant.INVALID_ID_FOUND);
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
        Table.tableHeader();
        for (Table table : tableList) {
            table.displayTableCustomer();
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
            System.out.println(Constant.INVALID_ID_FOUND);
            return;
        }
        List<Order_Item> orderItemsList = IOrderServiceImpl.getInstance().getOrderItems(UserSession.getInstance().getCurrentUser().getId(), table_id, pickOrder);
        if (orderItemsList == null || orderItemsList.isEmpty()) {
            System.out.println("Danh sách các món đã gọi trống! Bạn chưa gọi món!");
            return;
        }
        System.out.println("Danh sách các món đã gọi : ");
        Order_Item.tableHeader();
        for (Order_Item orderItem : orderItemsList) {
            orderItem.displayDataCustomer();
            System.out.println("+--------------------------------------------------------------------------+");
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
                    System.out.println(Constant.GREEN_CODE + "Huỷ món thành công!" + Constant.RESET_CODE);
                } else {

                    System.out.println(Constant.RED_CODE + "Huỷ món thất bại!" + Constant.RESET_CODE);
                }
                return;
            case 2:
                System.out.println("Đã huỷ thao tác!");
                return;
            default:
                System.out.println(Constant.INVALID_CHOICE);
                return;
        }
    }

    public static void handleAddReview(Scanner sc) {
        String content = InputMethod.getString(sc, "Nhập nội dung đánh giá : ");
        int star = InputMethod.getInt(sc, "Nhập số sao đánh giá : ");
        if (star < 0) {
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return;
        }
        if (IReviewServiceImpl.getInstance().addReview(new Review(content, UserSession.getInstance().getCurrentUser(), star))) {
            System.out.println(Constant.GREEN_CODE + "Thêm đánh giá thành công!" + Constant.RESET_CODE);
        } else {
            System.out.println(Constant.RED_CODE + "Thêm đánh giá thất bại!" + Constant.RESET_CODE);
        }
    }

    public static void handlePay(Scanner sc) {
        if (!printListTableByUser()) {
            return;
        }
        int table_id = InputMethod.getInt(sc, "Nhập id của bàn muốn thanh toán : ");
        Order pickOrder = IOrderServiceImpl.getInstance().getOrder(UserSession.getInstance().getCurrentUser().getId(), table_id);
        if (pickOrder == null) {
            System.out.println(Constant.INVALID_ID_FOUND);
            return;
        }
        List<Order_Item> orderItemsList = IOrderServiceImpl.getInstance().getOrderItems(UserSession.getInstance().getCurrentUser().getId(), table_id, pickOrder);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy");
        System.out.println("-------Hoá Đơn-------");
        System.out.println("Tên khách hàng : " + pickOrder.getCustomer().getUsername());
        System.out.println("Số của bàn : " + pickOrder.getTable().getNumber());
        System.out.println("Giờ vào bàn : "+ pickOrder.getCreated_at().format(formatter));
        LocalDateTime checkoutAt = LocalDateTime.now();
        System.out.println("Giờ rời : " + checkoutAt.format(formatter));
        if (orderItemsList == null || orderItemsList.isEmpty()) {
            System.out.println("Danh sách các món đã gọi trống! Bạn chưa gọi món!");
        } else {
            System.out.println("Danh sách các món đã gọi : ");
            System.out.println("+--------------------------------------------------------------+");
            System.out.printf("|%-5s|%-30s|%-10s|%-13s|\n", "STT", "Name", "Quantity", "Đơn giá");
            System.out.println("+--------------------------------------------------------------+");
            int i = 1;
            for (Order_Item orderItem : orderItemsList) {
                System.out.printf("|%-5d|%-30s|%-10d|%-13.2f|\n", i++, orderItem.getMenu_item().getName(), orderItem.getQuantity(), orderItem.getUnit_price());
                System.out.println("+--------------------------------------------------------------+");
            }
        }
        System.out.println("Tổng tiền : " + pickOrder.getTotal_amount());
        System.out.println("Chức năng : ");
        System.out.println("1. Thanh toán");
        System.out.println("2. Quay lại");
        int choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
        switch (choice) {
            case 1:
                if (IOrderServiceImpl.getInstance().payOrder(pickOrder,checkoutAt)) {
                    System.out.println(Constant.GREEN_CODE + "Thanh toán thành công!" + Constant.RESET_CODE);
                } else {
                    System.out.println(Constant.RED_CODE + "Thanh toán thất bại!" +  Constant.RESET_CODE);
                }
                return;
            case 2:
                return;
            default:
                System.out.println(Constant.INVALID_CHOICE);
                return;
        }
    }
}
