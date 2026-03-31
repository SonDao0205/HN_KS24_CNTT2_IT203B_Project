package com.restaurant.java.utils;

public class Constant {
    public static final String RED_CODE = "\u001B[31m";
    public static final String RESET_CODE = "\u001B[0m";
    public static final String GREEN_CODE = "\u001B[32m";
    public static final String YELLOW_CODE = "\u001B[33m";
    public static final String INPUT_ERR_MGS = RED_CODE + "Dữ liệu nhập vào không hợp lệ! Nhập lại!" + RESET_CODE;
    public static final String VARIABLE_ERR_MGS = RED_CODE + "Dữ liệu không hợp lệ!" + RESET_CODE;
    public static final String INVALID_ID_FOUND = YELLOW_CODE + "Không tìm thấy phần tử với id này!" + RESET_CODE;
}
