package com.restaurant.java.presentation;

import com.restaurant.java.entity.Table;
import com.restaurant.java.service.ITableServiceImpl;
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
                        System.out.println("Thêm bàn thành công!");
                    }else{
                        System.out.println("Thêm bàn thất bại!");
                    }
                    break;
                case 2:
                    updateTable(sc);
                    break;
                case 3:
                    if (deleteTable(sc)){
                        System.out.println("Xoá bàn thành công!");
                    }else{
                        System.out.println("Xoá bàn thất bại!");
                    }
                    break;
                case 4:
                    getListTable();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
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
            System.out.println("Không tìm thấy bàn hợp lệ!");
            return;
        }
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
                        System.out.println("Cập nhật thành công!");
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
                            System.out.println("Cập nhật thành công!");
                        }
                    } while (capacity <= 0);
                    break;
                case 3:
                      if(ITableServiceImpl.getInstance().update(table)){
                          System.out.println("Cập nhật thông tin thành công!");
                          return;
                      }else{
                          System.out.println("Cập nhật thông tin thất bại!");
                      }
                case 4:
                    System.out.println("Đã huỷ các thay đổi!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
                    break;
            }
        }
    }

    public static boolean deleteTable(Scanner sc) {
        int id = InputMethod.getInt(sc,"Nhập id bàn muốn xoá : ");
        return ITableServiceImpl.getInstance().delete(id);
    }

    public static void getListTable() {
        List<Table> tableList = ITableServiceImpl.getInstance().getAll();
        if(tableList == null) {
            return;
        }
        System.out.println("Danh sách các bàn hiện tại!");
        for(Table table : tableList) {
            System.out.printf("Id : %d | Number : %s | Capacity : %d | Status : %s |\n",table.getId(),table.getNumber(),table.getCapacity(),table.getStatus());
        }
    }

}
