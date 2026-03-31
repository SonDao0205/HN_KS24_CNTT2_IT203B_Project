package com.restaurant.java.presentation;

import com.restaurant.java.entity.Categories;
import com.restaurant.java.service.ICategoriesServiceImpl;
import com.restaurant.java.utils.Constant;
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
                        System.out.println(Constant.GREEN_CODE + "Thêm danh mục thành công!" + Constant.RESET_CODE);
                    }else{
                        System.out.println(Constant.RED_CODE + "Thêm danh mục thất bại!" + Constant.RESET_CODE);
                    }
                    break;
                case 2:
                    if(updateCategory(sc)){
                        System.out.println(Constant.GREEN_CODE + "Cập nhật thành công!" + Constant.RESET_CODE);
                    }else{
                        System.out.println(Constant.RED_CODE + "Cập nhật thất bại!" + Constant.RESET_CODE);
                    }
                    break;
                case 3:
                    if(deleteCategory(sc)){
                        System.out.println(Constant.GREEN_CODE + "Xoá danh mục thành công!" + Constant.RESET_CODE);
                    }else{
                        System.out.println(Constant.RED_CODE + "Xoá danh mục thất bại!" + Constant.RESET_CODE);
                    }
                    break;
                case 4:
                    getListCategories();
                    break;
                case 0:
                    break;
                default:
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        }while(choice != 0);
    }

    public static boolean insertCategory(Scanner sc){
        String name = InputMethod.getString(sc,"Nhập tên danh mục : ");
        return ICategoriesServiceImpl.getInstance().insert(new Categories(name));
    }

    public static boolean deleteCategory(Scanner sc){
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
        Categories.tableHeader();
        for (Categories categories : categoriesList) {
            categories.displayData();
        }
    }

    public static boolean updateCategory(Scanner sc){
        int id = InputMethod.getInt(sc,"Nhập id danh mục muốn sửa : ");
        Categories categories = ICategoriesServiceImpl.getInstance().getById(id);
        if(categories == null){
            System.out.println(Constant.INVALID_ID_FOUND);
            return false;
        }
        System.out.println("Dữ liệu cũ : ");
        Categories.tableHeader();
        categories.displayData();
        System.out.println("Cập nhật dữ liệu : ");
        String name = InputMethod.getString(sc,"Nhập tên mới : ");
        return ICategoriesServiceImpl.getInstance().update(id,name);



    }
}
