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

    public Categories(String name) {
        this.name = name;
        this.status = true;
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

    public static void tableHeader() {
        System.out.println("+--------------------------------+");
        System.out.printf("|%-10s|%-15s|%-10s|\n","ID","Name","Status");
        System.out.println("+--------------------------------+");
    }

    public void displayData(){
        System.out.printf("|%-10s|%-15s|%-10s|\n",this.id,this.name,(this.status ? "Đang hoạt động" : "Ngừng hoạt động"));
        System.out.println("+--------------------------------+");
    }
}
