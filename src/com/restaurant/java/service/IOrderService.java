package com.restaurant.java.service;

import com.restaurant.java.entity.Menu_Item;
import com.restaurant.java.entity.Order;
import com.restaurant.java.entity.Order_Item;
import com.restaurant.java.entity.Table;
import com.restaurant.java.entity.enums.OrderEnum;
import com.restaurant.java.entity.enums.OrderItemEnum;

import java.util.List;

public interface IOrderService {
    public boolean createOrder(int user_id, int table_id);
    public boolean cancelOrderItem(int order_item_id);
    public Order getOrder(int user_id, int table_id);
    public List<Order_Item> getOrderItems(int user_id, int table_id, Order order);
    public List<Table> getListTableByUser(int user_id);
    public boolean addItemOrder(int order_id, Menu_Item item, int quantity, String note);
    public List<Order_Item> getOrderItemWithStatus(OrderItemEnum orderItemEnum);
}
