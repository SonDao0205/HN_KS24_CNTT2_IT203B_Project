package com.restaurant.java.entity;

import com.restaurant.java.entity.enums.OrderEnum;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Order {
    private int id;
    private User customer;
    private Table table;
    private double total_amount;
    private OrderEnum status;
    private LocalDateTime created_at;

    public Order(int id, User customer, Table table, double total_amount, OrderEnum status, LocalDateTime created_at) {
        this.id = id;
        this.customer = customer;
        this.table = table;
        this.total_amount = total_amount;
        this.status = status;
        this.created_at = created_at;
    }

    public Order(User customer, Table table, double total_amount, OrderEnum status) {
        this.customer = customer;
        this.table = table;
        this.total_amount = total_amount;
        this.status = status;
        this.created_at = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public OrderEnum getStatus() {
        return status;
    }

    public void setStatus(OrderEnum status) {
        this.status = status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
