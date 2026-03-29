package com.restaurant.java.service;

import com.restaurant.java.dao.TableDao;
import com.restaurant.java.entity.Table;
import com.restaurant.java.entity.enums.TableEnum;

import java.util.List;

public class ITableServiceImpl implements ITableService {
    TableDao tableDao = TableDao.getInstance();
    private static ITableServiceImpl instance;
    private ITableServiceImpl() {}

    public static ITableServiceImpl getInstance() {
        if (instance == null) {
            instance = new ITableServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean insert(Table table) {
        if (table == null) {
            System.out.println("Dữ liệu không hợp lệ!");
            return false;
        }
        if(isNumberExist(table.getNumber())) {
            System.out.println("Số định danh bàn đã tồn tại!");
            return false;
        }
        return tableDao.insert(table);
    }

    @Override
    public boolean update(Table table) {
        if(table == null || table.getId() <= 0){
            System.out.println("Dữ liệu không hợp lệ!");
            return false;
        }
        if(isNumberExist(table.getNumber())) {
            System.out.println("Số định danh bàn đã tồn tại!");
            return false;
        }
        return tableDao.update(table);
    }

    @Override
    public boolean delete(int id) {
        if(id <= 0){
            System.out.println("Id không hợp lệ!");
            return false;
        }
        if(getById(id) == null){
            System.out.println("Không tìm thấy bàn hợp lệ!");
            return false;
        }
        return tableDao.delete(id);
    }

    @Override
    public Table getById(int id) {
        if(id <= 0){
            System.out.println("Id không hợp lệ!");
            return null;
        }
        return tableDao.getById(id);
    }

    @Override
    public List<Table> getAll() {
        List<Table> tableList = tableDao.getList();
        if(tableList == null || tableList.isEmpty()){
            System.out.println("Danh sách bàn trống!");
            return null;
        }
        return tableList;
    }

    public boolean isNumberExist(String number) {
        return tableDao.isNumberExists(number);
    }

    @Override
    public List<Table> getTableAvailable() {
        List<Table> tableList = tableDao.getListTableAvailable();
        if(tableList == null || tableList.isEmpty()){
            System.out.println("Danh sách bàn trống!");
            return null;
        }
        return tableList;
    }

    public boolean bookedTable(int id) {
        if(id <= 0){
            System.out.println("Dữ liệu không hợp lệ!");
            return false;
        }
        return tableDao.updateTableStatus(id, TableEnum.occupied);
    }
}
