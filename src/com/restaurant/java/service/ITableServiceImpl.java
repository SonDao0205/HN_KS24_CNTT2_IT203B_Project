package com.restaurant.java.service;

import com.restaurant.java.dao.TableDao;
import com.restaurant.java.entity.Table;
import com.restaurant.java.entity.enums.TableEnum;
import com.restaurant.java.utils.Constant;

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
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }
        if(isNumberExist(table.getNumber())) {
            System.out.println(Constant.YELLOW_CODE + "Số định danh bàn đã tồn tại!" + Constant.RESET_CODE);
            return false;
        }
        return tableDao.insert(table);
    }

    @Override
    public boolean update(Table table) {
        if(table == null || table.getId() <= 0){
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }
        if(isNumberExist(table.getNumber())) {
            System.out.println(Constant.YELLOW_CODE + "Số định danh bàn đã tồn tại!" + Constant.RESET_CODE);
            return false;
        }
        return tableDao.update(table);
    }

    @Override
    public boolean delete(int id) {
        if(id <= 0){
            System.out.println(Constant.INVALID_ID_FOUND);
            return false;
        }
        if(getById(id) == null){
            System.out.println(Constant.INVALID_ID_FOUND);
            return false;
        }
        return tableDao.delete(id);
    }

    @Override
    public Table getById(int id) {
        if(id <= 0){
            System.out.println(Constant.INVALID_ID_FOUND);
            return null;
        }
        return tableDao.getById(id);
    }

    @Override
    public List<Table> getAll() {
        List<Table> tableList = tableDao.getList();
        if(tableList == null || tableList.isEmpty()){
            System.out.println(Constant.YELLOW_CODE + "Danh sách bàn trống!" + Constant.RESET_CODE);
            return null;
        }
        return tableList;
    }

    public boolean isNumberExist(String number) {
        return tableDao.isNumberExists(number);
    }

    @Override
    public List<Table> getTableAvailable() {
        List<Table> tableList = tableDao.getListTable(TableEnum.available);
        if(tableList == null || tableList.isEmpty()){
            System.out.println(Constant.YELLOW_CODE + "Danh sách bàn trống rỗng!" + Constant.RESET_CODE);
            return null;
        }
        return tableList;
    }

    public List<Table> getTableOccupied(){
        List<Table> tableList = tableDao.getListTable(TableEnum.occupied);
        if(tableList == null || tableList.isEmpty()){
            System.out.println(Constant.YELLOW_CODE + "Danh sách bàn trống rỗng!" + Constant.RESET_CODE);
            return null;
        }
        return tableList;
    }

    public boolean bookedTable(int id) {
        if(id <= 0){
            System.out.println(Constant.VARIABLE_ERR_MGS);
            return false;
        }
        return tableDao.updateTableStatus(id, TableEnum.occupied);
    }
}
