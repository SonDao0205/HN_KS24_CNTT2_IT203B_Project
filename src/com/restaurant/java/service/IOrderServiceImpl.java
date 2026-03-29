package com.restaurant.java.service;

import com.restaurant.java.dao.OrderDao;
import com.restaurant.java.entity.Order;
import com.restaurant.java.entity.enums.OrderEnum;

import java.util.List;

public class IOrderServiceImpl implements IOrderService {
    private static IOrderServiceImpl instance;

    private IOrderServiceImpl() {
    }

    public static IOrderServiceImpl getInstance() {
        if (instance == null) {
            instance = new IOrderServiceImpl();
        }
        return instance;
    }

    OrderDao orderDao = OrderDao.getInstance();

    @Override
    public boolean createOrder(int user_id, int table_id) {
        if (user_id <= 0 || table_id <= 0) {
            System.out.println("Dữ liệu không phù hợp!");
            return false;
        }
        return orderDao.createOrder(user_id, table_id);
    }

    @Override
    public boolean updateOrder(int order_id, OrderEnum orderStatus) {
        return false;
    }

    @Override
    public boolean deleteOrder(int order_id) {
        return false;
    }

    @Override
    public boolean updateAmountOrder(int order_id, double price) {
        return false;
    }

    @Override
    public Order getOrder(int order_id) {
        return null;
    }

    @Override
    public List<Order> getListByTable(int table_id) {
        return List.of();
    }

    @Override
    public List<Order> getAllList() {
        return List.of();
    }
}
