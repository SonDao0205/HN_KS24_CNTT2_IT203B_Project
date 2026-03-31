package com.restaurant.java.presentation;

import com.restaurant.java.entity.Order_Item;
import com.restaurant.java.entity.enums.OrderItemEnum;
import com.restaurant.java.service.IOrderServiceImpl;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.InputMethod;
import com.restaurant.java.utils.UserSession;

import java.util.List;
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
                case 1:
                    listOrderItem();
                    break;
                case 2:
                    updateOrderItemStatus(sc);
                    break;
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
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        } while (choice != 0);
    }

    public static void listOrderItem() {
        List<Order_Item> orderItemList = IOrderServiceImpl.getInstance().getOrderItemWithStatus(OrderItemEnum.pending);
        if (orderItemList == null) {
            return;
        }
        System.out.println("Danh sách các món đang chờ : ");
        for (Order_Item orderItem : orderItemList) {
            System.out.printf("|Id : %d | Name : %s | Quantity : %d | Status : %s |\n",
                    orderItem.getId(), orderItem.getMenu_item().getName(), orderItem.getQuantity(), orderItem.getStatus());
        }
    }

    public static void updateOrderItemStatus(Scanner sc) {
        List<Order_Item> orderItemList = IOrderServiceImpl.getInstance().getOrderItemExcludeStatus();
        if (orderItemList == null) {
            return;
        }
        System.out.println("Danh sách các đơn hiện tại : ");
        for (Order_Item orderItem : orderItemList) {
            System.out.printf("|Id : %d | Name : %s | Quantity : %d | Status : %s |\n",
                    orderItem.getId(), orderItem.getMenu_item().getName(), orderItem.getQuantity(), orderItem.getStatus());
        }
        System.out.println("0. Quay lại");
        int id = InputMethod.getInt(sc, "Nhập id của đơn muốn cập nhật : ");
        if(id == 0){
            return;
        }
        OrderItemEnum status = null;
        for (Order_Item orderItem : orderItemList) {
            if(orderItem.getId() == id){
                status = orderItem.getStatus();
            }
        }

        if (status == null) {
            System.out.println(Constant.INVALID_ID_FOUND);
            return;
        }
        if(status == OrderItemEnum.cancel) {
            System.out.println("Đơn này đã bị huỷ! Không thể cập nhật!");
            return;
        }else if(status == OrderItemEnum.served){
            System.out.println("Đơn này đã được phục vụ!");
            return;
        }else if(status == OrderItemEnum.waiting){
            System.out.println("Đơn này chưa được duyệt!");
            return;
        }
        OrderItemEnum nextStatus = switch (status) {
            case pending -> OrderItemEnum.cooking;
            case cooking -> OrderItemEnum.ready;
            case ready -> OrderItemEnum.served;
            default -> null;
        };

        System.out.printf("Trạng thái đơn hiện tại là (%s) bạn có muốn đổi sang trạng thái (%s) không : \n",status,nextStatus);
        System.out.println("1. Chắc chắn");
        System.out.println("2. Huỷ");
        int choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
        switch (choice) {
            case 1:
                if (IOrderServiceImpl.getInstance().updateOrderItemStatus(id, status)) {
                    System.out.println(Constant.GREEN_CODE + "Cập nhật thành công!" + Constant.RESET_CODE);
                } else {
                    System.out.println(Constant.RED_CODE + "Cập nhật thất bại!" + Constant.RESET_CODE);
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
