package com.restaurant.java.entity;

import com.restaurant.java.entity.enums.TableEnum;
import com.restaurant.java.utils.InputMethod;

import java.util.Scanner;

public class Table {
    private int id;
    private String number;
    private int capacity;
    private TableEnum status;

    public Table(int id, String number, int capacity, TableEnum status) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.status = status;
    }

    public Table(String number, int capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    public Table() {
        this.status = TableEnum.available;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public TableEnum getStatus() {
        return status;
    }

    public void setStatus(TableEnum status) {
        this.status = status;
    }

    public static void tableHeader(){
        System.out.println("+--------------------------------+");
        System.out.printf("|%-10s|%-10s|%-10s|\n","ID", "Number","Capacity");
        System.out.println("+--------------------------------+");
    }

    public void displayTableCustomer(){
        System.out.printf("|%-10d|%-10s|%-10d|\n",this.id, this.number,this.capacity);
        System.out.println("+--------------------------------+");
    }
}
