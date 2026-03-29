package com.restaurant.java.service;

import com.restaurant.java.entity.Order;
import com.restaurant.java.entity.enums.OrderEnum;

import java.util.List;

public interface IOrderService {
    public boolean createOrder(int user_id, int table_id);
    public boolean updateOrder(int order_id, OrderEnum orderStatus);
    public boolean deleteOrder(int order_id);
    public boolean updateAmountOrder(int order_id, double price);
    public Order getOrder(int order_id);
    public List<Order> getListByTable(int table_id);
    public List<Order> getAllList();
}
