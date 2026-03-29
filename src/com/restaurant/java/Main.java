package com.restaurant.java;

import com.restaurant.java.presentation.MainMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MainMenu.printMenu(sc);
    }
}
