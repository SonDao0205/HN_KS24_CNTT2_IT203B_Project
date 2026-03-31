package com.restaurant.java.presentation;

import com.restaurant.java.entity.User;
import com.restaurant.java.entity.enums.UserRoleEnum;
import com.restaurant.java.service.ITableServiceImpl;
import com.restaurant.java.service.IUserService;
import com.restaurant.java.service.IUserServiceImpl;
import com.restaurant.java.utils.InputMethod;

import java.util.List;
import java.util.Scanner;

public class UserManagement {
    public static void printMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("""
                    +---------------------------------------+
                    |             User Management           |
                    +---------------------------------------+
                    | 1. Thêm tài khoản                     |
                    | 2. Sửa thông tin tài khoản            |
                    | 3. Xoá tài khoản                      |
                    | 4. Khoá tài khoản                     |
                    | 5. Danh sách các tài khoản            |
                    | 0. Thoát                              |
                    +---------------------------------------+""");
            choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    if (insertUser(sc)) {
                        System.out.println("Thêm tài khoản thành công!");
                    } else {
                        System.out.println("Thêm tài khoản thất bại");
                    }
                    break;
                case 2:
                    updateUser(sc);
                    break;
                case 3:
                    if (deleteUser(sc)) {
                        System.out.println("Thêm tài khoản thành công!");
                    } else {
                        System.out.println("Thêm tài khoản thất bại");
                    }
                    break;
                case 4:
                    if (blockUser(sc)) {
                        System.out.println("Khoá tài khoản thành công!");
                    } else {
                        System.out.println("Khoá tài khoản thất bại!");
                    }
                    break;
                case 5:
                    getList();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
                    break;
            }
        } while (choice != 0);
    }

    public static boolean insertUser(Scanner sc) {
        String username = InputMethod.getString(sc, "Nhập tên tài khoản : ");
        String password = InputMethod.getString(sc, "Nhập mật khẩu : ");
        UserRoleEnum role = pickRole(sc);
        return IUserServiceImpl.getInstance().insert(new User(username, password, role));
    }

    public static UserRoleEnum pickRole(Scanner sc) {
        int choice;
        do {
            System.out.println("Danh sách các role : ");
            System.out.println("1. Customer");
            System.out.println("2. Chef");
            choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    return UserRoleEnum.customer;
                case 2:
                    return UserRoleEnum.chef;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
                    break;
            }
        } while (true);
    }

    public static void updateUser(Scanner sc) {
        int id = InputMethod.getInt(sc, "Nhập id tài khoản muốn sửa : ");
        User user = IUserServiceImpl.getInstance().getById(id);
        if (user == null) {
            System.out.println("Id không hợp lệ!");
            return;
        }
        int choice;
        do {
            System.out.println("Danh sách các thông tin sửa : ");
            System.out.println("1. Username");
            System.out.println("2. Role");
            System.out.println("3. Lưu và quay lại");
            System.out.println("4. Không lưu và quay lại");
            choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
            switch (choice) {
                case 1:
                    String username = InputMethod.getString(sc, "Nhập tên tài khoản mới : ");
                    if (IUserServiceImpl.getInstance().getByUsername(username) != null) {
                        System.out.println("Tên tài khoản đã tồn tại!Nhập lại!");
                    } else {
                        user.setUsername(username);
                    }
                    break;
                case 2:
                    user.setRole(pickRole(sc));
                    break;
                case 3:
                    if (IUserServiceImpl.getInstance().update(user)) {
                        System.out.println("Cập nhật thông tin thành công!");
                    } else {
                        System.out.println("Cập nhật thông tin thất bại!");
                    }
                    return;
                case 4:
                    System.out.println("Đã huỷ các thay đổi!");
                    return;
                default:
                    System.out.println("Lựa chọn không phù hợp!");
                    break;
            }
        } while (true);
    }

    public static boolean deleteUser(Scanner sc) {
        int id = InputMethod.getInt(sc, "Nhập id tài khoản muốn xoá : ");
        System.out.println("Bạn có chắc chắn muốn xoá : ");
        System.out.println("1. Chắc chắn");
        System.out.println("2. Huỷ");
        int subChoice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
        switch (subChoice) {
            case 1:
                return IUserServiceImpl.getInstance().delete(id);
            case 2:
                break;
            default:
                System.out.println("Lựa chọn không phù hợp!");
                break;
        }
        return false;
    }

    public static void getList() {
        List<User> users = IUserServiceImpl.getInstance().getAll();
        if (users == null || users.isEmpty()) {
            System.out.println("Danh sách tài khoản rỗng!");
            return;
        }
        System.out.println("Danh sách tất cả tài khoản :");
        for (User user : users) {
            System.out.printf("Id : %d | Username : %s | Role : %s | Status : %s |\n", user.getId(), user.getUsername(), user.getRole(), (user.isStatus() ? "Đang hoạt động" : "Ngừng hoạt động"));
        }
    }

    public static boolean blockUser(Scanner sc) {
        int id = InputMethod.getInt(sc, "Nhập id của người dùng muốn khoá : ");
        User user = IUserServiceImpl.getInstance().getById(id);
        if (user == null) {
            System.out.println("Không tìm thấy người dùng hợp lệ!");
            return false;
        }
        System.out.println("Bạn có chắc chắn muốn khoá người dùng này : ");
        System.out.println("1. Chắc chắn");
        System.out.println("2. Huỷ");
        int choice = InputMethod.getInt(sc, "Lựa chọn của bạn : ");
        return switch (choice) {
            case 1 -> IUserServiceImpl.getInstance().blockUser(user);
            case 2 -> false;
            default -> {
                System.out.println("Lựa chọn không phù hợp!");
                yield false;
            }
        };

    }
}
