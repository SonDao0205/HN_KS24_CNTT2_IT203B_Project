package com.restaurant.java.entity;

import com.restaurant.java.entity.enums.TableEnum;
import com.restaurant.java.utils.InputMethod;

import java.util.Scanner;

public class Menu_Item {
    private int id;
    private Categories categories;
    private String name;
    private double price;
    private boolean status;

    public Menu_Item(int id, Categories categories, String name, double price, boolean status) {
        this.id = id;
        this.categories = categories;
        this.name = name;
        this.price = price;
        this.status = status;
    }

    public Menu_Item() {
        this.status = true;
    }

    public int getId() {
        return id;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void input(Scanner sc){
        setName(InputMethod.getString(sc,"Nhập tên món : "));
        setPrice(InputMethod.getDouble(sc,"Nhập giá món : "));
    }
}
