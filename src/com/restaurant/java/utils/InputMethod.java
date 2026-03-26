package com.restaurant.java.utils;

import java.util.Scanner;

public class InputMethod {
    public static String getString(Scanner sc, String label){
        while(true){
            System.out.print(label);
            String input = sc.nextLine().trim();
            if(input.isEmpty()){
                System.out.println(Constant.InputErrorMsg);
            }else{
                return input;
            }
        }
    }

    public static int getInt(Scanner sc, String label){
        while(true){
            try{
                System.out.print(label);
                int input = Integer.parseInt(sc.nextLine().trim());
                if(input < 0){
                    System.out.println(Constant.InputErrorMsg);
                }
                return input;
            }catch(NumberFormatException e){
                System.out.println(Constant.InputErrorMsg);
            }
        }
    }

    public static double getDouble(Scanner sc, String label){
        while(true){
            try{
                System.out.print(label);
                double input = Double.parseDouble(sc.nextLine().trim());
                if(input < 0){
                    System.out.println(Constant.InputErrorMsg);
                }
                return input;
            }catch(NumberFormatException e){
                System.out.println(Constant.InputErrorMsg);
            }
        }
    }
}
