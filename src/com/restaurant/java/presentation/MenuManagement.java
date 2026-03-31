package com.restaurant.java.presentation;

import com.restaurant.java.entity.Categories;
import com.restaurant.java.entity.Menu_Item;
import com.restaurant.java.service.ICategoriesService;
import com.restaurant.java.service.ICategoriesServiceImpl;
import com.restaurant.java.service.IMenuServiceImpl;
import com.restaurant.java.utils.Constant;
import com.restaurant.java.utils.InputMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuManagement {
    public static void printMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("""
                    +---------------------------------------+
                    |             Menu Management           |
                    +---------------------------------------+
                    | 1. Thêm món                           |
                    | 2. Sửa món                            |
                    | 3. Xoá món                            |
                    | 4. Tìm món theo tên                   |
                    | 5. Danh sách các món                  |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    if (insertMenu(sc)) {
                        System.out.println(Constant.GREEN_CODE + "Thêm món thành công!" + Constant.RESET_CODE);
                    } else {
                        System.out.println(Constant.RED_CODE + "Thêm món thất bại!" + Constant.RESET_CODE);
                    }
                    break;
                case 2:
                    updateMenu(sc);
                    break;
                case 3:
                    if (deleteMenu(sc)) {
                        System.out.println(Constant.GREEN_CODE + "Xoá món thành công!" + Constant.RESET_CODE);
                    } else {
                        System.out.println(Constant.RED_CODE + "Xoá món thất bại!" + Constant.RESET_CODE);
                    }
                    break;
                case 4:
                    findByName(sc);
                    break;
                case 5:
                    getListMenu();
                    break;
                case 0:
                    break;
                default:
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        } while (choice != 0);
    }

    public static boolean insertMenu(Scanner sc) {
        Categories categories = pickCategory(sc);
        if (categories == null) {
            return false;
        }
        String name = InputMethod.getString(sc, "Nhập tên món : ");
        double price;
        while (true) {
            price = InputMethod.getDouble(sc, "Nhập giá : ");
            if (price <= 0) {
                System.out.println(Constant.INPUT_ERR_MGS);
            } else {
                break;
            }
        }
        return IMenuServiceImpl.getInstance().insert(new Menu_Item(name, price), categories);
    }

    public static Categories pickCategory(Scanner sc) {
        List<Categories> listCategories = ICategoriesServiceImpl.getInstance().getList();
        if (listCategories != null) {
            System.out.println("Danh sách danh mục hiện có, hãy chọn danh mục phù hợp : ");
            int i = 1;
            for (Categories c : listCategories) {
                System.out.printf("%d, %s\n", i++, c.getName());
            }
            int choice;
            while (true) {
                choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
                if (choice < 1 || choice > listCategories.size()) {
                    System.out.println("Lựa chọn không phù hợp! Chọn lại");
                } else break;
            }
            return listCategories.get(choice - 1);
        }
        return null;
    }

    public static void updateMenu(Scanner sc) {
        int menu_id = InputMethod.getInt(sc, "Nhập id của món bạn muốn sửa : ");
        Menu_Item findMenuItem = IMenuServiceImpl.getInstance().getById(menu_id);
        if(findMenuItem == null) {
            System.out.println(Constant.INVALID_ID_FOUND);
            System.out.println(Constant.RED_CODE + "Cập nhật thất bại!" + Constant.RESET_CODE);
            return;
        }
        System.out.println("Dữ liệu cũ : ");
        Menu_Item.tableHeader();
        findMenuItem.displayDataCustomer();
        System.out.println("+-------------------------------------------------------------------------+");
        System.out.println("Chức năng bạn muốn sửa : ");
        int choice;
        do {
            System.out.println("1. Tên món");
            System.out.println("2. Giá món");
            System.out.println("3. Danh mục");
            System.out.println("4. Lưu và quay lại trang");
            System.out.println("0. Không lưu và quay lại trang");
            choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    String name = InputMethod.getString(sc, "Nhập tên mới : ");
                    findMenuItem.setName(name);
                    System.out.println(Constant.GREEN_CODE + "Cập nhật thành công" + Constant.RESET_CODE);
                    break;
                case 2:
                    double price;
                    while (true) {
                        price = InputMethod.getDouble(sc, "Nhập giá : ");
                        if (price <= 0) {
                            System.out.println(Constant.VARIABLE_ERR_MGS);
                        } else {
                            findMenuItem.setPrice(price);
                            System.out.println(Constant.GREEN_CODE + "Cập nhật thành công" + Constant.RESET_CODE);
                            break;
                        }
                    }
                case 3:
                    Categories categories = pickCategory(sc);
                    findMenuItem.setCategories(categories);
                    System.out.println(Constant.GREEN_CODE + "Cập nhật thành công" + Constant.RESET_CODE);
                    break;
                case 4:
                    if (IMenuServiceImpl.getInstance().update(findMenuItem)) {
                        System.out.println(Constant.GREEN_CODE + "Cập nhật thông tin thành công!" + Constant.RESET_CODE);

                    } else {
                        System.out.println(Constant.RED_CODE + "Cập nhật thông tin thất bại!" + Constant.RESET_CODE);
                    }
                    return;
                case 0:
                    System.out.println("Đã huỷ các thay đổi");
                    return;
                default:
                    System.out.println(Constant.INVALID_CHOICE);
                    break;
            }
        } while (true);
    }


    public static boolean deleteMenu(Scanner sc) {
        int id = InputMethod.getInt(sc, "Nhập id muốn xoá : ");
        if(IMenuServiceImpl.getInstance().getById(id) == null) {
            System.out.println(Constant.INVALID_ID_FOUND);
            return false;
        }
        System.out.println("Bạn có chắc chắn muốn xoá : ");
        System.out.println("1. Chắc chắn");
        System.out.println("2. Huỷ");
        int subChoice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
        switch (subChoice) {
            case 1:
                return IMenuServiceImpl.getInstance().delete(id);
            case 2:
                break;
            default:
                System.out.println(Constant.INVALID_CHOICE);
                break;
        }
        return false;
    }

    public static void getListMenu() {
        List<Menu_Item> listMenu = IMenuServiceImpl.getInstance().getList();
        if (listMenu == null || listMenu.isEmpty()) {
            System.out.println("Danh sách thực đơn rỗng!");
            return;
        }
        Menu_Item.tableHeader();
        for (Menu_Item menuItem : listMenu) {
            menuItem.displayDataCustomer();
            System.out.println("+-------------------------------------------------------------------------+");
        }
    }

    public static void findByName(Scanner sc) {
        String name = InputMethod.getString(sc,"Nhập tên món muốn tìm : ");
        List<Menu_Item> listMenu = IMenuServiceImpl.getInstance().findByName(name);
        if(listMenu == null || listMenu.isEmpty()){
            return;
        }
        Menu_Item.tableHeader();
        for (Menu_Item menuItem : listMenu) {
            menuItem.displayDataCustomer();
            System.out.println("+-------------------------------------------------------------------------+");
        }
    }
}
