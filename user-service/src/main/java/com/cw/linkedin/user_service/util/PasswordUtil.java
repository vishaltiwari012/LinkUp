package com.cw.linkedin.user_service.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

//    Hash a password for the first time
    public static String hasPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(10));
    }

//    Checks that a plain text password matches with previously hashed one
    public static boolean checkPassword(String plainTextPassword, String hashPassword) {
        return BCrypt.checkpw(plainTextPassword, hashPassword);
    }
}
