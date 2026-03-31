package com.restaurant.java.presentation;

import com.restaurant.java.entity.Order;
import com.restaurant.java.entity.Order_Item;
import com.restaurant.java.entity.Table;
import com.restaurant.java.entity.enums.OrderItemEnum;
import com.restaurant.java.service.IOrderServiceImpl;
import com.restaurant.java.service.ITableServiceImpl;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.InputMethod;

import java.util.List;
import java.util.Scanner;

public class OrderManagement {
    public static void approveOrder(Scanner sc){
        List<Table> tableList = ITableServiceImpl.getInstance().getTableOccupied();
        if (tableList == null || tableList.isEmpty()) {
            return;
        }
        System.out.println("Danh sách các bàn đang được đặt hiện tại :");
        Table.tableHeader();
        for (Table table : tableList) {
            table.displayTableCustomer();
        }
        System.out.println("0. Quay lại");
        int table_id = InputMethod.getInt(sc,"Nhập id của bàn muốn theo dõi : ");
        if(table_id == 0){
            return;
        }
        Order pickOrder = IOrderServiceImpl.getInstance().getOrder(1,table_id);
        if(pickOrder == null){
            System.out.println(Constant.INVALID_ID_FOUND);
            return;
        }
        List<Order_Item> orderItemList = IOrderServiceImpl.getInstance().getAllOrderItems(table_id,pickOrder);
        if(orderItemList == null || orderItemList.isEmpty()){
            System.out.println("Danh sách đơn rỗng!");
            return;
        }
        System.out.println("Danh sách các món đã gọi : ");
        Order_Item.tableHeader();
        for (Order_Item orderItem : orderItemList) {
            orderItem.displayDataCustomer();
            System.out.println("+--------------------------------------------------------------------------+");
        }
        System.out.println("0. Quay lại");
        boolean flag = false;
        int order_item_id = InputMethod.getInt(sc,"Nhập id của đơn muốn duyệt : ");
        if(order_item_id == 0){
            return;
        }
        for (Order_Item orderItem : orderItemList) {
            if(orderItem.getId() == order_item_id){
                flag = true;
                break;
            }
        }
        if(!flag){
            System.out.println(Constant.INVALID_ID_FOUND);
            return;
        }
        if(IOrderServiceImpl.getInstance().updateOrderItemStatus(order_item_id, OrderItemEnum.pending)){
            System.out.println(Constant.GREEN_CODE + "Duyệt đơn thành công!" + Constant.RESET_CODE);
        }else{
            System.out.println(Constant.RED_CODE + "Duyệt đơn thất bại!" + Constant.RESET_CODE);
        }
    }
}
