package com.restaurant.java.service;

import com.restaurant.java.entity.Table;

import java.util.List;

public interface ITableService {
    public boolean insert(Table table);
    public boolean update(Table table);
    public boolean delete(int id);
    public Table getById(int id);
    public List<Table> getAll();
    public List<Table> getTableAvailable();
}
