package com.restaurant.java.presentation;

import com.restaurant.java.entity.Table;
import com.restaurant.java.service.IMenuServiceImpl;
import com.restaurant.java.service.ITableServiceImpl;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.InputMethod;

import java.util.List;
import java.util.Scanner;

public class TableManagement {
    public static void printMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("""
                    +---------------------------------------+
                    |             Table Management          |
                    +---------------------------------------+
                    | 1. Thêm bàn                           |
                    | 2. Sửa bàn                            |
                    | 3. Xoá bàn                            |
                    | 4. Danh sách các bàn                  |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    if (insertTable(sc)){
                        System.out.println(Constant.GREEN_CODE + "Thêm bàn thành công!" + Constant.RESET_CODE);
                    }else{
                        System.out.println(Constant.RED_CODE + "Thêm bàn thất bại!" + Constant.RESET_CODE);
                    }
                    break;
                case 2:
                    updateTable(sc);
                    break;
                case 3:
                    if (deleteTable(sc)){
                        System.out.println(Constant.GREEN_CODE + "Xoá bàn thành công!" + Constant.RESET_CODE);
                    }else{
                        System.out.println(Constant.RED_CODE + "Xoá bàn thất bại!" + Constant.RESET_CODE);
                    }
                    break;
                case 4:
                    getListTable();
                    break;
                case 0:
                    break;
                default:
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        } while (choice != 0);
    }

    public static boolean insertTable(Scanner sc) {
        String number;
        while (true) {
            number = InputMethod.getString(sc, "Nhập số định danh bàn : ");
            if(ITableServiceImpl.getInstance().isNumberExist(number)){
                System.out.println("Số định danh bàn đã tồn tại!Nhập lại!");
            }else{
                break;
            }
        }
        int capacity;
        do {
            capacity = InputMethod.getInt(sc, "Nhập số lượng chứa của bàn : ");
            if(capacity <= 0) {
                System.out.println("Số lượng chứa của bản không được nhỏ hơn 0! Nhập lại!");
            }
        } while (capacity <= 0);
        return ITableServiceImpl.getInstance().insert(new Table(
                number,
                capacity
        ));
    }

    public static void updateTable(Scanner sc) {
        int id = InputMethod.getInt(sc,"Nhập id của bàn muốn sửa : ");
        Table table = ITableServiceImpl.getInstance().getById(id);
        if(table == null) {
            System.out.println(Constant.INVALID_ID_FOUND);
            return;
        }
        System.out.println("Dữ liệu cũ : ");
        Table.tableHeader();
        table.displayTableCustomer();
        int choice;
        while(true){
            System.out.println("Danh sách các thông tin sửa : ");
            System.out.println("1. Số định danh bàn");
            System.out.println("2. Số lượng chứa của bàn");
            System.out.println("3. Lưu và quay lại");
            System.out.println("4. Không lưu và quay lại");
            choice = InputMethod.getInt(sc,"Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    String number = InputMethod.getString(sc,"Nhập số định danh bàn mới : ");
                    if(ITableServiceImpl.getInstance().isNumberExist(number)){
                        System.out.println("Số định danh bàn đã tồn tại!");
                    }else{
                        table.setNumber(number);
                        System.out.println(Constant.GREEN_CODE + "Cập nhật thành công!" + Constant.RESET_CODE);
                    }
                    break;
                case 2:
                    int capacity;
                    do {
                        capacity = InputMethod.getInt(sc, "Nhập số lượng chứa của bàn : ");
                        if(capacity <= 0) {
                            System.out.println("Số lượng chứa của bản không được nhỏ hơn 0! Nhập lại!");
                        }else{
                            table.setCapacity(capacity);
                            System.out.println(Constant.GREEN_CODE + "Cập nhật thành công!" + Constant.RESET_CODE);
                        }
                    } while (capacity <= 0);
                    break;
                case 3:
                      if(ITableServiceImpl.getInstance().update(table)){
                          System.out.println(Constant.GREEN_CODE + "Cập nhật thông tin thành công!" + Constant.RESET_CODE);
                          System.out.println();
                          return;
                      }else{
                          System.out.println(Constant.RED_CODE + "Cập nhật thông tin thất bại!" + Constant.RESET_CODE);
                          System.out.println();
                      }
                case 4:
                    System.out.println("Đã huỷ các thay đổi!");
                    return;
                default:
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        }
    }

    public static boolean deleteTable(Scanner sc) {
        int id = InputMethod.getInt(sc,"Nhập id bàn muốn xoá : ");
        System.out.println("Bạn có chắc chắn muốn xoá : ");
        System.out.println("1. Chắc chắn");
        System.out.println("2. Huỷ");
        int subChoice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
        switch (subChoice) {
            case 1:
                return ITableServiceImpl.getInstance().delete(id);
            case 2:
                break;
            default:
                System.out.println(Constant.INVALID_CHOICE);
                break;
        }
        return false;
    }

    public static void getListTable() {
        List<Table> tableList = ITableServiceImpl.getInstance().getAll();
        if(tableList == null) {
            return;
        }
        System.out.println("Danh sách các bàn hiện tại!");
        Table.tableHeader();
        for(Table table : tableList) {
            table.displayTableCustomer();
        }
    }

}
