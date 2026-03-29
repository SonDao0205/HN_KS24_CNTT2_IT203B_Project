package com.restaurant.java.presentation;

import com.restaurant.java.entity.Categories;
import com.restaurant.java.service.ICategoriesServiceImpl;
import com.restaurant.java.utils.InputMethod;

import java.util.List;
import java.util.Scanner;

public class CategoriesManagement {
    public static void printMenu(Scanner sc){
        int choice;
        do {
            System.out.println("""
                    +---------------------------------------+
                    |          Categories Management        |
                    +---------------------------------------+
                    | 1. Thêm danh mục                      |
                    | 2. Sửa danh mục                       |
                    | 3. Xoá danh mục                       |
                    | 4. Danh sách danh mục                 |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc,"Lựa chọn của bạn : ");
            switch (choice){
                case 1:
                    if(insertCategory(sc)){
                        System.out.println("Thêm danh mục thành công!");
                    }else{
                        System.out.println("Thêm danh mục thất bại!");
                    }
                    break;
                case 2:
                    break;
                case 3:
                    if(dedeleCategory(sc)){
                        System.out.println("Xoá danh mục thành công!");
                    }else{
                        System.out.println("Xoá danh mục thất bại!");
                    }
                    break;
                case 4:
                    getListCategories();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
                    break;
            }
        }while(choice != 0);
    }

    public static boolean insertCategory(Scanner sc){
        String name = InputMethod.getString(sc,"Nhập tên danh mục : ");
        return ICategoriesServiceImpl.getInstance().insert(new Categories(name));
    }

    public static boolean dedeleCategory(Scanner sc){
        int id = InputMethod.getInt(sc,"Nhập id muốn xoá : ");
        return ICategoriesServiceImpl.getInstance().delete(id);
    }

    public static void getListCategories(){
        List<Categories> categoriesList = ICategoriesServiceImpl.getInstance().getList();
        if(categoriesList == null || categoriesList.isEmpty()){
            System.out.println("Danh sách dang mục rỗng!");
            return;
        }
        System.out.println("Danh sách danh mục : ");
        for (Categories categories : categoriesList) {
            System.out.printf("|Id : %d | Name : %s | Status : %s |\n ",categories.getId(),categories.getName(),(categories.isStatus() ? "Đang hoạt động" : "Ngừng hoạt động"));
        }

    }
}
