package com.restaurant.java.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHased {
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashPassword) {
        return BCrypt.checkpw(password, hashPassword);
    }
}
