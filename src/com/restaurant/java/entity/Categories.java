package com.restaurant.java.entity;

import com.restaurant.java.utils.InputMethod;

import java.util.Scanner;

public class Categories {
    private int id;
    private String name;
    private boolean status;

    public Categories(int id, String name, boolean status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Categories() {
        this.status = true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void input(Scanner sc){
        setName(InputMethod.getString(sc,"Nhập tên danh mục : "));
    }
}
