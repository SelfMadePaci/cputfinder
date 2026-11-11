/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.util;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author paci
 */
public class PasswordUtil {
  // Hash a plain-text password before saving to the database
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // Verify user login input against the hashed database password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$")) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static boolean matches(String password, String password0) {
        return checkPassword(password, password0);
    }

    public static String hash(String password) {
        return hashPassword(password);
    }
}  
