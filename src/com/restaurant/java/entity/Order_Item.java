package com.restaurant.java.entity;

import com.restaurant.java.entity.enums.OrderItemEnum;

public class Order_Item {
    private int id;
    private Order order;
    private Menu_Item menu_item;
    private double unit_price;
    private int quantity;
    private OrderItemEnum status;
    private String note;

    public Order_Item(int id, Order order, Menu_Item menu_item, double unit_price, int quantity, OrderItemEnum status, String note) {
        this.id = id;
        this.order = order;
        this.menu_item = menu_item;
        this.unit_price = unit_price;
        this.quantity = quantity;
        this.status = status;
        this.note = note;
    }

    public Order_Item(Order order, Menu_Item menu_item, double unit_price, int quantity, String note) {
        this.order = order;
        this.menu_item = menu_item;
        this.unit_price = unit_price;
        this.quantity = quantity;
        this.status = OrderItemEnum.pending;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Menu_Item getMenu_item() {
        return menu_item;
    }

    public void setMenu_item(Menu_Item menu_item) {
        this.menu_item = menu_item;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderItemEnum getStatus() {
        return status;
    }

    public void setStatus(OrderItemEnum status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
